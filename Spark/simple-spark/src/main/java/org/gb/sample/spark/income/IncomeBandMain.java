package org.gb.sample.spark.income;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class IncomeBandMain {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Person count per income band");
		JavaSparkContext sc = new JavaSparkContext(conf);
		String nameVsIncomeFile = args[0];
		List<Integer> incomeSlabs = Stream.of(args[1].split(",")).map(Integer::parseInt).collect(Collectors.toList());
		JavaRDD<String> nameVsIncomeLines = sc.textFile(nameVsIncomeFile);
		showCountPerIncomeBand(nameVsIncomeLines, incomeSlabs);
		sc.close();
	}

	private static void showCountPerIncomeBand(JavaRDD<String> nameVsIncomeLines, List<Integer> incomeSlabs) {
		JavaRDD<Band> incomeBands = nameVsIncomeLines.map(nameVsIncomeLine -> nameVsIncomeLine.split(","))
				.filter(tokenizedNameVsIncomeLine -> tokenizedNameVsIncomeLine.length == 3)
				.map(tokenizedNameVsIncomeLine -> mapToBand(tokenizedNameVsIncomeLine[2], incomeSlabs))
				.filter(band -> band != null);
		int noOfPartitions = incomeBands.rdd().getNumPartitions();
		System.out.printf("No. of partitions: %d.\n", noOfPartitions);
		Map<Band, Long> countPerIncomeBand = nameVsIncomeLines.map(nameVsIncomeLine -> nameVsIncomeLine.split(","))
				.filter(tokenizedNameVsIncomeLine -> tokenizedNameVsIncomeLine.length == 3)
				.map(tokenizedNameVsIncomeLine -> mapToBand(tokenizedNameVsIncomeLine[2], incomeSlabs))
				.filter(band -> band != null).countByValue();
		countPerIncomeBand.forEach((band, count) -> System.out.println(band + " : " + count));
	}

	private static Band mapToBand(String incomeStr, List<Integer> incomeSlabs) {
		Integer income = null;
		try {
			income = Integer.parseInt(incomeStr);
		} catch (NumberFormatException nfe) {
			System.err.println(String.format("incomeStr: %s could not be converted to an integer", incomeStr));
			return null;
		}

		Band band = null;
		for (int i = 0; i < incomeSlabs.size(); i++) {
			Integer currentSlab = incomeSlabs.get(i);
			Integer nextSlab = null;

			if ((i + 1) < incomeSlabs.size()) {
				nextSlab = incomeSlabs.get(i + 1);
			}

			if (nextSlab == null) {
				if (income.intValue() > currentSlab.intValue()) {
					band = new Band(currentSlab, nextSlab);
					break;
				}
			} else if (income.intValue() > currentSlab.intValue() && income.intValue() <= nextSlab.intValue()) {
				band = new Band(currentSlab, nextSlab);
				break;
			}
		}
		System.out.printf("Income: %d falls in band: %s.\n", income.intValue(), band);
		return band;
	}

}

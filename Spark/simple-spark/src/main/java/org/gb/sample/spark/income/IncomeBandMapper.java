package org.gb.sample.spark.income;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;

public class IncomeBandMapper implements Serializable {

	private static final long serialVersionUID = 1272508542730740489L;

	Map<Band, Long> getCountPerIncomeBand(JavaRDD<String> nameVsIncomeLines, List<Integer> incomeSlabs) {
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
		return countPerIncomeBand;
	}

	private Band mapToBand(String incomeStr, List<Integer> incomeSlabs) {
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

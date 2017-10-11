package org.gb.sample.spark.temperature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class Main2 {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Max-min temperature finder");
		JavaSparkContext sc = new JavaSparkContext(conf);
		String inputFile = args[0];
		JavaRDD<String> lines = sc.textFile(inputFile);
		List<Tuple2<Integer, Integer>> tempVsYearSortedByTemp = lines.flatMapToPair(Main2::splitMaxMinTemp).sortByKey(false)
				.collect();
		Tuple2<Integer, Integer> maxTempVsYear = tempVsYearSortedByTemp.get(0);
		Tuple2<Integer, Integer> minTempVsYear = tempVsYearSortedByTemp.get(tempVsYearSortedByTemp.size() - 1);

		System.out.println("Overall max-min temperature.");
		System.out.println("-----------------------------------------");
		System.out.println(maxTempVsYear._1() + ":" + maxTempVsYear._2());
		System.out.println(minTempVsYear._1() + ":" + minTempVsYear._2());
		System.out.println("-----------------------------------------");

		sc.close();
	}

	private static Iterator<Tuple2<Integer, Integer>> splitMaxMinTemp(String line) {
		String[] parts = line.split(":");
		List<Tuple2<Integer, Integer>> maxMinTempTuples = new ArrayList<>();
		Integer year = null;
		try {
			year = Integer.parseInt(parts[0]);
		} catch (NumberFormatException nfe) {
			return maxMinTempTuples.iterator();
		}

		String[] temps = parts[1].split(",");

		Integer maxTemp = null;
		try {
			maxTemp = Integer.parseInt(temps[0]);
			maxMinTempTuples.add(new Tuple2<Integer, Integer>(maxTemp, year));
		} catch (NumberFormatException nfe) {
			System.err.println(String.format("max-temp: %s could not be converted to an integer", temps[0]));
		}

		Integer minTemp = null;
		try {
			minTemp = Integer.parseInt(temps[1]);
			maxMinTempTuples.add(new Tuple2<Integer, Integer>(minTemp, year));
		} catch (NumberFormatException nfe) {
			System.err.println(String.format("min-temp: %s could not be converted to an integer", temps[1]));
		}

		return maxMinTempTuples.iterator();
	}
}

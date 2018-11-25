package org.gb.sample.spark.nytaxitrips;

import java.time.LocalTime;
import java.time.Month;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;

public class MonthlyMaxMinFaresMain {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf()
				.setAppName("New York Yellow Taxis - monthly maximum and minimum fares for a given timeband");
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		LongAccumulator defectiveRecords = sparkContext.sc().longAccumulator("Defective Records");
		JavaRDD<TaxiTrip> tripData = loadTripData(sparkContext, args[0], defectiveRecords);
		TripAnalyzer tripAnalyzer = new RDDTripAnalyzer(tripData);
		TimeBand earlyMorning = new TimeBand(LocalTime.MIDNIGHT, LocalTime.of(6, 0));
		Map<Month, MaxMinFares> maxMinFaresPerMonth = tripAnalyzer.getMaxMinFaresPerMonth(earlyMorning, false);
		/*maxMinFaresPerMonth.forEach((month, maxMinFares) -> System.out.printf(
				"Month: %s, timeband: %s, maximum fare: %s, minimum fare: %s.\n", month, maxMinFares.getTimeBand(),
				maxMinFares.getMaxFareAmount(), maxMinFares.getMinFareAmount()));*/

		sparkContext.close();
	}

	private static JavaRDD<TaxiTrip> loadTripData(JavaSparkContext sparkContext, String taxiTripFile,
			LongAccumulator defectiveRecords) {
		TripDataLoader loader = new TextFileLoader(sparkContext, taxiTripFile, defectiveRecords);
		return loader.fetchRecords();
	}
}

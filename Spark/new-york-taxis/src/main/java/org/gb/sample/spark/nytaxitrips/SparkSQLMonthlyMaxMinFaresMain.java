package org.gb.sample.spark.nytaxitrips;

import java.time.LocalTime;
import java.time.Month;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSQLMonthlyMaxMinFaresMain {

	public static void main(String[] args) {
		
		SparkSession session = SparkSession.builder()
				.appName("New York Yellow Taxis - monthly maximum and minimum fares for a given timeband - using Spark SQL").getOrCreate();
		Dataset<Row> tripData = session.read().csv(args[0]);
		TripAnalyzer tripAnalyzer = new SQLTripAnalyzer(tripData);
		TimeBand earlyMorning = new TimeBand(LocalTime.MIDNIGHT, LocalTime.of(6, 0));
		Map<Month, MaxMinFares> maxMinFaresPerMonth = tripAnalyzer.getMaxMinFaresPerMonth(earlyMorning, true);
		maxMinFaresPerMonth.forEach((month, maxMinFares) -> System.out.printf(
				"Month: %s, timeband: %s, maximum fare: %s, minimum fare: %s.\n", month, maxMinFares.getTimeBand(),
				maxMinFares.getMaxFareAmount(), maxMinFares.getMinFareAmount()));
		//session.close();
	}
}

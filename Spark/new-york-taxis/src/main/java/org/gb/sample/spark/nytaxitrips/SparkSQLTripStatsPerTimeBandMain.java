package org.gb.sample.spark.nytaxitrips;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;

public class SparkSQLTripStatsPerTimeBandMain {

	public static void main(String[] args) {
		SparkSession session = SparkSession.builder()
				.appName("New York Yellow Taxis - trip statistics per time band - using Spark SQL").getOrCreate();
		Dataset<Row> tripData = session.read().csv(args[0]);
		SQLTripAnalyzer tripAnalyzer = new SQLTripAnalyzer(tripData);

		StructField startTimeField = DataTypes.createStructField("startTime", DataTypes.StringType, true);
		StructField endTimeField = DataTypes.createStructField("endTime", DataTypes.StringType, true);
		StructField[] timeBandFields = { startTimeField, endTimeField };
		session.udf().register("mapToTimeBandUDF",
				(String pickupDateTimeStr) -> tripAnalyzer.mapToTimeBandUDF(pickupDateTimeStr),
				DataTypes.createStructType(timeBandFields));

		TimeBand earlyMorning = new TimeBand(LocalTime.MIDNIGHT, LocalTime.of(6, 0));
		TimeBand morning = new TimeBand(LocalTime.of(6, 0), LocalTime.NOON);
		TimeBand afternoon = new TimeBand(LocalTime.NOON, LocalTime.of(18, 0));
		TimeBand night = new TimeBand(LocalTime.of(18, 0));
		List<TimeBand> timeBands = Arrays.asList(earlyMorning, morning, afternoon, night);

		Map<TimeBand, TripStats> tripStatsPerTimeBand = tripAnalyzer.getTripStatsPerTimeBand(timeBands);

		tripStatsPerTimeBand.forEach(SparkSQLTripStatsPerTimeBandMain::printTripStatsPerTimeBand);
		session.close();
	}

	private static void printTripStatsPerTimeBand(TimeBand timeBand, TripStats tripStats) {
		Integer totalTripCount = tripStats.getTotalTripCount();
		Double avgPassengersPerTrip = tripStats.getAvgPassengersPerTrip();
		Double avgDistancePerTrip = tripStats.getAvgDistancePerTrip();
		BigDecimal avgFarePerTrip = tripStats.getAvgFarePerTrip();

		System.out.printf("%s --> total trips: %d, total passengers: %d, avg. passengers per trip: %6.2f, "
				+ "total distance: %6.2f, avg. distance per trip: %6.2f, total fare: %s, avg. fare per trip: %s.\n",
				timeBand, totalTripCount, tripStats.getTotalPassengerCount(), avgPassengersPerTrip,
				tripStats.getTotalDistanceCovered(), avgDistancePerTrip, tripStats.getTotalFareAmount(),
				avgFarePerTrip);
	}

}

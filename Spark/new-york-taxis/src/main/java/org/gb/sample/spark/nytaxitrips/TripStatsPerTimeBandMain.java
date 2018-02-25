package org.gb.sample.spark.nytaxitrips;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class TripStatsPerTimeBandMain {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("New York Yellow Taxi trips - trip statistics per time band");
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		sparkContext.sc().addSparkListener(new TripAnalysisListener());
		JavaRDD<TaxiTrip> tripData = loadTripData(sparkContext, args[0]);
		TripAnalyzer tripAnalyzer = new TripAnalyzer(tripData);
		
		TimeBand earlyMorning = new TimeBand(LocalTime.MIDNIGHT, LocalTime.of(6, 0));
		TimeBand morning = new TimeBand(LocalTime.of(6, 0), LocalTime.NOON);
		TimeBand afternoon = new TimeBand(LocalTime.NOON, LocalTime.of(18, 0));
		TimeBand night = new TimeBand(LocalTime.of(18, 0));
		List<TimeBand> timeBands = Arrays.asList(earlyMorning, morning, afternoon, night);
		
		Map<TimeBand, TripStats> tripStatsPerTimeBand = tripAnalyzer.getTripStatsPerTimeBand(timeBands);
		tripStatsPerTimeBand.forEach(TripStatsPerTimeBandMain::printTripStatsPerTimeBand);
		
		sparkContext.close();
	}

	private static void printTripStatsPerTimeBand(TimeBand timeBand, TripStats tripStats) {
		Integer totalTripCount = tripStats.getTotalTripCount();
		Double avgPassengersPerTrip = tripStats.getAvgPassengersPerTrip();
		Double avgDistancePerTrip = tripStats.getAvgDistancePerTrip();
		BigDecimal avgFarePerTrip = tripStats.getAvgFarePerTrip();

		System.out.printf(
				"%s --> total trips: %d, total passengers: %d, avg. passengers per trip: %6.2f, "
						+ "total distance: %6.2f, avg. distance per trip: %6.2f, total fare: %s, avg. fare per trip: %s.\n",
				timeBand, totalTripCount, tripStats.getTotalPassengerCount(), avgPassengersPerTrip,
				tripStats.getTotalDistanceCovered(), avgDistancePerTrip, tripStats.getTotalFareAmount(),
				avgFarePerTrip);
	}

	private static JavaRDD<TaxiTrip> loadTripData(JavaSparkContext sparkContext, String taxiTripFile) {
		TripDataLoader loader = new TextFileLoader(sparkContext, taxiTripFile);
		return loader.fetchRecords();
	}
}

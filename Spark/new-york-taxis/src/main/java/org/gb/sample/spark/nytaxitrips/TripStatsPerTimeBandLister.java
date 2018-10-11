package org.gb.sample.spark.nytaxitrips;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.util.LongAccumulator;

class TripStatsPerTimeBandLister {

	private static final TripStatsPerTimeBandLister singleton = new TripStatsPerTimeBandLister();

	private TripStatsPerTimeBandLister() {

	}

	static TripStatsPerTimeBandLister getInstance() {
		return singleton;
	}

	void collectTripStats(String taxiTripFile, boolean useBroadcast) {
		String appName = "New York Yellow Taxi trips - trip statistics per time band";
		appName = useBroadcast ? appName.concat(" - broadcasting time bands") : appName;

		SparkConf conf = new SparkConf().setAppName(appName);
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		sparkContext.sc().addSparkListener(new TripAnalysisListener());
		LongAccumulator defectiveRecords = sparkContext.sc().longAccumulator("Defective Records");

		JavaRDD<TaxiTrip> tripData = loadTripData(sparkContext, taxiTripFile, defectiveRecords);
		TripAnalyzer tripAnalyzer = new TripAnalyzer(tripData);

		TimeBand earlyMorning = new TimeBand(LocalTime.MIDNIGHT, LocalTime.of(6, 0));
		TimeBand morning = new TimeBand(LocalTime.of(6, 0), LocalTime.NOON);
		TimeBand afternoon = new TimeBand(LocalTime.NOON, LocalTime.of(18, 0));
		TimeBand night = new TimeBand(LocalTime.of(18, 0));
		List<TimeBand> timeBands = Arrays.asList(earlyMorning, morning, afternoon, night);

		Map<TimeBand, TripStats> tripStatsPerTimeBand = null;
		if (useBroadcast) {
			Broadcast<List<TimeBand>> bdcstTimeBands = sparkContext.broadcast(timeBands);
			tripStatsPerTimeBand = tripAnalyzer.getTripStatsPerTimeBand(bdcstTimeBands);
		} else {
			tripStatsPerTimeBand = tripAnalyzer.getTripStatsPerTimeBand(timeBands);
		}

		System.out.printf("No. of defective records: %d.\n", defectiveRecords.value().longValue());
		tripStatsPerTimeBand.forEach(this::printTripStatsPerTimeBand);

		sparkContext.close();
	}

	private void printTripStatsPerTimeBand(TimeBand timeBand, TripStats tripStats) {
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

	private JavaRDD<TaxiTrip> loadTripData(JavaSparkContext sparkContext, String taxiTripFile,
			LongAccumulator defectiveRecords) {
		TripDataLoader loader = new TextFileLoader(sparkContext, taxiTripFile, defectiveRecords);
		return loader.fetchRecords();
	}
}

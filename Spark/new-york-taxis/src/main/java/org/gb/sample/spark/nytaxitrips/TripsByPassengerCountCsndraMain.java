package org.gb.sample.spark.nytaxitrips;

import java.time.Month;
import java.util.List;
import java.util.Map;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class TripsByPassengerCountCsndraMain {

	public static void main(String[] args1) {
		JavaSparkContext sparkContext = connectSparkToCassandra();
		JavaRDD<TaxiTrip> tripData = loadTripData(sparkContext);
		TripAnalyzer tripAnalyzer = new RDDTripAnalyzer(tripData);
		final int tshldPsngrCnt = 8;
		List<TaxiTrip> tripsWithPsngrsAboveTshld = tripAnalyzer.getTripsWithPsngrsAboveTshld(tshldPsngrCnt);
		System.out.printf("No. of taxi trips with at least %d passengers: %d.\n", tshldPsngrCnt,
				tripsWithPsngrsAboveTshld.size());
		Map<Integer, Integer> tripCountPerPsngrCount = tripAnalyzer.getTripCountPerPsngrCount();
		tripCountPerPsngrCount.forEach((psngrCnt, tripCnt) -> System.out.printf("%d <--> %d\n", psngrCnt, tripCnt));
		Map<Month, Double> avgPsngrCountPerMonth = tripAnalyzer.getAvgPsngrCountPerMonth();
		avgPsngrCountPerMonth.forEach((month, avgPsngrCnt) -> System.out.printf("Average passenger count for %s --> %2.2f\n", month, avgPsngrCnt));
		sparkContext.close();
	}

	private static JavaRDD<TaxiTrip> loadTripData(JavaSparkContext sparkContext) {
		TripDataLoader loader = new CassandraTableLoader(sparkContext);
		return loader.fetchRecords();
	}

	private static JavaSparkContext connectSparkToCassandra() {
		SparkConf sparkConf = new SparkConf();
		sparkConf.set("spark.cassandra.connection.host", "localhost");
		sparkConf.set("spark.cassandra.connection.port", "9042");
		sparkConf.set("spark.app.name", "New York Yellow Taxis - selected trips");

		JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
		return sparkContext;
	}
}

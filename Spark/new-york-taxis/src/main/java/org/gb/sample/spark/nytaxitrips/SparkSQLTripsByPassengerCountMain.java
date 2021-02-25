package org.gb.sample.spark.nytaxitrips;

import java.time.Month;
import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSQLTripsByPassengerCountMain {

	public static void main(String[] args) {
		SparkSession session = SparkSession.builder()
				.appName("New York Yellow Taxis - selected trips - using Spark SQL").getOrCreate();
		Dataset<Row> tripData = session.read().csv(args[0]);
		TripAnalyzer tripAnalyzer = new SQLTripAnalyzer(tripData);
		final int tshldPsngrCnt = 8;
		List<TaxiTrip> tripsWithPsngrsAboveTshld = tripAnalyzer.getTripsWithPsngrsAboveTshld(tshldPsngrCnt);
		System.out.printf("No. of taxi trips with at least %d passengers: %d.\n", tshldPsngrCnt,
				tripsWithPsngrsAboveTshld.size());
		Map<Integer, Integer> tripCountPerPsngrCount = tripAnalyzer.getTripCountPerPsngrCount();
		tripCountPerPsngrCount.forEach((psngrCnt, tripCnt) -> System.out.printf("%d <--> %d\n", psngrCnt, tripCnt));
		Map<Month, Double> avgPsngrCountPerMonth = tripAnalyzer.getAvgPsngrCountPerMonth();
		avgPsngrCountPerMonth.forEach((month, avgPsngrCnt) -> System.out
				.printf("Average passenger count for %s --> %2.2f\n", month, avgPsngrCnt));
		//session.close();
	}

}

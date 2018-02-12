package org.gb.sample.spark.nytaxitrips;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class TripSelectionMain {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("New York Yellow Taxis - selected trips");
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		String yellowTaxiTripFile = args[0];
		JavaRDD<String> yellowTaxiTripLines = sparkContext.textFile(yellowTaxiTripFile);
		TripAnalyzer tripAnalyzer = new TripAnalyzer(yellowTaxiTripLines);
		int tshldPsngrCnt = 8;
		List<TaxiTrip> tripsWithPsngrsAboveTshld = tripAnalyzer.getTripsWithPsngrsAboveTshld(tshldPsngrCnt);
		System.out.printf("No. of taxi trips with at least %d passengers: %d.\n", tshldPsngrCnt,
				tripsWithPsngrsAboveTshld.size());
		sparkContext.close();
	}

}

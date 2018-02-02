package org.gb.sample.spark.nytaxitrips;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class TipPercentageMain {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("New York Yellow Taxi trips - average tip as percentage of fare");
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		String yellowTaxiTripFile = args[0];
		JavaRDD<String> yellowTaxiTripLines = sparkContext.textFile(yellowTaxiTripFile);
		TripAnalyzer tripAnalyzer = new TripAnalyzer(yellowTaxiTripLines);
		double avgTipAsPctgOfFare = tripAnalyzer.computeAvgTipAsPercentageOfFare();
		System.out.printf("On an average, tip paid is %2.2f percent of fare amount.\n", avgTipAsPctgOfFare);
		sparkContext.close();
	}

}

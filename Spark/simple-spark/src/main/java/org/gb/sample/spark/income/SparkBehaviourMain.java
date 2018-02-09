package org.gb.sample.spark.income;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkBehaviourMain {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Person Income & Profile - Spark behaviour");
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		String yellowTaxiTripFilesDir = args[0];
		JavaRDD<String> yellowTaxiTripLines = read_newAPIHadoopFile(sparkContext, yellowTaxiTripFilesDir);
		System.out.println("SparkBehaviourMain, no. of partitions: " + yellowTaxiTripLines.getNumPartitions());
		System.out.println("SparkBehaviourMain, yellowTaxiTripLines.count(): " + yellowTaxiTripLines.count());
		sparkContext.close();
	}

	private static JavaRDD<String> read_textFile(JavaSparkContext sparkContext, String inputLocation) {
		return sparkContext.textFile(inputLocation);
	}

	private static JavaRDD<String> read_hadoopFile(JavaSparkContext sparkContext, String inputLocation) {
		JavaRDD<Text> textLines = sparkContext
				.hadoopFile(inputLocation, HadoopOldAPICustomTextInputFormat.class, LongWritable.class, Text.class).values();
		return textLines.map(Text::toString);
	}

	private static JavaRDD<String> read_newAPIHadoopFile(JavaSparkContext sparkContext, String inputLocation) {
		JavaRDD<Text> textLines = sparkContext.newAPIHadoopFile(inputLocation, HadoopNewAPICustomTextInputFormat.class,
				LongWritable.class, Text.class, sparkContext.hadoopConfiguration()).values();
		return textLines.map(Text::toString);
	}
}

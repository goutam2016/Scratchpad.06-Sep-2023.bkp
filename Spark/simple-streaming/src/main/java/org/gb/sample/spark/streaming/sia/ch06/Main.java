package org.gb.sample.spark.streaming.sia.ch06;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class Main {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: Main <input-directory>");
			System.exit(1);
		}

		try {
			String inputDir = args[0];
			SparkConf sparkConf = new SparkConf().setAppName("Stock Orders Dashboard");
			JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(5));
			
			JavaDStream<String> orderLines = streamingContext.textFileStream(inputDir);
			orderLines.print();

			streamingContext.start();
			streamingContext.awaitTermination();
			streamingContext.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

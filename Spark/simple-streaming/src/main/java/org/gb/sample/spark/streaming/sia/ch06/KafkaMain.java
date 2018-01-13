package org.gb.sample.spark.streaming.sia.ch06;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import kafka.serializer.StringDecoder;

public class KafkaMain {

	public static void main(String[] args) {
		try {
			SparkConf sparkConf = new SparkConf().setAppName("Stock Orders Dashboard");
			JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(5));
			streamingContext.checkpoint("/tmp");

			Map<String, String> kafkaParams = new HashMap<>();
			kafkaParams.put("metadata.broker.list", "Goutams-MBP:9092");
			Set<String> topics = Collections.singleton("orders");

			JavaPairInputDStream<String, String> kafkaStream = KafkaUtils.createDirectStream(streamingContext,
					String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);
			kafkaStream.print(10);

			streamingContext.start();
			streamingContext.awaitTermination();
			streamingContext.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

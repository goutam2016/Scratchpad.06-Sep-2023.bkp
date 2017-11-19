package org.gb.sample.spark.streaming.sia.ch06;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;

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
			JavaPairDStream<Order.Direction, Integer> countPerDirection = orderLines.map(Main::convertToOrder)
					.mapToPair(order -> new Tuple2<>(order.getDirection(), 1)).reduceByKey(Integer::sum);
			countPerDirection.print();

			streamingContext.start();
			streamingContext.awaitTermination();
			streamingContext.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Order convertToOrder(String orderLine) {
		String[] ordLineTokens = orderLine.split(",");
		LocalDateTime ordDateTime = LocalDateTime.parse(ordLineTokens[0],
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Integer id = Integer.parseInt(ordLineTokens[1]);
		Integer clientId = Integer.parseInt(ordLineTokens[2]);
		String symbol = ordLineTokens[3];
		Integer quantity = Integer.parseInt(ordLineTokens[4]);
		BigDecimal price = new BigDecimal(ordLineTokens[5]);
		Order.Direction direction = ordLineTokens[6].equals("B") ? Order.Direction.BUY : Order.Direction.SELL;
		Order order = new Order();
		order.setOrdDateTime(ordDateTime);
		order.setId(id);
		order.setClientId(clientId);
		order.setSymbol(symbol);
		order.setQuantity(quantity);
		order.setPrice(price);
		order.setDirection(direction);
		return order;
	}

}

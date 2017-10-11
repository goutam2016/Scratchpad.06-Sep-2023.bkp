package org.gb.sample.spark.temperature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

public class Main {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Max-min temperature finder");
		JavaSparkContext sc = new JavaSparkContext(conf);
		String inputFile = args[0];
		JavaRDD<String> lines = sc.textFile(inputFile);
		PairFlatMapFunction<String, String, String> maxMinTempSplitter = (String line) -> {
			String[] parts = line.split(":");
			String year = parts[0];
			String[] temps = parts[1].split(",");
			String maxTemp = temps[0];
			String minTemp = temps[1];
			String yearlyMax = year.concat(",").concat(maxTemp);
			String yearlyMin = year.concat(",").concat(minTemp);
			List<Tuple2<String, String>> maxMinTempTuples = new ArrayList<>();
			maxMinTempTuples.add(new Tuple2<>("max-temp", yearlyMax));
			maxMinTempTuples.add(new Tuple2<>("min-temp", yearlyMin));
			return maxMinTempTuples.iterator();
		};

		PairFunction<Tuple2<String, Iterable<String>>, Integer, Integer> overallMaxMinTempMapper = (
				Tuple2<String, Iterable<String>> tuple) -> {
			String key = tuple._1();
			Iterable<String> values = tuple._2();
			if (key.equals("max-temp")) {
				Integer overallMaxTemp = null;
				Integer yearOfOvrlMaxTemp = null;
				for (String value : values) {
					String[] parts = value.split(",");
					Integer year = Integer.parseInt(parts[0]);
					Integer temperature = Integer.parseInt(parts[1]);

					if (overallMaxTemp == null) {
						overallMaxTemp = temperature;
						yearOfOvrlMaxTemp = year;
					} else if (temperature.intValue() > overallMaxTemp.intValue()) {
						overallMaxTemp = temperature;
						yearOfOvrlMaxTemp = year;
					}
				}
				return new Tuple2<Integer, Integer>(overallMaxTemp, yearOfOvrlMaxTemp);
			} else if (key.equals("min-temp")) {
				Integer overallMinTemp = null;
				Integer yearOfOvrlMinTemp = null;
				for (String value : values) {
					String[] parts = value.split(",");
					Integer year = Integer.parseInt(parts[0]);
					Integer temperature = Integer.parseInt(parts[1]);

					if (overallMinTemp == null) {
						overallMinTemp = temperature;
						yearOfOvrlMinTemp = year;
					} else if (temperature.intValue() < overallMinTemp.intValue()) {
						overallMinTemp = temperature;
						yearOfOvrlMinTemp = year;
					}
				}
				return new Tuple2<Integer, Integer>(overallMinTemp, yearOfOvrlMinTemp);
			} else {
				throw new IllegalAccessException("Invalid key found: " + key);
			}
		};

		JavaPairRDD<Integer, Integer> overallMaxMinTemp = lines.flatMapToPair(maxMinTempSplitter).groupByKey()
				.mapToPair(overallMaxMinTempMapper);
		Map<Integer, Integer> overallMaxMinTempMap = overallMaxMinTemp.collectAsMap();
		BiConsumer<Integer, Integer> overallMaxMinTempPrinter = (Integer temp, Integer year) -> System.out
				.println(temp + ":" + year);
		System.out.println("Overall max-min temperature.");
		System.out.println("-----------------------------------------");
		overallMaxMinTempMap.forEach(overallMaxMinTempPrinter);
		System.out.println("-----------------------------------------");
		
		sc.close();
	}

}

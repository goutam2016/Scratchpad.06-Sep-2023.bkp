package org.gb.sample.spark.wordcount;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class Main {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local").setAppName("Word counter");
		JavaSparkContext sc = new JavaSparkContext(conf);
		String inputFile = args[0];
		JavaRDD<String> lines = sc.textFile(inputFile);
		FlatMapFunction<String, String> lineSplitter = (String line) -> Arrays.asList(line.split(" ")).iterator();
		PairFunction<String, String, Integer> wordPicker = (String word) -> new Tuple2<String, Integer>(word, 1);
		Function2<Integer, Integer, Integer> wordCounter = (Integer currentCount, Integer nextCount) -> currentCount
				+ nextCount;
		JavaPairRDD<String, Integer> countPerWord = lines.flatMap(lineSplitter).mapToPair(wordPicker)
				.reduceByKey(wordCounter);
		Map<String, Integer> countPerWordMap = countPerWord.collectAsMap();
		BiConsumer<String, Integer> wordCountPrinter = (String word, Integer count) -> System.out
				.println(word + ":" + count);
		countPerWordMap.forEach(wordCountPrinter);
		sc.close();
	}

}

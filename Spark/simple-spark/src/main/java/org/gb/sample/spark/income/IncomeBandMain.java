package org.gb.sample.spark.income;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class IncomeBandMain {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Person count per income band");
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		String nameVsIncomeFile = args[0];
		List<Integer> incomeSlabs = Stream.of(args[1].split(",")).map(Integer::parseInt).collect(Collectors.toList());
		JavaRDD<String> nameVsIncomeLines = sparkContext.textFile(nameVsIncomeFile);
		Map<Band, Long> countPerIncomeBand = new IncomeBandMapper().getCountPerIncomeBand(nameVsIncomeLines, incomeSlabs);
		countPerIncomeBand.forEach((band, count) -> System.out.println(band + " : " + count));
		sparkContext.close();
	}

}

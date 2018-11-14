package org.gb.sample.spark.income;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import scala.Tuple2;

public class SparkBehaviourMain {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Person Income & Profile - Spark behaviour");
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		String personProfileFile = args[0];
		JavaRDD<String> personProfileLines = read_newAPIHadoopFile(sparkContext, personProfileFile);
		System.out.println(
				"SparkBehaviourMain, personProfileLines no. of partitions: " + personProfileLines.getNumPartitions());
		Converter converter = Converter.getInstance();
		citiesWithMostPeople_Using_reduceByKey(personProfileLines, converter, 10);
		//citiesWithMostPeople_Using_groupByKey(personProfileLines, converter, 10);
		sparkContext.close();
	}

	private static JavaRDD<String> read_textFile(JavaSparkContext sparkContext, String inputLocation) {
		return sparkContext.textFile(inputLocation);
	}

	private static JavaRDD<String> read_hadoopFile(JavaSparkContext sparkContext, String inputLocation) {
		JavaRDD<Text> textLines = sparkContext
				.hadoopFile(inputLocation, HadoopOldAPICustomTextInputFormat.class, LongWritable.class, Text.class)
				.values();
		return textLines.map(Text::toString);
	}

	private static JavaRDD<String> read_newAPIHadoopFile(JavaSparkContext sparkContext, String inputLocation) {
		JavaRDD<Text> textLines = sparkContext.newAPIHadoopFile(inputLocation, HadoopNewAPICustomTextInputFormat.class,
				LongWritable.class, Text.class, sparkContext.hadoopConfiguration()).values();
		return textLines.map(Text::toString);
	}

	private static class CountComparator implements Comparator<Tuple2<String, Integer>>, Serializable {

		@Override
		public int compare(Tuple2<String, Integer> cityVsCount1, Tuple2<String, Integer> cityVsCount2) {
			return cityVsCount1._2().intValue() - cityVsCount2._2().intValue();
		}

	}
	
	/*
	 * reduceByKey() causes lesser data transfer during shuffle because along with each key, the aggregated count per key are transferred. 
	 * The amount of shuffle can be seen on the history server UI, under the stages tab.
	 * The no. of partitions created after distributed reduction operations (groupByKey, reduceByKey, aggregateByKey etc.) can be explicitly
	 * specified through optional parameters to these methods, otherwise Spark uses the value of the configuration property spark.default.parallelism.
	 */
	private static void citiesWithMostPeople_Using_reduceByKey(JavaRDD<String> personProfileLines, Converter converter,
			int topNum) {
		JavaPairRDD<String, Integer> cityVsCountPairs = personProfileLines.map(converter::convertToPersProfile)
				.map(PersonProfile::getCity).mapToPair(city -> new Tuple2<>(city, 1))
				.reduceByKey((accCount, unit) -> accCount + unit);
		System.out.println(
				"SparkBehaviourMain, cityVsCountPairs no. of partitions: " + cityVsCountPairs.getNumPartitions());
		Comparator<Tuple2<String, Integer>> countComparator = new CountComparator();
		List<Tuple2<String, Integer>> cityVsCountTuples = cityVsCountPairs.top(topNum, countComparator);
		Map<String, Integer> countPerCity = cityVsCountTuples.stream()
				.collect(Collectors.toMap(Tuple2::_1, Tuple2::_2, (cnt1, cnt2) -> cnt1, LinkedHashMap::new));
		System.out.println("SparkBehaviourMain, top cities by people.");
		countPerCity.entrySet()
				.forEach(cityVsCount -> System.out.println(cityVsCount.getKey() + " --> " + cityVsCount.getValue()));
	}

	/*
	 * groupByKey() causes larger data transfer during shuffle because along with each key, all individual values associated to a key are transferred as well.
	 */
	private static void citiesWithMostPeople_Using_groupByKey(JavaRDD<String> personProfileLines, Converter converter,
			int topNum) {
		JavaPairRDD<String, Iterable<Integer>> cityVsUnits = personProfileLines.map(converter::convertToPersProfile)
				.map(PersonProfile::getCity).mapToPair(city -> new Tuple2<>(city, 1))
				.groupByKey();
		Function<Iterable<Integer>, Integer> unitsAdder = (Iterable<Integer> units) -> StreamSupport
				.stream(units.spliterator(), false).mapToInt(Integer::intValue).sum();
		JavaPairRDD<String, Integer> cityVsCountPairs = cityVsUnits.mapValues(unitsAdder);
		System.out.println(
				"SparkBehaviourMain, cityVsCountPairs no. of partitions: " + cityVsCountPairs.getNumPartitions());
		Comparator<Tuple2<String, Integer>> countComparator = new CountComparator();
		List<Tuple2<String, Integer>> cityVsCountTuples = cityVsCountPairs.top(topNum, countComparator);
		Map<String, Integer> countPerCity = cityVsCountTuples.stream()
				.collect(Collectors.toMap(Tuple2::_1, Tuple2::_2, (cnt1, cnt2) -> cnt1, LinkedHashMap::new));
		System.out.println("SparkBehaviourMain, top cities by people.");
		countPerCity.entrySet()
				.forEach(cityVsCount -> System.out.println(cityVsCount.getKey() + " --> " + cityVsCount.getValue()));
	}
}

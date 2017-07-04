package org.gb.sample.spark.animals;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

public class Main {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local").setAppName("Animals");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		String mammalsFile = args[0];
		String primatesFile = args[1];
		
		JavaRDD<String> mammals = sc.textFile(mammalsFile);
		JavaRDD<String> primates = sc.textFile(primatesFile);
		
		JavaRDD<String> unionResult = mammals.union(primates);
		JavaRDD<String> intersectionResult = mammals.intersection(primates);
		JavaRDD<String> subtractResult = mammals.subtract(primates);
		
		VoidFunction<String> elementPrinter = (String element) -> System.out.println(element);
		
		System.out.println("Union result:");
		System.out.println("---------------------------------------------------------------------\n");
		unionResult.foreach(elementPrinter);
		System.out.println();

		System.out.println("Intersection result:");
		System.out.println("---------------------------------------------------------------------\n");
		intersectionResult.foreach(elementPrinter);
		System.out.println();

		System.out.println("Subtract result:");
		System.out.println("---------------------------------------------------------------------\n");
		subtractResult.foreach(elementPrinter);
		System.out.println();

		sc.close();
	}

}

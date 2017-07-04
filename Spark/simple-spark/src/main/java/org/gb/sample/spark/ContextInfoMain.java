package org.gb.sample.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class ContextInfoMain {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local").setAppName("Spark Context Information");
		JavaSparkContext sc = new JavaSparkContext(conf);
		String sparkHome = sc.getSparkHome().get();
		String appName = sc.appName();
		Boolean local = sc.isLocal();
		String sparkVersion = sc.version();
		String user = sc.sparkUser();
		String appId = sc.getConf().getAppId();
		Tuple2<String, String>[] parameterPairs = sc.getConf().getAll();

		System.out.println("Spark Home: " + sparkHome);
		System.out.println("App Name: " + appName);
		System.out.println("Is local? " + local);
		System.out.println("Spark Version: " + sparkVersion);
		System.out.println("User: " + user);
		System.out.println("App Id: " + appId);

		for (Tuple2<String, String> parameterPair : parameterPairs) {
			String paramName = parameterPair._1();
			String paramValue = parameterPair._2();
			System.out.println("Param Name: " + paramName + ", Param Value: " + paramValue);
		}

		sc.close();
	}

}

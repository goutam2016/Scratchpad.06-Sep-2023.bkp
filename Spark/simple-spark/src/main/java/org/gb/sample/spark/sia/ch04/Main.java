package org.gb.sample.spark.sia.ch04;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class Main {
	
	public static void main(String[] args) {
		try {
			String txnFile = args[0];
			String productListFile = args[1];
			String outputLocation = args[2];
			
			SparkConf conf = new SparkConf().setAppName("Spark-in-action chapter 4");
			String master = conf.get("spark.master");
			
			if(master.startsWith("local")) {
				cleanOutputLocation(outputLocation);
			} else if (master.startsWith("spark")) {
				cleanOutputLocation(outputLocation);
			}
			
			JavaSparkContext sparkContext = new JavaSparkContext(conf);
			JavaRDD<String> txnLines = sparkContext.textFile(txnFile);
			JavaRDD<String> productLines = sparkContext.textFile(productListFile);
			RewardCalculator rewardCalculator = new RewardCalculator(txnLines, productLines);
			JavaRDD<Transaction> rewards = sparkContext.parallelize(rewardCalculator.calculateRewards());
			rewards.saveAsTextFile(outputLocation);
			sparkContext.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void cleanOutputLocation(String outputLocation) throws IOException {
		Path path = FileSystems.getDefault().getPath(outputLocation);
		
		if(Files.exists(path)) {
			FileVisitor<Path> outputCleaner = new OutputCleaner();
			Files.walkFileTree(path, outputCleaner);
		}
	}
}

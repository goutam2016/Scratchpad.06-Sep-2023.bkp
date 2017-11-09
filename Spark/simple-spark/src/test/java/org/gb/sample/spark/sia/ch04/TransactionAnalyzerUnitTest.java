package org.gb.sample.spark.sia.ch04;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import scala.Tuple2;

public class TransactionAnalyzerUnitTest {

	private static JavaSparkContext sparkContext;
	private TransactionAnalyzer txnAnalyzer;
	
	@BeforeClass
	public static void setupForAll() {
		SparkConf conf = new SparkConf().setMaster("local").setAppName("Spark-in-action chapter 4");
		sparkContext = new JavaSparkContext(conf);
	}

	@AfterClass
	public static void teardownForAll() {
		sparkContext.close();
	}

	@Test
	public void getCustIdWithMaxTxns() {
		// Prepare test data
		final String txnFile = "data/sia/ch04/transactions.txt";
		final JavaRDD<String> txnLines = sparkContext.textFile(txnFile);
		
		// Setup expectations
		final int custIdWithMaxTxns = 53;
		final int maxTxnCount = 19;
		
		// Invoke test target
		txnAnalyzer = new TransactionAnalyzer(txnLines);
		Tuple2<Integer, Integer> maxTxnsTuple = txnAnalyzer.getCustIdWithMaxTxns();
		
		// Verify results
		Assert.assertEquals(custIdWithMaxTxns, maxTxnsTuple._1().intValue());
		Assert.assertEquals(maxTxnCount, maxTxnsTuple._2().intValue());
	}

}

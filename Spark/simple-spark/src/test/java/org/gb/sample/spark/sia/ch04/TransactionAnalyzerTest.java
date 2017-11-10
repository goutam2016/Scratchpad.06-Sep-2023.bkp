package org.gb.sample.spark.sia.ch04;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import scala.Tuple2;

public class TransactionAnalyzerTest {

	private static final String TXN_FILE = "data/sia/ch04/transactions.txt";
	private static final String PRODUCT_LIST_FILE = "data/sia/ch04/products.txt";
	private static JavaSparkContext sparkContext;
	private static TransactionAnalyzer txnAnalyzer;

	@BeforeClass
	public static void setupForAll() {
		SparkConf conf = new SparkConf().setMaster("local").setAppName("Spark-in-action chapter 4");
		sparkContext = new JavaSparkContext(conf);
		JavaRDD<String> txnLines = sparkContext.textFile(TXN_FILE);
		JavaRDD<String> productLines = sparkContext.textFile(PRODUCT_LIST_FILE);
		txnAnalyzer = new TransactionAnalyzer(txnLines, productLines);
	}

	@AfterClass
	public static void teardownForAll() {
		sparkContext.close();
	}

	@Test
	public void getCustIdWithMaxTxns() {
		// Prepare test data

		// Setup expectations
		final int custIdWithMaxTxns = 53;
		final int maxTxnCount = 19;

		// Invoke test target
		Tuple2<Integer, Integer> maxTxnsTuple = txnAnalyzer.getCustIdWithMaxTxns();

		// Verify results
		Assert.assertEquals(custIdWithMaxTxns, maxTxnsTuple._1().intValue());
		Assert.assertEquals(maxTxnCount, maxTxnsTuple._2().intValue());
	}

	@Test
	public void getTxnsWithItemsAboveThreshold_BarbiePlayset() {
		// Prepare test data
		final String productName = "Barbie Shopping Mall Playset";
		final int thresholdQty = 5;

		// Setup expectations
		final int custId1 = 17;
		final int qty1 = 6;
		final int custId2 = 93;
		final int qty2 = 7;
		final int custId3 = 68;
		final int qty3 = 9;
		final int custId4 = 59;
		final int qty4 = 5;
		final int custId5 = 42;
		final int qty5 = 10;	
		final int custId6 = 22;
		final int qty6 = 9;	
		final int custId7 = 32;
		final int qty7 = 8;	
		final int custId8 = 75;
		final int qty8 = 10;	

		// Invoke test target
		List<Transaction> txnsWithItemsAboveThreshold = txnAnalyzer.getTxnsWithItemsAboveThreshold(productName,
				thresholdQty);

		// Verify results
		Map<Integer, Integer> qtyPerCustId = txnsWithItemsAboveThreshold.stream()
				.collect(Collectors.toMap(Transaction::getCustomerId, Transaction::getQuantity));
		Assert.assertEquals(qty1, qtyPerCustId.get(custId1).intValue());
		Assert.assertEquals(qty2, qtyPerCustId.get(custId2).intValue());
		Assert.assertEquals(qty3, qtyPerCustId.get(custId3).intValue());
		Assert.assertEquals(qty4, qtyPerCustId.get(custId4).intValue());
		Assert.assertEquals(qty5, qtyPerCustId.get(custId5).intValue());
		Assert.assertEquals(qty6, qtyPerCustId.get(custId6).intValue());
		Assert.assertEquals(qty7, qtyPerCustId.get(custId7).intValue());
		Assert.assertEquals(qty8, qtyPerCustId.get(custId8).intValue());
	}
	
	@Test
	public void getTxnsWithItemsAboveThreshold_Dictionary() {
		// Prepare test data
		final String productName = "Dictionary";
		final int thresholdQty = 5;

		// Setup expectations
		final int custId1 = 16;
		final int qty1 = 9;
		final int custId2 = 82;
		final int qty2 = 6;
		final int custId3 = 85;
		final int qty3 = 7;
		final int custId4 = 10;
		final int qty4 = 10;
		final int custId5 = 77;
		final int qty5 = 5;
		final int custId6 = 47;
		final int qty6 = 10;	

		// Invoke test target
		List<Transaction> txnsWithItemsAboveThreshold = txnAnalyzer.getTxnsWithItemsAboveThreshold(productName,
				thresholdQty);

		// Verify results
		Map<Integer, Integer> qtyPerCustId = txnsWithItemsAboveThreshold.stream()
				.collect(Collectors.toMap(Transaction::getCustomerId, Transaction::getQuantity));
		Assert.assertEquals(qty1, qtyPerCustId.get(custId1).intValue());
		Assert.assertEquals(qty2, qtyPerCustId.get(custId2).intValue());
		Assert.assertEquals(qty3, qtyPerCustId.get(custId3).intValue());
		Assert.assertEquals(qty4, qtyPerCustId.get(custId4).intValue());
		Assert.assertEquals(qty5, qtyPerCustId.get(custId5).intValue());
		Assert.assertEquals(qty6, qtyPerCustId.get(custId6).intValue());
	}

}

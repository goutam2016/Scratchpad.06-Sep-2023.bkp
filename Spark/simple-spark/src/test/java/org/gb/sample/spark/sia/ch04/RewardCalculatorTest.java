package org.gb.sample.spark.sia.ch04;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RewardCalculatorTest {

	private static final String TXN_FILE = "data/sia/ch04/transactions.txt";
	private static final String PRODUCT_LIST_FILE = "data/sia/ch04/products.txt";
	private static JavaSparkContext sparkContext;
	private static RewardCalculator rewardCalculator;

	@BeforeClass
	public static void setupForAll() {
		SparkConf conf = new SparkConf().setMaster("local").setAppName("Spark-in-action chapter 4");
		sparkContext = new JavaSparkContext(conf);
		JavaRDD<String> txnLines = sparkContext.textFile(TXN_FILE);
		JavaRDD<String> productLines = sparkContext.textFile(PRODUCT_LIST_FILE);
		rewardCalculator = new RewardCalculator(txnLines, productLines);
	}

	@AfterClass
	public static void teardownForAll() {
		sparkContext.close();
	}

	@Test
	public void calculateRewards() {
		// Setup expectations
		final int exptdDiscountedTxnCount = 9;
		final int exptdComplTxnCount = 8;

		// Invoke test target
		List<Transaction> rewards = rewardCalculator.calculateRewards();

		// Verify results
		int retndDiscountedTxnCount = (int) rewards.stream().filter(txn -> txn.getAggrPrice().signum() == -1).count();
		int retndComplTxnCount = (int) rewards.stream().filter(txn -> txn.getAggrPrice().signum() == 0).count();
		Assert.assertEquals(exptdDiscountedTxnCount, retndDiscountedTxnCount);
		Assert.assertEquals(exptdComplTxnCount, retndComplTxnCount);
	}
}

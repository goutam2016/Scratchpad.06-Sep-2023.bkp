package org.gb.sample.spark.income;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class IncomeBandMapperUnitTest {

	private static JavaSparkContext sparkContext;
	private IncomeBandMapper incomeBandMapper;

	@BeforeClass
	public static void setupForAll() {
		SparkConf conf = new SparkConf().setMaster("local").setAppName("Person count per income band");
		sparkContext = new JavaSparkContext(conf);
	}

	@AfterClass
	public static void teardownForAll() {
		sparkContext.close();
	}

	@Test
	public void getCountPerIncomeBand() {
		// Prepare test data
		final String nameVsIncomeFile = "data/income/name-vs-income_500.txt";
		final Integer incomeSlab1 = 0;
		final Integer incomeSlab2 = 50000;
		final Integer incomeSlab3 = 75000;
		final List<Integer> incomeSlabs = Arrays.asList(incomeSlab1, incomeSlab2, incomeSlab3);
		final JavaRDD<String> nameVsIncomeLines = sparkContext.textFile(nameVsIncomeFile);
		
		// Setup expectations
		final long count1 = 233;
		final Band band1 = new Band(incomeSlab1, incomeSlab2);
		final long count2 = 216;
		final Band band2 = new Band(incomeSlab2, incomeSlab3);
		final long count3 = 51;
		final Band band3 = new Band(incomeSlab3, null);

		// Invoke test target
		incomeBandMapper = new IncomeBandMapper(nameVsIncomeLines, incomeSlabs);
		Map<Band, Long> countPerIncomeBand = incomeBandMapper.getCountPerIncomeBand();		
		
		// Verify results
		Assert.assertEquals(countPerIncomeBand.get(band1).longValue(), count1);
		Assert.assertEquals(countPerIncomeBand.get(band2).longValue(), count2);
		Assert.assertEquals(countPerIncomeBand.get(band3).longValue(), count3);
	}
	
}

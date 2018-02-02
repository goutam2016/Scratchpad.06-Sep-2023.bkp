package org.gb.sample.spark.nytaxitrips;

import java.time.LocalTime;
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

public class TripAnalyzerTest {

	private static final String TAXI_TRIP_FILE = "data/nytaxitrips/yellow_tripdata_1000.csv";
	//private static final String TAXI_TRIP_FILE = "data/nytaxitrips/bkp.txt";
	private static JavaSparkContext sparkContext;
	private static TripAnalyzer tripAnalyzer;

	@BeforeClass
	public static void setupForAll() {
		SparkConf conf = new SparkConf().setMaster("local").setAppName("New York Yellow Taxi trips");
		sparkContext = new JavaSparkContext(conf);
		JavaRDD<String> taxiTripLines = sparkContext.textFile(TAXI_TRIP_FILE);
		tripAnalyzer = new TripAnalyzer(taxiTripLines);
	}

	@AfterClass
	public static void teardownForAll() {
		sparkContext.close();
	}

	@Test
	public void getTripsWithPsngrsAboveTshld() {
		// Prepare test data
		final int tshldPsngrCnt = 6;

		// Setup expectations
		final int exptdTripCount = 39;

		// Invoke test target
		List<TaxiTrip> tripsWithPsngrsAboveTshld = tripAnalyzer.getTripsWithPsngrsAboveTshld(tshldPsngrCnt);

		// Verify results
		Assert.assertEquals(exptdTripCount, tripsWithPsngrsAboveTshld.size());
	}

	@Test
	public void getTripsBetweenPickupDropoffTimes() {
		// Prepare test data
		final LocalTime earliestPickupTime = LocalTime.MIDNIGHT;
		final LocalTime latestDropoffTime = LocalTime.of(6, 0);
		
		// Setup expectations
		final int exptdTripCount = 169;

		// Invoke test target
		List<TaxiTrip> tripsBtwnPickupDropoffTimes = tripAnalyzer.getTripsBetweenPickupDropoffTimes(earliestPickupTime,
				latestDropoffTime);

		// Verify results
		Assert.assertEquals(exptdTripCount, tripsBtwnPickupDropoffTimes.size());
	}
	
	@Test
	public void computeAvgTipAsPercentageOfFare_bkp() {
		double tipAsPercentageOfFare = tripAnalyzer.computeAvgTipAsPercentageOfFare_bkp();
		System.out.println("tipAsPercentageOfFare: " + tipAsPercentageOfFare);
		/*TaxiTrip trip1 = new TaxiTrip();
		TaxiTrip trip2 = new TaxiTrip();
		TaxiTrip trip3 = new TaxiTrip();
		TaxiTrip trip4 = new TaxiTrip();
		trip1.setFareAmount(BigDecimal.valueOf(100));
		trip1.setTipAmount(BigDecimal.ZERO);
		trip2.setFareAmount(BigDecimal.valueOf(100));
		trip2.setTipAmount(BigDecimal.ZERO);
		trip3.setFareAmount(BigDecimal.valueOf(100));
		trip3.setTipAmount(BigDecimal.ZERO);
		trip4.setFareAmount(BigDecimal.valueOf(100));
		trip4.setTipAmount(BigDecimal.TEN);
		JavaRDD<TaxiTrip> taxiTrips = sparkContext.parallelize(Arrays.asList(trip1, trip2, trip3, trip4));
		Tuple2<Integer, Double> initialTripCountVsTipRatio = new Tuple2<Integer, Double>(0, 0.0);
		Function2<Tuple2<Integer, Double>, Double, Tuple2<Integer, Double>> tipRatioAdder = (tripCountVsTipRatio,
				nextTipRatio) -> new Tuple2<Integer, Double>(tripCountVsTipRatio._1().intValue() + 1,
						tripCountVsTipRatio._2().doubleValue() + nextTipRatio.doubleValue());
		Function2<Tuple2<Integer, Double>, Tuple2<Integer, Double>, Tuple2<Integer, Double>> tripCountVsTipRatioCombiner = (
				tripCountVsTipRatio1, tripCountVsTipRatio2) -> new Tuple2<Integer, Double>(
						tripCountVsTipRatio1._1().intValue() + tripCountVsTipRatio2._1().intValue(),
						tripCountVsTipRatio1._2().doubleValue() + tripCountVsTipRatio2._2().doubleValue());
		Tuple2<Integer, Double> combinedTripCountVsTipRatio = taxiTrips.map(trip -> trip.getTipAmount().divide(trip.getFareAmount()).doubleValue())
				.aggregate(initialTripCountVsTipRatio, tipRatioAdder, tripCountVsTipRatioCombiner);
		double avgTipAsPercentageOfFare = (combinedTripCountVsTipRatio._2().doubleValue() / combinedTripCountVsTipRatio._1().intValue()) * 100;
		System.out.println(combinedTripCountVsTipRatio._1() + " <--> " + combinedTripCountVsTipRatio._2() + " <--> " + avgTipAsPercentageOfFare);*/
	}
	
	@Test
	public void computeAvgTipAsPercentageOfFare() {
		// Setup expectations
		final double exptdAvgTipPercentage = 13.51;
		
		// Invoke test target
		double retndAvgTipPercentage = tripAnalyzer.computeAvgTipAsPercentageOfFare();
		
		// Verify results
		Assert.assertEquals(exptdAvgTipPercentage, retndAvgTipPercentage, 0.0);
	}
	
	@Test
	public void getTripStatsPerTimeBand() {
		TimeBand earlyMorning = new TimeBand(LocalTime.MIDNIGHT, LocalTime.of(6, 0));
		TimeBand morning = new TimeBand(LocalTime.of(6, 0), LocalTime.NOON);
		TimeBand afternoon = new TimeBand(LocalTime.NOON, LocalTime.of(18, 0));
		TimeBand night = new TimeBand(LocalTime.of(18, 0));

		List<TimeBand> timeBands = Arrays.asList(earlyMorning, morning, afternoon, night);
		
		Map<TimeBand, TripStats> tripStatsPerTimeBand = tripAnalyzer.getTripStatsPerTimeBand(timeBands);
		
		tripStatsPerTimeBand.forEach((timeBand, tripStats) -> System.out.println(timeBand + " --> " + tripStats));
		//tripStatsPerTimeBand.forEach((timeBand, tripStats) -> System.out.println(timeBand));
	}
}

package org.gb.sample.spark.nytaxitrips;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function2;

import scala.Tuple2;

public class TripAnalyzer implements Serializable {

	private static final long serialVersionUID = -3531081469539195154L;

	private JavaRDD<TaxiTrip> taxiTrips;

	TripAnalyzer(JavaRDD<TaxiTrip> taxiTrips) {
		this.taxiTrips = taxiTrips;
	}

	List<TaxiTrip> getTripsWithPsngrsAboveTshld(int tshldPsngrCnt) {
		JavaRDD<TaxiTrip> tripsWithPsngrsAboveTshld = taxiTrips.filter(Objects::nonNull)
				.filter(trip -> trip.getPassengerCount().intValue() >= tshldPsngrCnt);
		return tripsWithPsngrsAboveTshld.collect();
	}

	Map<Integer, Integer> getTripCountPerPsngrCount() {
		JavaPairRDD<Integer, Integer> tripCountPerPsngrCount = taxiTrips.filter(Objects::nonNull)
				.mapToPair(trip -> new Tuple2<>(trip.getPassengerCount(), 1)).reduceByKey(Integer::sum);
		return tripCountPerPsngrCount.collectAsMap();
	}

	Map<Month, Double> getAvgPsngrCountPerMonth() {
		Tuple2<Integer, Integer> initialTripCntVsPsngrCnt = new Tuple2<Integer, Integer>(0, 0);
		Function2<Tuple2<Integer, Integer>, Integer, Tuple2<Integer, Integer>> tripCntVsPsngrCntAdder = (
				tripCntVsPsngrCnt, nextPsngrCnt) -> new Tuple2<>(tripCntVsPsngrCnt._1().intValue() + 1,
						tripCntVsPsngrCnt._2().intValue() + nextPsngrCnt.intValue());
		Function2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> tripCntVsPsngrCntCombiner = (
				tripCntVsPsngrCnt1, tripCntVsPsngrCnt2) -> new Tuple2<>(
						tripCntVsPsngrCnt1._1().intValue() + tripCntVsPsngrCnt2._1().intValue(),
						tripCntVsPsngrCnt1._2().intValue() + tripCntVsPsngrCnt2._2().intValue());
		JavaPairRDD<Month, Double> avgPsngrCountPerMonth = taxiTrips.filter(Objects::nonNull)
				.mapToPair(trip -> new Tuple2<>(trip.getPickupDateTime().getMonth(), trip.getPassengerCount()))
				.aggregateByKey(initialTripCntVsPsngrCnt, tripCntVsPsngrCntAdder, tripCntVsPsngrCntCombiner)
				.mapValues(monthlyTripCntVsPsngrCnt -> monthlyTripCntVsPsngrCnt._2().doubleValue()
						/ monthlyTripCntVsPsngrCnt._1().intValue());

		return avgPsngrCountPerMonth.collectAsMap();
	}

	List<TaxiTrip> getTripsBetweenPickupDropoffTimes(LocalTime earliestPickupTime, LocalTime latestDropoffTime) {
		JavaRDD<TaxiTrip> tripsBtwnPickupDropoffTimes = taxiTrips.filter(Objects::nonNull)
				.filter(trip -> trip.getPickupDateTime().toLocalTime().isAfter(earliestPickupTime))
				.filter(trip -> trip.getDropoffDateTime().toLocalTime().isBefore(latestDropoffTime));
		return tripsBtwnPickupDropoffTimes.collect();
	}

	double computeAvgTipAsPercentageOfFare() {
		Tuple2<Integer, BigDecimal> initialTripCountVsTipRatio = new Tuple2<>(0, BigDecimal.ZERO);
		Function2<Tuple2<Integer, BigDecimal>, BigDecimal, Tuple2<Integer, BigDecimal>> tipRatioAdder = (
				tripCountVsTipRatio, nextTipRatio) -> new Tuple2<>(tripCountVsTipRatio._1().intValue() + 1,
						tripCountVsTipRatio._2().add(nextTipRatio));
		Function2<Tuple2<Integer, BigDecimal>, Tuple2<Integer, BigDecimal>, Tuple2<Integer, BigDecimal>> tripCountVsTipRatioCombiner = (
				tripCountVsTipRatio1, tripCountVsTipRatio2) -> new Tuple2<>(
						tripCountVsTipRatio1._1().intValue() + tripCountVsTipRatio2._1().intValue(),
						tripCountVsTipRatio1._2().add(tripCountVsTipRatio2._2()));
		Tuple2<Integer, BigDecimal> combinedTripCountVsTipRatio = taxiTrips.filter(Objects::nonNull)
				.filter(trip -> trip.getFareAmount().signum() == 1)
				.map(trip -> trip.getTipAmount().divide(trip.getFareAmount(), 4, RoundingMode.HALF_UP))
				.aggregate(initialTripCountVsTipRatio, tipRatioAdder, tripCountVsTipRatioCombiner);

		BigDecimal avgTipRatio = combinedTripCountVsTipRatio._2()
				.divide(BigDecimal.valueOf(combinedTripCountVsTipRatio._1().intValue()), 4, RoundingMode.HALF_UP);
		return avgTipRatio.multiply(BigDecimal.valueOf(100)).doubleValue();
	}

	/**
	 * Define 4 time bands. Start of time band inclusive, end of time band exclusive. 06:00(inclusive) -
	 * 12:00(exclusive) 12:00 - 18:00 18:00 - 00:00 00:00 - 06:00
	 * 
	 * Segregate taxi trips between these 4 time bands according to their pickup times. For each band, find the
	 * following statistics: Total no. of trips Total passenger count and average passenger count per trip. Total
	 * distance covered and average distance per trip. Total fare amount and average fare amount per trip.
	 */
	Map<TimeBand, TripStats> getTripStatsPerTimeBand(List<TimeBand> timeBands) {
		String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		String threadName = Thread.currentThread().getName();
		long threadId = Thread.currentThread().getId();
		System.out.printf(
				"TripAnalyzer.getTripStatsPerTimeBand() called from jvm: %s, thread-name: %s, thread-id: %d.\n",
				jvmName, threadName, threadId);
		JavaPairRDD<TimeBand, TripStats> tripStatsPerTimeBand = taxiTrips
				.mapToPair(trip -> new Tuple2<>(mapToTimeBand(trip, timeBands), trip))
				.combineByKey(this::createTripStats, this::mergeTripStats, this::combineTripStats)
				.mapValues(this::setAvgStats);
		return tripStatsPerTimeBand.collectAsMap();
	}

	private TripStats setAvgStats(TripStats tripStats) {
		Double avgPassengersPerTrip = tripStats.getTotalPassengerCount().doubleValue()
				/ tripStats.getTotalTripCount().intValue();
		Double avgDistancePerTrip = tripStats.getTotalDistanceCovered().doubleValue()
				/ tripStats.getTotalTripCount().intValue();
		BigDecimal avgFarePerTrip = tripStats.getTotalFareAmount()
				.divide(BigDecimal.valueOf(tripStats.getTotalTripCount().intValue()), 4, RoundingMode.HALF_UP);
		tripStats.setAvgPassengersPerTrip(avgPassengersPerTrip);
		tripStats.setAvgDistancePerTrip(avgDistancePerTrip);
		tripStats.setAvgFarePerTrip(avgFarePerTrip);
		return tripStats;
	}

	private TripStats createTripStats(TaxiTrip trip) {
		String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		String threadName = Thread.currentThread().getName();
		long threadId = Thread.currentThread().getId();
		System.out.printf("TripAnalyzer.createTripStats() called from jvm: %s, thread-name: %s, thread-id: %d.\n",
				jvmName, threadName, threadId);
		TripStats stats = new TripStats();
		stats.setTotalTripCount(1);
		stats.setTotalPassengerCount(trip.getPassengerCount());
		stats.setTotalDistanceCovered(trip.getTripDistance());
		stats.setTotalFareAmount(trip.getFareAmount());
		return stats;
	}

	private TripStats mergeTripStats(TripStats stats, TaxiTrip nextTrip) {
		String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		String threadName = Thread.currentThread().getName();
		long threadId = Thread.currentThread().getId();
		System.out.printf("TripAnalyzer.mergeTripStats() called from jvm: %s, thread-name: %s, thread-id: %d.\n",
				jvmName, threadName, threadId);
		Integer totalTripCount = stats.getTotalTripCount().intValue() + 1;
		Integer totalPassengerCount = stats.getTotalPassengerCount().intValue()
				+ nextTrip.getPassengerCount().intValue();
		Double totalDistanceCovered = stats.getTotalDistanceCovered().doubleValue()
				+ nextTrip.getTripDistance().doubleValue();
		BigDecimal totalFareAmount = stats.getTotalFareAmount().add(nextTrip.getFareAmount());
		stats.setTotalTripCount(totalTripCount);
		stats.setTotalPassengerCount(totalPassengerCount);
		stats.setTotalDistanceCovered(totalDistanceCovered);
		stats.setTotalFareAmount(totalFareAmount);
		return stats;
	}

	private TripStats combineTripStats(TripStats stats1, TripStats stats2) {
		String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		String threadName = Thread.currentThread().getName();
		long threadId = Thread.currentThread().getId();
		System.out.printf("TripAnalyzer.combineTripStats() called from jvm: %s, thread-name: %s, thread-id: %d.\n",
				jvmName, threadName, threadId);
		Integer totalTripCount = stats1.getTotalTripCount().intValue() + stats2.getTotalTripCount().intValue();
		Integer totalPassengerCount = stats1.getTotalPassengerCount().intValue()
				+ stats2.getTotalPassengerCount().intValue();
		Double totalDistanceCovered = stats1.getTotalDistanceCovered().doubleValue()
				+ stats2.getTotalDistanceCovered().doubleValue();
		BigDecimal totalFareAmount = stats1.getTotalFareAmount().add(stats2.getTotalFareAmount());
		stats1.setTotalTripCount(totalTripCount);
		stats1.setTotalPassengerCount(totalPassengerCount);
		stats1.setTotalDistanceCovered(totalDistanceCovered);
		stats1.setTotalFareAmount(totalFareAmount);
		return stats1;
	}

	private TimeBand mapToTimeBand(TaxiTrip taxiTrip, List<TimeBand> timeBands) {
		String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		String threadName = Thread.currentThread().getName();
		long threadId = Thread.currentThread().getId();
		System.out.printf("TripAnalyzer.mapToTimeBand() called from jvm: %s, thread-name: %s, thread-id: %d.\n",
				jvmName, threadName, threadId);
		LocalTime pickupTime = taxiTrip.getPickupDateTime().toLocalTime();
		TimeBand selectedTimeBand = null;

		for (TimeBand timeBand : timeBands) {
			if (pickupTime.equals(timeBand.getStartTime()) || pickupTime.isAfter(timeBand.getStartTime())) {
				if (timeBand.getEndTime() == null) {
					selectedTimeBand = timeBand;
					break;
				} else if (pickupTime.isBefore(timeBand.getEndTime())) {
					selectedTimeBand = timeBand;
					break;
				}
			}
		}

		return selectedTimeBand;
	}
}

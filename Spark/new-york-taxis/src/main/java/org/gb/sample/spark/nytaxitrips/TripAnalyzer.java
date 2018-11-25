package org.gb.sample.spark.nytaxitrips;

import java.io.Serializable;
import java.time.Month;
import java.util.List;
import java.util.Map;

import org.apache.spark.broadcast.Broadcast;

public interface TripAnalyzer extends Serializable {

	Map<Integer, Integer> getTripCountPerPsngrCount();

	List<TaxiTrip> getTripsWithPsngrsAboveTshld(int tshldPsngrCnt);

	Map<Month, Double> getAvgPsngrCountPerMonth();

	Map<TimeBand, TripStats> getTripStatsPerTimeBand(List<TimeBand> timeBands);

	Map<TimeBand, TripStats> getTripStatsPerTimeBand(Broadcast<List<TimeBand>> timeBands);

	Map<Month, MaxMinFares> getMaxMinFaresPerMonth(TimeBand timeBand, boolean useCaching); 
}

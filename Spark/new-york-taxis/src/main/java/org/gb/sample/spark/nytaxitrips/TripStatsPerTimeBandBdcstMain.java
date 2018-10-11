package org.gb.sample.spark.nytaxitrips;

public class TripStatsPerTimeBandBdcstMain {

	public static void main(String[] args) {
		String taxiTripFile = args[0];
		TripStatsPerTimeBandLister statsLister = TripStatsPerTimeBandLister.getInstance();
		statsLister.collectTripStats(taxiTripFile, true);
	}
}

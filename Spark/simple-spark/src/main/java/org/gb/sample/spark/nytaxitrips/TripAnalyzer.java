package org.gb.sample.spark.nytaxitrips;

import java.util.List;
import java.util.Objects;

import org.apache.spark.api.java.JavaRDD;

public class TripAnalyzer {

	private Converter converter;
	private JavaRDD<TaxiTrip> taxiTrips;

	TripAnalyzer(JavaRDD<String> taxiTripLines) {
		converter = Converter.getInstance();
		taxiTrips = taxiTripLines.map(converter::convertToTaxiTrip);
	}

	List<TaxiTrip> getTripsWithPsngrsAboveTshld(int tshldPsngrCnt) {
		JavaRDD<TaxiTrip> tripsWithPsngrsAboveTshld = taxiTrips.filter(Objects::nonNull)
				.filter(trip -> trip.getPassengerCount().intValue() >= tshldPsngrCnt);
		return tripsWithPsngrsAboveTshld.collect();
	}
}

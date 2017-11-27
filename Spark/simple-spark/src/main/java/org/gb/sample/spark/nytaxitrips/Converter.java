package org.gb.sample.spark.nytaxitrips;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Converter implements Serializable {

	private static final long serialVersionUID = 3646605257162251213L;
	private static final Converter singleton = new Converter();

	private Converter() {

	}

	static Converter getInstance() {
		return singleton;
	}

	TaxiTrip convertToTaxiTrip(String tripLine) {
		TaxiTrip trip;
		try {
			String[] tripLineTokens = tripLine.split(",");
			Integer vendorId = Integer.parseInt(tripLineTokens[0].trim());
			LocalDateTime pickupDateTime = LocalDateTime.parse(tripLineTokens[1].trim(),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime dropoffDateTime = LocalDateTime.parse(tripLineTokens[2].trim(),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			Integer passengerCount = Integer.parseInt(tripLineTokens[3].trim());
			Double tripDistance = Double.parseDouble(tripLineTokens[4].trim());
			Double pickupLongitude = Double.parseDouble(tripLineTokens[5].trim());
			Double pickupLatitude = Double.parseDouble(tripLineTokens[6].trim());
			Integer ratecodeId = Integer.parseInt(tripLineTokens[7].trim());
			Boolean storeAndFwd = tripLineTokens[8].equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE;
			Double dropoffLongitude = Double.parseDouble(tripLineTokens[9].trim());
			Double dropoffLatitude = Double.parseDouble(tripLineTokens[10].trim());
			Integer paymentType = Integer.parseInt(tripLineTokens[11].trim());
			BigDecimal fareAmount = new BigDecimal(tripLineTokens[12].trim());
			BigDecimal extra = new BigDecimal(tripLineTokens[13].trim());
			BigDecimal mtaTax = new BigDecimal(tripLineTokens[14].trim());
			BigDecimal tipAmount = new BigDecimal(tripLineTokens[15].trim());
			BigDecimal tollsAmount = new BigDecimal(tripLineTokens[16].trim());
			BigDecimal improvementSurcharge = new BigDecimal(tripLineTokens[17].trim());
			BigDecimal totalAmount = new BigDecimal(tripLineTokens[18].trim());

			trip = new TaxiTrip();
			trip.setVendorId(vendorId);
			trip.setPickupDateTime(pickupDateTime);
			trip.setDropoffDateTime(dropoffDateTime);
			trip.setPassengerCount(passengerCount);
			trip.setTripDistance(tripDistance);
			trip.setPickupLongitude(pickupLongitude);
			trip.setPickupLatitude(pickupLatitude);
			trip.setRatecodeId(ratecodeId);
			trip.setStoreAndFwd(storeAndFwd);
			trip.setDropoffLongitude(dropoffLongitude);
			trip.setDropoffLatitude(dropoffLatitude);
			trip.setPaymentType(paymentType);
			trip.setFareAmount(fareAmount);
			trip.setExtra(extra);
			trip.setMtaTax(mtaTax);
			trip.setTipAmount(tipAmount);
			trip.setTollsAmount(tollsAmount);
			trip.setImprovementSurcharge(improvementSurcharge);
			trip.setTotalAmount(totalAmount);
		} catch (Exception e) {
			System.err.println(String.format("Could not convert tripLine: %s to an instance of TaxiTrip.", tripLine));
			trip = null;
		}

		return trip;
	}
}

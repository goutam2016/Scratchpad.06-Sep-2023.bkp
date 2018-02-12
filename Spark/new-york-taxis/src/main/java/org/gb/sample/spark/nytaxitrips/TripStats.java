package org.gb.sample.spark.nytaxitrips;

import java.io.Serializable;
import java.math.BigDecimal;

public class TripStats implements Serializable {
	
	private static final long serialVersionUID = 6512054887969205055L;
	
	private Integer totalTripCount;
	private Integer totalPassengerCount;
	private Double avgPassengersPerTrip;
	private Double totalDistanceCovered;
	private Double avgDistancePerTrip;
	private BigDecimal totalFareAmount;
	private BigDecimal avgFarePerTrip;

	Integer getTotalTripCount() {
		return totalTripCount;
	}

	void setTotalTripCount(Integer totalTripCount) {
		this.totalTripCount = totalTripCount;
	}

	Integer getTotalPassengerCount() {
		return totalPassengerCount;
	}

	void setTotalPassengerCount(Integer totalPassengerCount) {
		this.totalPassengerCount = totalPassengerCount;
	}

	Double getAvgPassengersPerTrip() {
		return avgPassengersPerTrip;
	}

	void setAvgPassengersPerTrip(Double avgPassengersPerTrip) {
		this.avgPassengersPerTrip = avgPassengersPerTrip;
	}

	Double getTotalDistanceCovered() {
		return totalDistanceCovered;
	}

	void setTotalDistanceCovered(Double totalDistanceCovered) {
		this.totalDistanceCovered = totalDistanceCovered;
	}

	Double getAvgDistancePerTrip() {
		return avgDistancePerTrip;
	}

	void setAvgDistancePerTrip(Double avgDistancePerTrip) {
		this.avgDistancePerTrip = avgDistancePerTrip;
	}

	BigDecimal getTotalFareAmount() {
		return totalFareAmount;
	}

	void setTotalFareAmount(BigDecimal totalFareAmount) {
		this.totalFareAmount = totalFareAmount;
	}

	BigDecimal getAvgFarePerTrip() {
		return avgFarePerTrip;
	}

	void setAvgFarePerTrip(BigDecimal avgFarePerTrip) {
		this.avgFarePerTrip = avgFarePerTrip;
	}

	@Override
	public String toString() {
		String statsAsStr = "Total no. of trips: %d, total no. of passengers: %d, total distance covered: %6.2f, total fare amount: %s.";
		return String.format(statsAsStr, totalTripCount, totalPassengerCount, totalDistanceCovered, totalFareAmount);
	}
}

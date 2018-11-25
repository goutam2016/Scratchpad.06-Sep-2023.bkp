package org.gb.sample.spark.nytaxitrips;

import java.io.Serializable;
import java.math.BigDecimal;

public class MaxMinFares implements Serializable {

	private static final long serialVersionUID = 8377098700384373667L;

	private TimeBand timeBand;
	private BigDecimal maxFareAmount;
	private BigDecimal minFareAmount;

	MaxMinFares(TimeBand timeBand) {
		super();
		this.timeBand = timeBand;
	}

	MaxMinFares(TimeBand timeBand, BigDecimal maxFareAmount, BigDecimal minFareAmount) {
		super();
		this.timeBand = timeBand;
		this.maxFareAmount = maxFareAmount;
		this.minFareAmount = minFareAmount;
	}
	
	TimeBand getTimeBand() {
		return timeBand;
	}

	BigDecimal getMaxFareAmount() {
		return maxFareAmount;
	}
	void setMaxFareAmount(BigDecimal maxFareAmount) {
		this.maxFareAmount = maxFareAmount;
	}

	BigDecimal getMinFareAmount() {
		return minFareAmount;
	}
	void setMinFareAmount(BigDecimal minFareAmount) {
		this.minFareAmount = minFareAmount;
	}
}

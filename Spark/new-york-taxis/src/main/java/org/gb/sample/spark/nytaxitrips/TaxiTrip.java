package org.gb.sample.spark.nytaxitrips;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TaxiTrip implements Serializable {

	private static final long serialVersionUID = -3141510117236530281L;

	private Integer vendorId;
	private LocalDateTime pickupDateTime;
	private LocalDateTime dropoffDateTime;
	private Integer passengerCount;
	private Double tripDistance;
	private Double pickupLongitude;
	private Double pickupLatitude;
	private Integer ratecodeId;
	private Boolean storeAndFwd;
	private Double dropoffLongitude;
	private Double dropoffLatitude;
	private Integer paymentType;
	private BigDecimal fareAmount;
	private BigDecimal extra;
	private BigDecimal mtaTax;
	private BigDecimal tipAmount;
	private BigDecimal tollsAmount;
	private BigDecimal improvementSurcharge;
	private BigDecimal totalAmount;
	
	Integer getVendorId() {
		return vendorId;
	}
	void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	LocalDateTime getPickupDateTime() {
		return pickupDateTime;
	}
	void setPickupDateTime(LocalDateTime pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}
	LocalDateTime getDropoffDateTime() {
		return dropoffDateTime;
	}
	void setDropoffDateTime(LocalDateTime dropoffDateTime) {
		this.dropoffDateTime = dropoffDateTime;
	}
	Integer getPassengerCount() {
		return passengerCount;
	}
	void setPassengerCount(Integer passengerCount) {
		this.passengerCount = passengerCount;
	}
	Double getTripDistance() {
		return tripDistance;
	}
	void setTripDistance(Double tripDistance) {
		this.tripDistance = tripDistance;
	}
	Double getPickupLongitude() {
		return pickupLongitude;
	}
	void setPickupLongitude(Double pickupLongitude) {
		this.pickupLongitude = pickupLongitude;
	}
	Double getPickupLatitude() {
		return pickupLatitude;
	}
	void setPickupLatitude(Double pickupLatitude) {
		this.pickupLatitude = pickupLatitude;
	}
	Integer getRatecodeId() {
		return ratecodeId;
	}
	void setRatecodeId(Integer ratecodeId) {
		this.ratecodeId = ratecodeId;
	}
	Boolean getStoreAndFwd() {
		return storeAndFwd;
	}
	void setStoreAndFwd(Boolean storeAndFwd) {
		this.storeAndFwd = storeAndFwd;
	}
	Double getDropoffLongitude() {
		return dropoffLongitude;
	}
	void setDropoffLongitude(Double dropoffLongitude) {
		this.dropoffLongitude = dropoffLongitude;
	}
	Double getDropoffLatitude() {
		return dropoffLatitude;
	}
	void setDropoffLatitude(Double dropoffLatitude) {
		this.dropoffLatitude = dropoffLatitude;
	}
	Integer getPaymentType() {
		return paymentType;
	}
	void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	BigDecimal getFareAmount() {
		return fareAmount;
	}
	void setFareAmount(BigDecimal fareAmount) {
		this.fareAmount = fareAmount;
	}
	BigDecimal getExtra() {
		return extra;
	}
	void setExtra(BigDecimal extra) {
		this.extra = extra;
	}
	BigDecimal getMtaTax() {
		return mtaTax;
	}
	void setMtaTax(BigDecimal mtaTax) {
		this.mtaTax = mtaTax;
	}
	BigDecimal getTipAmount() {
		return tipAmount;
	}
	void setTipAmount(BigDecimal tipAmount) {
		this.tipAmount = tipAmount;
	}
	BigDecimal getTollsAmount() {
		return tollsAmount;
	}
	void setTollsAmount(BigDecimal tollsAmount) {
		this.tollsAmount = tollsAmount;
	}
	BigDecimal getImprovementSurcharge() {
		return improvementSurcharge;
	}
	void setImprovementSurcharge(BigDecimal improvementSurcharge) {
		this.improvementSurcharge = improvementSurcharge;
	}
	BigDecimal getTotalAmount() {
		return totalAmount;
	}
	void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
}

package org.gb.sample.spark.nytaxitrips;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;

public class SQLTripAnalyzer implements TripAnalyzer {

	private static final long serialVersionUID = -1426435397001902919L;

	private Dataset<Row> tripRecords;
	private List<TimeBand> timeBands;

	SQLTripAnalyzer(Dataset<Row> tripRecords) {
		this.tripRecords = tripRecords.filter(this::isValid).toDF("vendorId", "pickupDateTime", "dropoffDateTime",
				"passengerCount", "tripDistance", "pickupLongitude", "pickupLatitude", "ratecodeId", "storeAndFwd",
				"dropoffLongitude", "dropoffLatitude", "paymentType", "fareAmount", "extra", "mtatax", "tipAmount",
				"tollsAmount", "improvementSurcharge", "totalAmount");
	}

	private <T> T getValue(Row row, String colName) {
		return row.getAs(colName);
	}

	private boolean isValid(Row tripRecord) {
		if (tripRecord.length() != 19) {
			return false;
		}

		try {
			String col3Val = tripRecord.getString(3);
			Integer.parseInt(col3Val);
		} catch (Exception e) {
			System.err.println(String.format(
					"3rd column is considered as passenger count and expected to be integer, but the value is: %s.",
					tripRecord.getString(3)));
			return false;
		}
		return true;
	}

	@Override
	public Map<Integer, Integer> getTripCountPerPsngrCount() {
		Dataset<Row> groupedByPsngrCntRows = tripRecords
				.withColumn("passengerCount", tripRecords.col("passengerCount").cast(DataTypes.IntegerType))
				.groupBy("passengerCount").count();
		List<Row> groupedRows = groupedByPsngrCntRows.collectAsList();
		return groupedRows.stream().collect(Collectors.toMap(row -> getValue(row, "passengerCount"),
				row -> ((Long) getValue(row, "count")).intValue()));
	}

	@Override
	public List<TaxiTrip> getTripsWithPsngrsAboveTshld(int tshldPsngrCnt) {
		Dataset<TaxiTrip> tripsWithPsngrsAboveTshld = tripRecords
				.where(tripRecords.col("passengerCount").cast(DataTypes.IntegerType).geq(tshldPsngrCnt))
				.map(this::convertToTaxiTrip, Encoders.bean(TaxiTrip.class));
		return tripsWithPsngrsAboveTshld.collectAsList();
	}

	@Override
	public Map<Month, Double> getAvgPsngrCountPerMonth() {
		List<Row> groupedAvgPsngrCountRows = tripRecords
				.withColumn("passengerCount", tripRecords.col("passengerCount").cast(DataTypes.IntegerType))
				.withColumn("pickupMonth", functions.month(tripRecords.col("pickupDateTime"))).groupBy("pickupMonth")
				.avg("passengerCount").collectAsList();
		return groupedAvgPsngrCountRows.stream().collect(Collectors.toMap(row -> Month.of(getValue(row, "pickupMonth")),
				row -> getValue(row, "avg(passengerCount)")));
	}

	@Override
	public Map<TimeBand, TripStats> getTripStatsPerTimeBand(List<TimeBand> timeBands) {
		this.timeBands = timeBands;
		Dataset<Row> tripRecordsWithExtraCols = tripRecords
				.withColumn("eachTrip", functions.lit(1).cast(DataTypes.IntegerType))
				.withColumn("timeBand", functions.callUDF("mapToTimeBandUDF", tripRecords.col("pickupDateTime")));
		List<Row> groupedByTimeBandRows = tripRecordsWithExtraCols.groupBy("timeBand")
				.agg(functions.sum(tripRecordsWithExtraCols.col("eachTrip")).as("totalTripCount"),
						functions.sum(tripRecordsWithExtraCols.col("passengerCount").cast(DataTypes.IntegerType))
								.as("totalPassengerCount"),
						functions.avg(tripRecordsWithExtraCols.col("passengerCount").cast(DataTypes.IntegerType))
								.as("avgPassengersPerTrip"),
						functions.sum(tripRecordsWithExtraCols.col("tripDistance").cast(DataTypes.DoubleType))
								.as("totalDistanceCovered"),
						functions.avg(tripRecordsWithExtraCols.col("tripDistance").cast(DataTypes.DoubleType))
								.as("avgDistancePerTrip"),
						functions.sum(tripRecordsWithExtraCols.col("fareAmount").cast(DataTypes.DoubleType))
								.as("totalFareAmount"),
						functions.avg(tripRecordsWithExtraCols.col("fareAmount").cast(DataTypes.DoubleType))
								.as("avgFarePerTrip"))
				.collectAsList();
		return groupedByTimeBandRows.stream().collect(Collectors.toMap(this::createTimeBand, this::createTripStats));
	}
	
	@Override
	public Map<Month, MaxMinFares> getMaxMinFaresPerMonth(TimeBand timeBand, boolean useCaching) {
		Dataset<Row> tripRecordsWithinTimeBand = tripRecords
				.filter(tripRecord -> isPickupWithinTimeBand(tripRecord, timeBand))
				.withColumn("pickupMonth", functions.month(tripRecords.col("pickupDateTime")))
				.withColumn("fareAmount", tripRecords.col("fareAmount").cast(DataTypes.createDecimalType()));

		if (useCaching) {
			tripRecordsWithinTimeBand = tripRecordsWithinTimeBand.persist();
		}

		List<Row> groupedMaxFareAmtRows = tripRecordsWithinTimeBand.groupBy("pickupMonth").max("fareAmount")
				.collectAsList();
		List<Row> groupedMinFareAmtRows = tripRecordsWithinTimeBand.groupBy("pickupMonth").min("fareAmount")
				.collectAsList();

		if (useCaching) {
			tripRecordsWithinTimeBand.unpersist();
		}

		Map<Month, MaxMinFares> maxFarePerMonth = groupedMaxFareAmtRows.stream()
				.collect(Collectors.toMap(row -> Month.of(getValue(row, "pickupMonth")),
						row -> createMaxMinFares(timeBand, row, "max(fareAmount)", null)));
		Map<Month, MaxMinFares> minFarePerMonth = groupedMinFareAmtRows.stream()
				.collect(Collectors.toMap(row -> Month.of(getValue(row, "pickupMonth")),
						row -> createMaxMinFares(timeBand, row, null, "min(fareAmount)")));

		Stream<Entry<Month, MaxMinFares>> concatdMaxMinFares = Stream.concat(maxFarePerMonth.entrySet().stream(),
				minFarePerMonth.entrySet().stream());
		Map<Month, MaxMinFares> maxMinFaresPerMonth = concatdMaxMinFares.collect(Collectors.toMap(Map.Entry::getKey,
				Map.Entry::getValue, (withMaxFare, withMinFare) -> new MaxMinFares(timeBand,
						withMaxFare.getMaxFareAmount(), withMinFare.getMinFareAmount())));

		return maxMinFaresPerMonth;
	}
	
	private MaxMinFares createMaxMinFares(TimeBand timeBand, Row maxMinFareRow, String maxFareColName,
			String minFareColName) {
		MaxMinFares maxMinFares = new MaxMinFares(timeBand);

		if (maxFareColName != null) {
			maxMinFares.setMaxFareAmount(getValue(maxMinFareRow, maxFareColName));
		}
		if (minFareColName != null) {
			maxMinFares.setMinFareAmount(getValue(maxMinFareRow, minFareColName));
		}
		return maxMinFares;
	}
	
	private boolean isPickupWithinTimeBand(Row tripRecord, TimeBand timeBand) {
		LocalDateTime pickupDateTime = LocalDateTime.parse(((String)tripRecord.getAs("pickupDateTime")).trim(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		if ((pickupDateTime.toLocalTime().equals(timeBand.getStartTime())
				|| pickupDateTime.toLocalTime().isAfter(timeBand.getStartTime()))
				&& pickupDateTime.toLocalTime().isBefore(timeBand.getEndTime())) {
			return true;
		}

		return false;
	}
	
	private TimeBand createTimeBand(Row row) {
		Row timeBandAsRow = getValue(row, "timeBand");
		String startTimeStr = getValue(timeBandAsRow, "startTime");
		String endTimeStr = getValue(timeBandAsRow, "endTime");
		LocalTime startTime = LocalTime.parse(startTimeStr, DateTimeFormatter.ISO_LOCAL_TIME);
		LocalTime endTime = LocalTime.parse(endTimeStr, DateTimeFormatter.ISO_LOCAL_TIME);
		return new TimeBand(startTime, endTime);		
	}
	
	private TripStats createTripStats(Row row) {
		Long totalTripCount = getValue(row, "totalTripCount");
		Long totalPassengerCount = getValue(row, "totalPassengerCount");
		Double avgPassengersPerTrip = getValue(row, "avgPassengersPerTrip");
		Double totalDistanceCovered = getValue(row, "totalDistanceCovered");
		Double avgDistancePerTrip = getValue(row, "avgDistancePerTrip");
		Double totalFareAmount = getValue(row, "totalFareAmount");
		Double avgFarePerTrip = getValue(row, "avgFarePerTrip");
		TripStats tripStats = new TripStats();
		tripStats.setTotalTripCount(totalTripCount.intValue());
		tripStats.setTotalPassengerCount(totalPassengerCount.intValue());
		tripStats.setAvgPassengersPerTrip(avgPassengersPerTrip);
		tripStats.setTotalDistanceCovered(totalDistanceCovered);
		tripStats.setAvgDistancePerTrip(avgDistancePerTrip);
		tripStats.setTotalFareAmount(BigDecimal.valueOf(totalFareAmount.doubleValue()));
		tripStats.setAvgFarePerTrip(BigDecimal.valueOf(avgFarePerTrip.doubleValue()));
		return tripStats;
	}

	@Override
	public Map<TimeBand, TripStats> getTripStatsPerTimeBand(Broadcast<List<TimeBand>> timeBands) {
		// TODO Auto-generated method stub
		return null;
	}

	TaxiTrip convertToTaxiTrip(Row tripRecord) {
		TaxiTrip trip;
		try {
			Integer vendorId = Integer.parseInt(tripRecord.getString(0).trim());
			LocalDateTime pickupDateTime = LocalDateTime.parse(tripRecord.getString(1).trim(),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime dropoffDateTime = LocalDateTime.parse(tripRecord.getString(2).trim(),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			Integer passengerCount = Integer.parseInt(tripRecord.getString(3).trim());
			Double tripDistance = Double.parseDouble(tripRecord.getString(4).trim());
			Double pickupLongitude = Double.parseDouble(tripRecord.getString(5).trim());
			Double pickupLatitude = Double.parseDouble(tripRecord.getString(6).trim());
			Integer ratecodeId = Integer.parseInt(tripRecord.getString(7).trim());
			Boolean storeAndFwd = tripRecord.getString(8).equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE;
			Double dropoffLongitude = Double.parseDouble(tripRecord.getString(9).trim());
			Double dropoffLatitude = Double.parseDouble(tripRecord.getString(10).trim());
			Integer paymentType = Integer.parseInt(tripRecord.getString(11).trim());
			BigDecimal fareAmount = new BigDecimal(tripRecord.getString(12).trim());
			BigDecimal extra = new BigDecimal(tripRecord.getString(13).trim());
			BigDecimal mtaTax = new BigDecimal(tripRecord.getString(14).trim());
			BigDecimal tipAmount = new BigDecimal(tripRecord.getString(15).trim());
			BigDecimal tollsAmount = new BigDecimal(tripRecord.getString(16).trim());
			BigDecimal improvementSurcharge = new BigDecimal(tripRecord.getString(17).trim());
			BigDecimal totalAmount = new BigDecimal(tripRecord.getString(18).trim());

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
			System.err.println(String.format("Could not convert tripLine: %s to an instance of TaxiTrip.", tripRecord));
			trip = null;
		}

		return trip;
	}

	Row mapToTimeBandUDF(String pickupDateTimeStr) {
		LocalDateTime pickupDateTime = LocalDateTime.parse(pickupDateTimeStr.trim(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		LocalTime pickupTime = pickupDateTime.toLocalTime();
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

		if (selectedTimeBand == null) {
			return null;
		}

		String formattedStartTime = selectedTimeBand.getStartTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
		String formattedEndTime = selectedTimeBand.getEndTime() == null ? "00:00:00"
				: selectedTimeBand.getEndTime().format(DateTimeFormatter.ISO_LOCAL_TIME);

		return RowFactory.create(formattedStartTime, formattedEndTime);
	}
}

package org.gb.sample.spark.nytaxitrips;

import java.time.LocalDateTime;
import java.time.ZoneId;
import com.datastax.driver.core.Row;
import com.datastax.spark.connector.CassandraRowMetadata;
import com.datastax.spark.connector.ColumnRef;
import com.datastax.spark.connector.cql.TableDef;
import com.datastax.spark.connector.rdd.reader.RowReader;
import scala.Option;
import scala.collection.IndexedSeq;
import scala.collection.Seq;

public class TripRecordReader implements RowReader<TaxiTrip> {

	private static final long serialVersionUID = -2078152852829759019L;

	private final TableDef tableDef;
	private final IndexedSeq<ColumnRef> columnRefs;

	TripRecordReader(TableDef tableDef, IndexedSeq<ColumnRef> columnRefs) {
		this.tableDef = tableDef;
		this.columnRefs = columnRefs;
	}

	@Override
	public Option<Seq<ColumnRef>> neededColumns() {
		return Option.apply(columnRefs);
	}

	@Override
	public TaxiTrip read(Row row, CassandraRowMetadata rowMetadata) {
		TaxiTrip taxiTrip = new TaxiTrip();
		taxiTrip.setVendorId(Integer.valueOf(row.getByte("vendor_id")));
		
		LocalDateTime pickupDateTime = LocalDateTime.ofInstant(row.getTimestamp("pickup_date_time").toInstant(), ZoneId.systemDefault());
		taxiTrip.setPickupDateTime(pickupDateTime);
		LocalDateTime dropoffDateTime = LocalDateTime.ofInstant(row.getTimestamp("dropoff_date_time").toInstant(), ZoneId.systemDefault());
		taxiTrip.setDropoffDateTime(dropoffDateTime);
		
		taxiTrip.setPassengerCount(Integer.valueOf(row.getByte("passenger_count")));
		taxiTrip.setTripDistance(row.getDouble("trip_distance"));
		taxiTrip.setPickupLongitude(row.getDouble("pickup_longitude"));
		taxiTrip.setPickupLatitude(row.getDouble("pickup_latitude"));
		taxiTrip.setRatecodeId(Integer.valueOf(row.getByte("ratecode_id")));
		taxiTrip.setStoreAndFwd(row.getBool("store_and_fwd"));
		taxiTrip.setDropoffLongitude(row.getDouble("dropoff_longitude"));
		taxiTrip.setDropoffLatitude(row.getDouble("dropoff_latitude"));
		taxiTrip.setPaymentType(Integer.valueOf(row.getByte("payment_type")));
		taxiTrip.setFareAmount(row.getDecimal("fare_amount"));
		taxiTrip.setExtra(row.getDecimal("extra"));
		taxiTrip.setMtaTax(row.getDecimal("mta_tax"));
		taxiTrip.setTipAmount(row.getDecimal("tip_amount"));
		taxiTrip.setTollsAmount(row.getDecimal("tolls_amount"));
		taxiTrip.setImprovementSurcharge(row.getDecimal("improvement_surcharge"));
		taxiTrip.setTotalAmount(row.getDecimal("total_amount"));
		
		return taxiTrip;
	}

}

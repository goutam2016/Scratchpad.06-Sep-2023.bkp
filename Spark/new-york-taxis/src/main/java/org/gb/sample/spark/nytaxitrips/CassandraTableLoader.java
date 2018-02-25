package org.gb.sample.spark.nytaxitrips;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.rdd.CassandraTableScanJavaRDD;
import com.datastax.spark.connector.rdd.reader.RowReaderFactory;

public class CassandraTableLoader implements TripDataLoader {

	private final JavaSparkContext sparkContext;
	private final RowReaderFactory<TaxiTrip> rowRdrFactory;

	CassandraTableLoader(JavaSparkContext sparkContext) {
		this.sparkContext = sparkContext;
		rowRdrFactory = new TripRecordReaderFactory();
	}

	@Override
	public JavaRDD<TaxiTrip> fetchRecords() {
		CassandraTableScanJavaRDD<TaxiTrip> tripRecordTableRows = CassandraJavaUtil.javaFunctions(sparkContext)
				.cassandraTable("nytaxis", "trip_record", rowRdrFactory);
		return tripRecordTableRows;
	}

}

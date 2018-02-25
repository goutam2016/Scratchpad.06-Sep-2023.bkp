package org.gb.sample.spark.nytaxitrips;

import com.datastax.spark.connector.ColumnRef;
import com.datastax.spark.connector.cql.TableDef;
import com.datastax.spark.connector.rdd.reader.RowReader;
import com.datastax.spark.connector.rdd.reader.RowReaderFactory;

import scala.collection.IndexedSeq;

public class TripRecordReaderFactory implements RowReaderFactory<TaxiTrip> {

	@Override
	public RowReader<TaxiTrip> rowReader(TableDef tableDef, IndexedSeq<ColumnRef> indexedSeq) {
		return new TripRecordReader(tableDef, indexedSeq);
	}

	@Override
	public Class<TaxiTrip> targetClass() {
		return TaxiTrip.class;
	}

}

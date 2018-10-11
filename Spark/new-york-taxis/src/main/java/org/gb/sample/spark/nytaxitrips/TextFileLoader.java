package org.gb.sample.spark.nytaxitrips;

import java.io.Serializable;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;

public class TextFileLoader implements TripDataLoader, Serializable {

	private static final long serialVersionUID = 1175689118437396358L;
	private final transient JavaSparkContext sparkContext;
	private final String taxiTripFile;
	private final Converter converter;
	private final LongAccumulator defectiveRecords;

	TextFileLoader(JavaSparkContext sparkContext, String taxiTripFile) {
		this(sparkContext, taxiTripFile, null);
	}

	TextFileLoader(JavaSparkContext sparkContext, String taxiTripFile, LongAccumulator defectiveRecords) {
		super();
		this.sparkContext = sparkContext;
		this.taxiTripFile = taxiTripFile;
		this.converter = Converter.getInstance();
		this.defectiveRecords = defectiveRecords;
	}

	@Override
	public JavaRDD<TaxiTrip> fetchRecords() {
		JavaRDD<String> taxiTripLines = sparkContext.textFile(taxiTripFile);
		JavaRDD<TaxiTrip> taxiTrips = taxiTripLines.map(converter::convertToTaxiTrip).filter(this::isValid);
		return taxiTrips;
	}

	private boolean isValid(TaxiTrip trip) {
		if (trip == null) {
			defectiveRecords.add(1);
			return false;
		} else {
			return true;
		}
	}
}

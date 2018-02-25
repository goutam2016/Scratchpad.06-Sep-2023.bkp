package org.gb.sample.spark.nytaxitrips;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class TextFileLoader implements TripDataLoader {

	private final JavaSparkContext sparkContext;
	private final String taxiTripFile;
	private final Converter converter;

	TextFileLoader(JavaSparkContext sparkContext, String taxiTripFile) {
		this.sparkContext = sparkContext;
		this.taxiTripFile = taxiTripFile;
		converter = Converter.getInstance();
	}

	@Override
	public JavaRDD<TaxiTrip> fetchRecords() {
		JavaRDD<String> taxiTripLines = sparkContext.textFile(taxiTripFile);
		JavaRDD<TaxiTrip> taxiTrips = taxiTripLines.map(converter::convertToTaxiTrip);
		return taxiTrips;
	}

}

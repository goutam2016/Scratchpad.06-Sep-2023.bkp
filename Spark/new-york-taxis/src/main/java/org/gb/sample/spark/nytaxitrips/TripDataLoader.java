package org.gb.sample.spark.nytaxitrips;

import org.apache.spark.api.java.JavaRDD;

public interface TripDataLoader {

	JavaRDD<TaxiTrip> fetchRecords();
}
 
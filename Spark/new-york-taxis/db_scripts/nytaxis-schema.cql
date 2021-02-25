DROP KEYSPACE IF EXISTS nytaxis;
CREATE KEYSPACE nytaxis WITH replication = {'class': 'SimpleStrategy', 'replication_factor':2};
USE nytaxis;

CREATE TABLE trip_record ( 
	vendor_id tinyint,
	pickup_date_time timestamp,
	dropoff_date_time timestamp,
	passenger_count tinyint,
	trip_distance double,
	pickup_longitude double,
	pickup_latitude double,
	ratecode_id tinyint,
	store_and_fwd boolean,
	dropoff_longitude double,
	dropoff_latitude double,
	payment_type tinyint,
	fare_amount decimal,
	extra decimal,
	mta_tax decimal,
	tip_amount decimal,
	tolls_amount decimal,
	improvement_surcharge decimal,
	total_amount decimal,
	PRIMARY KEY ((pickup_date_time, dropoff_date_time), passenger_count, trip_distance) 
);

COPY trip_record
(
	vendor_id, pickup_date_time, dropoff_date_time, passenger_count, trip_distance, pickup_longitude, pickup_latitude,
	ratecode_id, store_and_fwd, dropoff_longitude, dropoff_latitude, payment_type, fare_amount, extra, mta_tax,
	tip_amount, tolls_amount, improvement_surcharge, total_amount
)  
FROM 'test-data/yellow_tripdata_100000.csv';
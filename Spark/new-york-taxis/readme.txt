1. 	Build the project. 
	mvn -f simple-spark/pom.xml clean install
2.	Using Spark Standalone Cluster
	%SPARK_HOME%\sbin\start-master.sh
	%SPARK_HOME%\sbin\start-slave.sh spark://localhost:7077
3.	From command prompt, run the application with spark-submit.
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.nytaxitrips.TripStatsPerTimeBandMain --properties-file conf/standalone.properties target/new-york-taxis-0.0.1-SNAPSHOT.jar data/yellow_tripdata_2016-01.csv
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.nytaxitrips.TripsByPassengerCountMain --properties-file conf/standalone.properties target/new-york-taxis-0.0.1-SNAPSHOT.jar data/
	
	
Mounting a shared folder into VirtualBox VM
---------------------------------------------
sudo mount -t vboxsf -o uid=1000,gid=1000 SharedFromHost /mnt/SharedFromHost/

Connect to Cassandra node using cqlsh
------------------------------------------------
cqlsh --request-timeout=600 192.168.56.4 9042

Load from csv file using cassandra-loader
------------------------------------------------
./cassandra-loader -f /WorkArea/Workspaces/Scratchpad/Spark/new-york-taxis/data/yellow_tripdata_2016-01.csv -host localhost 
-schema "nytaxis.trip_record(vendor_id, pickup_date_time, dropoff_date_time, passenger_count, trip_distance, pickup_longitude, pickup_latitude,ratecode_id, store_and_fwd, 
dropoff_longitude, dropoff_latitude, payment_type, fare_amount, extra, mta_tax,tip_amount, tolls_amount, improvement_surcharge, total_amount)" 
-dateFormat "yyyy-MM-dd HH:mm:ss" -boolStyle "Y_N" -batchSize 100

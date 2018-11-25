1. 	Build the project. 
	mvn -f simple-spark/pom.xml clean install
2.	Using Spark Standalone Cluster
	%SPARK_HOME%\sbin\start-master.sh
	%SPARK_HOME%\sbin\start-slave.sh spark://localhost:7077
3.	From command prompt, run the application with spark-submit.
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.nytaxitrips.TripStatsPerTimeBandMain --properties-file conf/standalone.properties target/new-york-taxis-0.0.1-SNAPSHOT.jar data/yellow_tripdata_2016-01.csv
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.nytaxitrips.TripsByPassengerCountMain --properties-file conf/standalone.properties target/new-york-taxis-0.0.1-SNAPSHOT.jar data/
	
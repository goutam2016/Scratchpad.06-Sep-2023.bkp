1. 	Build the project. 
	mvn -f simple-spark/pom.xml clean install
2.	From command prompt, run the application with spark-submit.
3.	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.nytaxitrips.Main --master local simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/nytaxitrips/yellow_tripdata_1000.csv
4.	Using Spark Standalone Cluster
	%SPARK_HOME%\sbin\start-master.sh
	%SPARK_HOME%\sbin\start-slave.sh spark://localhost:7077
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.nytaxitrips.Main --properties-file simple-spark/conf/standalone.properties simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/nytaxitrips/yellow_tripdata_100000.csv
	
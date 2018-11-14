1. 	Build the project. 
	mvn -f simple-spark/pom.xml clean install
2.	From command prompt, run the application with spark-submit.
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.ContextInfoMain --master local simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar
3.	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.wordcount.Main --master local simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/wordcount/poem.txt
4.	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.temperature.Main simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/temperature/max-min-temp-by-year.txt
5.	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.sia.ch04.Main --master local simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/sia/ch04/transactions.txt simple-spark/data/sia/ch04/products.txt simple-spark/data/sia/ch04/output
6.	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.nytaxitrips.Main --master local simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/nytaxitrips/yellow_tripdata_1000.csv
7.	Using Spark Standalone Cluster
	%SPARK_HOME%\sbin\start-master.sh
	%SPARK_HOME%\sbin\start-slave.sh spark://localhost:7077
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.ContextInfoMain --master spark://localhost:7077 simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.income.SparkBehaviourMain --properties-file simple-spark/conf/standalone.properties simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar /simple-spark/data/income/person-profile_1000000.txt
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.wordcount.Main --master spark://localhost:7077 simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/wordcount/poem.txt
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.temperature.Main2 --master spark://localhost:7077 simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/temperature/max-min-temp-by-year.txt
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.income.Main --master spark://localhost:7077 simple-spark/target/uber-simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/income/name-vs-income_500.txt simple-spark/data/income/person-profile_500.txt
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.income.IncomeBandMain --master spark://localhost:7077 simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/income/name-vs-income_500.txt 0,30000,50000,75000,100000,125000
	
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.income.TopIncomesMain --properties-file simple-spark/conf/standalone.properties simple-spark/target/uber-simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/income/name-vs-income_500.txt simple-spark/data/income/person-profile_500.txt
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.nytaxitrips.Main --properties-file simple-spark/conf/standalone.properties simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/nytaxitrips/yellow_tripdata_100000.csv
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.income.IncomeBandMain --properties-file simple-spark/conf/standalone.properties simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/income/name-vs-income_50000.txt 0,30000,50000,80000
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.income.IncomeBandMain --master yarn target/simple-spark-0.0.1-SNAPSHOT.jar hdfs://localhost:9000/data/scratchpad/simple-spark/income/name-vs-income_1000000.txt 0,30000,50000,75000,100000,125000

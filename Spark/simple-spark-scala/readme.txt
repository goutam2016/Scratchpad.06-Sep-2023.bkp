spark-submit --class org.gb.sample.spark.income.TopIncomesMain --master local target/uber-simple-spark-scala-0.0.1-SNAPSHOT.jar data/income/name-vs-income_500.txt data/income/person-profile_500.txt

Using Spark Standalone Cluster
%SPARK_HOME%\sbin\start-master.sh
%SPARK_HOME%\sbin\start-slave.sh spark://localhost:7077
	
spark-submit --class org.gb.sample.spark.income.TopIncomesMain --master spark://localhost:7077 target/uber-simple-spark-scala-0.0.1-SNAPSHOT.jar data/income/name-vs-income_50000.txt data/income/person-profile_50000.txt
spark-submit --class org.gb.sample.spark.income.TopIncomesMain --properties-file conf/standalone.properties target/uber-simple-spark-scala-0.0.1-SNAPSHOT.jar data/income/name-vs-income_50000.txt data/income/person-profile_50000.txt

1. 	Build the project. 
	mvn -f simple-spark/pom.xml clean install
2.	From command prompt, run the application with spark-submit.
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.ContextInfoMain simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar
3.	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.wordcount.Main simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/wordcount/poem.txt
4.	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.temperature.Main simple-spark/target/simple-spark-0.0.1-SNAPSHOT.jar simple-spark/data/temperature/max-min-temp-by-year.txt
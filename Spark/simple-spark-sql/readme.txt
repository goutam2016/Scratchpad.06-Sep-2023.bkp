1. 	Build the project. 
	mvn -f simple-spark/pom.xml clean install
2.	Using Spark Standalone Cluster
	%SPARK_HOME%\sbin\start-master.sh
	%SPARK_HOME%\sbin\start-slave.sh spark://localhost:7077
3.	From command prompt, run the application with spark-submit.
	%SPARK_HOME%\bin\spark-submit --class org.gb.sample.spark.sql.income.TopIncomesMain --properties-file conf/standalone.properties target/simple-spark-sql-0.0.1-SNAPSHOT.jar data/income/name-vs-income_500.txt data/income/person-profile_500.txt

	
	
	
Inside Spark-shell
---------------------
val nameVsIncome = spark.read.csv("data/income/name-vs-income_500.txt")
val nameVsIncomeWithColNames = nameVsIncome.toDF("firstName", "lastName", "income")
nameVsIncomeWithColNames.createOrReplaceTempView("nameVsIncome")
val sqlDF = spark.sql("SELECT * FROM nameVsIncome")
sqlDF.show
nameVsIncomeWithColNames.printSchema
nameVsIncomeWithColNames.filter($"income" > 60000).show

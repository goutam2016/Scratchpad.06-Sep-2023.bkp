1. 	Build the project. 
	mvn -f simple-jobs\pom.xml clean install
2.	In another command prompt, start Hadoop file system.
	%HADOOP_HOME%\sbin\start-dfs.cmd
3. 	In the 1st command prompt, copy a file to HDFS.
	hadoop fs -conf simple-jobs\conf\hadoop-localhost.xml -ls /
	This will print contents of the root directory (/) from HDFS.
	This command will throw java.net.ConnectException if HDFS has not been started (step 2 has not been done).
4.	To run WordCountMain, HDFS and YARN must be started from admin mode command prompt.
	%HADOOP_HOME%\sbin\start-dfs.cmd
	%HADOOP_HOME%\sbin\start-yarn.cmd
5.	Then run WordCountMain from another command prompt.
	hadoop jar simple-jobs\target\simple-jobs-0.0.1-SNAPSHOT.jar 
	org.gb.sample.hadoop.WordCountMain -conf simple-jobs\conf\hadoop-localhost.xml wordcount wordcount/output
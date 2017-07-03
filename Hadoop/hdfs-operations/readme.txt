1. 	Build the project. 
	mvn -f hdfs-operations\pom.xml clean install
2.	In another command prompt, start Hadoop file system.
	sbin\start-dfs.cmd
3. 	In the 1st command prompt, copy a file to HDFS.
	hadoop fs -copyFromLocal -f D:\WorkSW\IDE\Eclipse\Workspaces\Scratchpad\Hadoop\hdfs-operations\textfiles\helloworld.txt /user/Goutam/textfiles/helloworld.txt
4.	In the 1st command prompt, set HADOOP_CLASSPATH.
	set HADOOP_CLASSPATH=hdfs-operations\target\hdfs-operations-xx.jar
5.	Now run the program.
	hadoop org.gb.sample.hadoop.URLCat hdfs://localhost:9000/user/Goutam/textfiles/helloworld.txt
	This will print contents on the file (helloworld.txt) from HDFS.
6.	hadoop org.gb.sample.hadoop.FileSystemCat hdfs://localhost:9000/user/Goutam/textfiles/helloworld.txt
	This will print contents on the file (helloworld.txt) from HDFS.
7.	hadoop org.gb.sample.hadoop.DirectoryLister hdfs://localhost:9000/
	This will print contents of the root directory (/) from HDFS.
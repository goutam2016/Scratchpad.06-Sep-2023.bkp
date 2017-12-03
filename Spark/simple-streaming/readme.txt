./splitAndSend.sh order-batches/ local
spark-submit --class org.gb.sample.spark.streaming.sia.ch06.Main --master local target/simple-streaming-0.0.1-SNAPSHOT.jar data/sia/ch06/order-batches/

Kafka streams
----------------
1. 	Install Kafka, set KAFKA_HOME, set PATH.
2. 	Open config/server.properties, uncomment listeners=PLAINTEXT://<host>:<port>
3. 	zookeeper-server-start.sh config/zookeeper.properties &
4. 	kafka-server-start.sh config/server.properties &
5. 	Create topic
	kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic <topic-name>
6.	Check existing topics
	kafka-topics.sh --list --zookeeper localhost:2181
7.	Start Spark program to read Kafka streams.
	spark-submit --class org.gb.sample.spark.streaming.sia.ch06.KafkaMain --master local target/uber-simple-streaming-0.0.1-SNAPSHOT.jar
8.	Go to data/sia/ch06 and start streaming to Kafka topic. Subsequently, the Spark program will read from the Kafka topic.
	./streamOrders.sh
9.	KafkaMain should print the incoming orders now.
10.	Stop Kafka and Zookeeper.
	kafka-server-stop.sh
	zookeeper-server-stop.sh
	
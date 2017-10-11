package org.gb.sample.spark.sia.ch04;

import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Main {

	public static void main(String[] args) {
		try {
			String txnFile = args[0];
			String outputLocation = args[1];
			
			Path path = FileSystems.getDefault().getPath(outputLocation);
			Files.deleteIfExists(path);
			
			SparkConf conf = new SparkConf().setMaster("local").setAppName("Spark-in-action chapter 4");
			JavaSparkContext sc = new JavaSparkContext(conf);
			JavaRDD<String> txnLines = sc.textFile(txnFile);
			JavaRDD<Transaction> txns = txnLines.map(Main::convertToTransaction);
			JavaPairRDD<Integer, Transaction> custIdTxnPairs = txns.mapToPair(txn -> new Tuple2<>(txn.getCustomerId(), txn));
			
			JavaPairRDD<Integer, Iterable<Transaction>> custIdVsTxns = custIdTxnPairs.groupByKey();
			custIdVsTxns.saveAsTextFile(outputLocation);
			/*Map<Integer, Long> custIdVsTxnCount = custIdVsTxns.countByKey();
			
			
			custIdVsTxnCount.forEach((custId, txnCount) -> System.out.println(custId + " <--> " + txnCount));*/
			
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Transaction convertToTransaction(String txnLine) {
		String[] txnLineTokens = txnLine.split("#");
		LocalDate txnDate = LocalDate.parse(txnLineTokens[0], DateTimeFormatter.ISO_DATE);
		LocalTime txnTime = LocalTime.parse(txnLineTokens[1], DateTimeFormatter.ofPattern("h:mm a"));
		LocalDateTime txnDateTime = LocalDateTime.of(txnDate, txnTime);
		Integer customerId = Integer.parseInt(txnLineTokens[2]);
		Integer productId = Integer.parseInt(txnLineTokens[3]);
		Integer quantity = Integer.parseInt(txnLineTokens[4]);
		BigDecimal productPrice = new BigDecimal(txnLineTokens[5]);
		Transaction txn = new Transaction();
		txn.setTxnDateTime(txnDateTime);
		txn.setCustomerId(customerId);
		txn.setProductId(productId);
		txn.setQuantity(quantity);
		txn.setProductPrice(productPrice);
		return txn;
	}
}

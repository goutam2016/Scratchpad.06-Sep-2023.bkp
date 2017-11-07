package org.gb.sample.spark.sia.ch04;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;

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
			
			SparkConf conf = new SparkConf().setAppName("Spark-in-action chapter 4");
			String master = conf.get("spark.master");
			
			if(master.startsWith("local")) {
				cleanOutputLocation(outputLocation);
			} else if (master.startsWith("spark")) {
				cleanOutputLocation(outputLocation);
			}
						
			JavaSparkContext sc = new JavaSparkContext(conf);
			JavaRDD<String> txnLines = sc.textFile(txnFile);
			JavaRDD<Transaction> txns = txnLines.map(Main::convertToTransaction);
			JavaPairRDD<Integer, Transaction> custIdTxnPairs = txns.mapToPair(txn -> new Tuple2<>(txn.getCustomerId(), txn));
			
			//JavaPairRDD<Integer, Iterable<Transaction>> custIdVsTxns = custIdTxnPairs.groupByKey();
			//custIdVsTxns.saveAsTextFile(outputLocation);
			//Map<Integer, Long> custIdVsTxnCount = custIdTxnPairs.countByKey();
			//custIdVsTxnCount.forEach((custId, txnCount) -> System.out.println(custId + " <--> " + txnCount));
			Comparator<Tuple2<Integer, Integer>> txnCountComparator = new TxnCountComparator();
			Tuple2<Integer, Integer> maxTxnsTuple = custIdTxnPairs
					.mapToPair(custIdTxnTuple -> new Tuple2<>(custIdTxnTuple._1(), 1))
					.foldByKey(0, (txnCount, oneTxn) -> txnCount + oneTxn).max(txnCountComparator);
			//custIdVsTxnCount.forEach((custId, txnCount) -> System.out.println(custId + " <--> " + txnCount));
			System.out.println(maxTxnsTuple._1() + " <--> " + maxTxnsTuple._2());
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void cleanOutputLocation(String outputLocation) throws IOException {
		Path path = FileSystems.getDefault().getPath(outputLocation);
		
		if(Files.exists(path)) {
			FileVisitor<Path> outputCleaner = new OutputCleaner();
			Files.walkFileTree(path, outputCleaner);
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
	
	private static class TxnCountComparator implements Comparator<Tuple2<Integer, Integer>>, Serializable {

		private static final long serialVersionUID = 8599281137629438124L;

		@Override
		public int compare(Tuple2<Integer, Integer> custIdVsTxnCount1, Tuple2<Integer, Integer> custIdVsTxnCount2) {
			return custIdVsTxnCount1._1().intValue() - custIdVsTxnCount2._2().intValue(); 
		}		
	}
}

package org.gb.sample.spark.sia.ch04;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

public class TransactionAnalyzer implements Serializable {

	private static final long serialVersionUID = 5738463725391572356L;
	private JavaRDD<String> txnLines;

	TransactionAnalyzer(JavaRDD<String> txnLines) {
		super();
		this.txnLines = txnLines;
	}

	private Transaction convertToTransaction(String txnLine) {
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

	Tuple2<Integer, Integer> getCustIdWithMaxTxns() {
		JavaRDD<Transaction> txns = txnLines.map(this::convertToTransaction);
		JavaPairRDD<Integer, Transaction> custIdTxnPairs = txns
				.mapToPair(txn -> new Tuple2<>(txn.getCustomerId(), txn));

		Comparator<Tuple2<Integer, Integer>> txnCountComparator = new TxnCountComparator();
		Tuple2<Integer, Integer> maxTxnsTuple = custIdTxnPairs
				.mapToPair(custIdTxnTuple -> new Tuple2<>(custIdTxnTuple._1(), 1))
				.foldByKey(0, (txnCount, oneTxn) -> txnCount + oneTxn).max(txnCountComparator);
		return maxTxnsTuple;
	}

	private class TxnCountComparator implements Comparator<Tuple2<Integer, Integer>>, Serializable {

		private static final long serialVersionUID = 8599281137629438124L;

		@Override
		public int compare(Tuple2<Integer, Integer> custIdVsTxnCount1, Tuple2<Integer, Integer> custIdVsTxnCount2) {
			return custIdVsTxnCount1._2().intValue() - custIdVsTxnCount2._2().intValue();
		}
	}

}

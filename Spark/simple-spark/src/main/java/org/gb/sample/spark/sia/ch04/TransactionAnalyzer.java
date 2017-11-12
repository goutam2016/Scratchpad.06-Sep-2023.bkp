package org.gb.sample.spark.sia.ch04;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

public class TransactionAnalyzer implements Serializable {

	private static final long serialVersionUID = 5738463725391572356L;
	private JavaRDD<String> txnLines;
	private JavaRDD<String> productLines;

	TransactionAnalyzer(JavaRDD<String> txnLines, JavaRDD<String> productLines) {
		super();
		this.txnLines = txnLines;
		this.productLines = productLines;
	}

	private Transaction convertToTransaction(String txnLine) {
		String[] txnLineTokens = txnLine.split("#");
		LocalDate txnDate = LocalDate.parse(txnLineTokens[0], DateTimeFormatter.ISO_DATE);
		LocalTime txnTime = LocalTime.parse(txnLineTokens[1], DateTimeFormatter.ofPattern("h:mm a"));
		LocalDateTime txnDateTime = LocalDateTime.of(txnDate, txnTime);
		Integer customerId = Integer.parseInt(txnLineTokens[2]);
		Integer productId = Integer.parseInt(txnLineTokens[3]);
		Integer quantity = Integer.parseInt(txnLineTokens[4]);
		BigDecimal aggrPrice = new BigDecimal(txnLineTokens[5]);
		Transaction txn = new Transaction();
		txn.setTxnDateTime(txnDateTime);
		txn.setCustomerId(customerId);
		txn.setProductId(productId);
		txn.setQuantity(quantity);
		txn.setAggrPrice(aggrPrice);
		return txn;
	}

	private Product convertToProduct(String productLine) {
		String[] productLineTokens = productLine.split("#");
		Integer productId = Integer.parseInt(productLineTokens[0]);
		String productName = productLineTokens[1];
		return new Product(productId, productName);
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

	List<Transaction> getTxnsWithItemsAboveThreshold(String productName, int thresholdQty) {
		JavaRDD<Product> products = productLines.map(this::convertToProduct);
		Product product = products.filter(p -> p.getName() != null).filter(p -> p.getName().equals(productName))
				.first();
		JavaRDD<Transaction> txns = txnLines.map(this::convertToTransaction);
		JavaRDD<Transaction> txnsWithItemsAboveThreshold = txns
				.filter(t -> t.getProductId().intValue() == product.getId().intValue())
				.filter(t -> t.getQuantity().intValue() >= thresholdQty);
		return txnsWithItemsAboveThreshold.collect();
	}

	Tuple2<Integer, BigDecimal> getCustIdWithMostSpent() {
		JavaRDD<Transaction> txns = txnLines.map(this::convertToTransaction);
		JavaPairRDD<Integer, BigDecimal> custIdTxnPricePairs = txns
				.mapToPair(txn -> new Tuple2<>(txn.getCustomerId(), txn.getAggrPrice()));
		JavaPairRDD<Integer, BigDecimal> custIdTotalPricePairs = custIdTxnPricePairs
				.reduceByKey((totalPrice, txnPrice) -> totalPrice.add(txnPrice));

		Comparator<Tuple2<Integer, BigDecimal>> priceComparator = new PriceComparator();
		return custIdTotalPricePairs.max(priceComparator);
	}

	private class TxnCountComparator implements Comparator<Tuple2<Integer, Integer>>, Serializable {

		private static final long serialVersionUID = 8599281137629438124L;

		@Override
		public int compare(Tuple2<Integer, Integer> custIdVsTxnCount1, Tuple2<Integer, Integer> custIdVsTxnCount2) {
			return custIdVsTxnCount1._2().intValue() - custIdVsTxnCount2._2().intValue();
		}
	}

	public class PriceComparator implements Comparator<Tuple2<Integer, BigDecimal>>, Serializable {

		private static final long serialVersionUID = 1654363779263606021L;

		@Override
		public int compare(Tuple2<Integer, BigDecimal> custIdVsPrice1, Tuple2<Integer, BigDecimal> custIdVsPrice2) {
			return custIdVsPrice1._2().compareTo(custIdVsPrice2._2());
		}
	}
}

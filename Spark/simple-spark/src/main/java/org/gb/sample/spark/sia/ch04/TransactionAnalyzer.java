package org.gb.sample.spark.sia.ch04;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

public class TransactionAnalyzer implements Serializable {

	private static final long serialVersionUID = 5738463725391572356L;
	private JavaRDD<Transaction> transactions;
	private JavaRDD<Product> products;

	TransactionAnalyzer(JavaRDD<Transaction> transactions, JavaRDD<Product> products) {
		super();
		this.transactions = transactions;
		this.products = products;
	}

	Tuple2<Integer, Integer> getCustIdWithMaxTxns() {
		JavaPairRDD<Integer, Transaction> custIdTxnPairs = transactions
				.mapToPair(txn -> new Tuple2<>(txn.getCustomerId(), txn));

		Comparator<Tuple2<Integer, Integer>> txnCountComparator = new TxnCountComparator();
		Tuple2<Integer, Integer> maxTxnsTuple = custIdTxnPairs
				.mapToPair(custIdTxnTuple -> new Tuple2<>(custIdTxnTuple._1(), 1))
				.foldByKey(0, (txnCount, oneTxn) -> txnCount + oneTxn).max(txnCountComparator);
		return maxTxnsTuple;
	}

	List<Transaction> getTxnsWithItemsAboveThreshold(String productName, int thresholdQty) {
		Product product = products.filter(p -> p.getName() != null).filter(p -> p.getName().equals(productName))
				.first();
		JavaRDD<Transaction> txnsWithItemsAboveThreshold = transactions
				.filter(t -> t.getProductId().intValue() == product.getId().intValue())
				.filter(t -> t.getQuantity().intValue() >= thresholdQty);
		return txnsWithItemsAboveThreshold.collect();
	}

	Tuple2<Integer, BigDecimal> getCustIdWithMostSpent() {
		JavaPairRDD<Integer, BigDecimal> custIdTxnPricePairs = transactions
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

	private class PriceComparator implements Comparator<Tuple2<Integer, BigDecimal>>, Serializable {

		private static final long serialVersionUID = 1654363779263606021L;

		@Override
		public int compare(Tuple2<Integer, BigDecimal> custIdVsPrice1, Tuple2<Integer, BigDecimal> custIdVsPrice2) {
			return custIdVsPrice1._2().compareTo(custIdVsPrice2._2());
		}
	}
}

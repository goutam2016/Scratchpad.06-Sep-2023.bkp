package org.gb.sample.spark.sia.ch04;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

public class RewardCalculator {

	private final Converter converter;
	private final TransactionAnalyzer txnAnalyzer;
	private JavaRDD<Transaction> transactions;
	private JavaRDD<Product> products;

	RewardCalculator(JavaRDD<String> txnLines, JavaRDD<String> productLines) {
		converter = Converter.getInstance();
		transactions = txnLines.map(converter::convertToTransaction);
		products = productLines.map(converter::convertToProduct);
		txnAnalyzer = new TransactionAnalyzer(transactions, products);
	}

	List<Transaction> calculateRewards() {
		/*
		 * Complimentary bear doll to the customer who made most transactions.
		 */
		Tuple2<Integer, Integer> custIdWithMaxTxns = txnAnalyzer.getCustIdWithMaxTxns();
		Product bearDoll = products.filter(p -> p.getName() != null).filter(p -> p.getName().equals("Bear doll"))
				.first();
		Transaction complBearDollTxn = buildComplimentaryTxn(custIdWithMaxTxns._1(), bearDoll.getId());

		/*
		 * 5% discount to transactions with 2 or more Barbie Shopping Mall Playset.
		 */
		List<Transaction> txnsWithBarbiePlaysetsAboveThreshold = txnAnalyzer
				.getTxnsWithItemsAboveThreshold("Barbie Shopping Mall Playset", 2);
		List<Transaction> discountedTxns = buildDiscountedTxns(txnsWithBarbiePlaysetsAboveThreshold, 0.05);

		/*
		 * Complimentary toothbrush to the customers who bought more than 5 dictionaries.
		 */
		List<Transaction> txnsWithDictionariesAboveThreshold = txnAnalyzer.getTxnsWithItemsAboveThreshold("Dictionary",
				5);
		Product toothbrush = products.filter(p -> p.getName() != null).filter(p -> p.getName().equals("Toothbrush"))
				.first();
		List<Transaction> complToothbrushTxns = txnsWithDictionariesAboveThreshold.stream()
				.map(txn -> buildComplimentaryTxn(txn.getCustomerId(), toothbrush.getId()))
				.collect(Collectors.toList());

		/*
		 * Complimentary pajamas to the customer who spent most overall.
		 */
		Product pajamas = products.filter(p -> p.getName() != null).filter(p -> p.getName().equals("Pajamas")).first();
		Tuple2<Integer, BigDecimal> custIdWithMostSpent = txnAnalyzer.getCustIdWithMostSpent();
		Transaction complPajamasTxn = buildComplimentaryTxn(custIdWithMostSpent._1(), pajamas.getId());

		List<Transaction> rewards = new ArrayList<>();
		rewards.add(complBearDollTxn);
		rewards.addAll(discountedTxns);
		rewards.addAll(complToothbrushTxns);
		rewards.add(complPajamasTxn);
		return rewards;
	}

	private Transaction buildComplimentaryTxn(Integer customerId, Integer productId) {
		Transaction bearDollTxn = new Transaction();
		bearDollTxn.setTxnDateTime(LocalDateTime.now());
		bearDollTxn.setCustomerId(customerId);
		bearDollTxn.setProductId(productId);
		bearDollTxn.setQuantity(1);
		bearDollTxn.setAggrPrice(BigDecimal.ZERO);
		return bearDollTxn;
	}

	private Transaction applyDiscount(Transaction txn, double discountRatio) {
		txn.setTxnDateTime(LocalDateTime.now());
		BigDecimal discount = txn.getAggrPrice().multiply(BigDecimal.valueOf(discountRatio)).negate();
		txn.setAggrPrice(discount);
		return txn;
	}

	private List<Transaction> buildDiscountedTxns(List<Transaction> eligibleTxns, double discountRatio) {
		Stream<Transaction> discountedTxns = eligibleTxns.stream().map(txn -> applyDiscount(txn, discountRatio));
		return discountedTxns.collect(Collectors.toList());
	}
}

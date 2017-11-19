package org.gb.sample.spark.sia.ch04;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class Converter implements Serializable {
	
	private static final long serialVersionUID = 3646605257162251213L;
	private static final Converter singleton = new Converter();
	
	private Converter() {

	}
	
	static Converter getInstance() {
		return singleton;
	}

	Transaction convertToTransaction(String txnLine) {
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
	
	Product convertToProduct(String productLine) {
		String[] productLineTokens = productLine.split("#");
		Integer productId = Integer.parseInt(productLineTokens[0]);
		String productName = productLineTokens[1];
		return new Product(productId, productName);
	}
}

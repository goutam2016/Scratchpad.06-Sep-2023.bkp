package org.gb.sample.spark.sia.ch04;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction implements Serializable {

	private static final long serialVersionUID = -1279726313695561533L;

	private LocalDateTime txnDateTime;
	private Integer customerId;
	private Integer productId;
	private Integer quantity;
	private BigDecimal aggrPrice;

	public LocalDateTime getTxnDateTime() {
		return txnDateTime;
	}

	void setTxnDateTime(LocalDateTime txnDateTime) {
		this.txnDateTime = txnDateTime;
	}

	Integer getCustomerId() {
		return customerId;
	}

	void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	Integer getProductId() {
		return productId;
	}

	void setProductId(Integer productId) {
		this.productId = productId;
	}

	Integer getQuantity() {
		return quantity;
	}

	void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	BigDecimal getAggrPrice() {
		return aggrPrice;
	}

	void setAggrPrice(BigDecimal aggrPrice) {
		this.aggrPrice = aggrPrice;
	}

	@Override
	public String toString() {
		String txnAsString = String.format("Customer-id: %d, Product-id: %d, Quantity: %d, Aggr. price: %s", customerId,
				productId, quantity, aggrPrice);
		return txnAsString;
	}
}

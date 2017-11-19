package org.gb.sample.spark.streaming.sia.ch06;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
	
	enum Direction {
		BUY,
		SELL;
	}
	
	private LocalDateTime ordDateTime;
	private Integer id;
	private Integer clientId;
	private String symbol;
	private Integer quantity;
	private BigDecimal price;
	private Direction direction;
	
	LocalDateTime getOrdDateTime() {
		return ordDateTime;
	}
	void setOrdDateTime(LocalDateTime ordDateTime) {
		this.ordDateTime = ordDateTime;
	}
	Integer getId() {
		return id;
	}
	void setId(Integer id) {
		this.id = id;
	}
	Integer getClientId() {
		return clientId;
	}
	void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	String getSymbol() {
		return symbol;
	}
	void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	Integer getQuantity() {
		return quantity;
	}
	void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	BigDecimal getPrice() {
		return price;
	}
	void setPrice(BigDecimal price) {
		this.price = price;
	}
	Direction getDirection() {
		return direction;
	}
	void setDirection(Direction direction) {
		this.direction = direction;
	}
}

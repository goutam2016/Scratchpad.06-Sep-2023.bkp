package org.gb.sample.spark.sia.ch04;

import java.io.Serializable;

public class Product implements Serializable {

	private static final long serialVersionUID = -1179225535926384775L;
	private Integer id;
	private String name;
	
	Product(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	Integer getId() {
		return id;
	}
	String getName() {
		return name;
	}
}

package org.gb.sample.cassandra;

import com.datastax.driver.mapping.annotations.UDT;

@UDT(keyspace = "mammals", name = "genus")
public class Genus {

	private String name;
	private String description;
	private String literalMeaning;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getLiteralMeaning() {
		return literalMeaning;
	}
	public void setLiteralMeaning(String literalMeaning) {
		this.literalMeaning = literalMeaning;
	}
}
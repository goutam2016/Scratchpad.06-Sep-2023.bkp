package org.gb.sample.spark.sql.income;

import java.io.Serializable;

public class PersonName implements Serializable {

	private static final long serialVersionUID = -6091937685687582003L;
	
	private String firstPart;
	private String lastPart;
	
	PersonName(String firstPart, String lastPart) {
		super();
		this.firstPart = firstPart;
		this.lastPart = lastPart;
	}
	
	String getFirstPart() {
		return firstPart;
	}
	String getLastPart() {
		return lastPart;
	}
	
	@Override
	public String toString() {
		return firstPart + " " + lastPart;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstPart == null) ? 0 : firstPart.hashCode());
		result = prime * result + ((lastPart == null) ? 0 : lastPart.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonName other = (PersonName) obj;
		if (firstPart == null) {
			if (other.firstPart != null)
				return false;
		} else if (!firstPart.equals(other.firstPart))
			return false;
		if (lastPart == null) {
			if (other.lastPart != null)
				return false;
		} else if (!lastPart.equals(other.lastPart))
			return false;
		return true;
	}
}

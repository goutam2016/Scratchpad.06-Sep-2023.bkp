package org.gb.sample.pig.income;

public class PersonName {

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

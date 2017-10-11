package org.gb.sample.spark.income;

import java.io.Serializable;

public class Band implements Serializable {

	private static final long serialVersionUID = 5293922981697936967L;

	private Integer lowerLimit;
	private Integer upperLimit;

	Band(Integer lowerLimit, Integer upperLimit) {
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
	}

	Integer getLowerLimit() {
		return lowerLimit;
	}

	Integer getUpperLimit() {
		return upperLimit;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(String.valueOf(lowerLimit.intValue()));
		if (upperLimit == null) {
			return sb.append("+").toString();
		} else {
			sb = sb.append(" - ").append(upperLimit.intValue());
			return sb.toString();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lowerLimit == null) ? 0 : lowerLimit.hashCode());
		result = prime * result + ((upperLimit == null) ? 0 : upperLimit.hashCode());
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
		Band other = (Band) obj;
		if (lowerLimit == null) {
			if (other.lowerLimit != null)
				return false;
		} else if (!lowerLimit.equals(other.lowerLimit))
			return false;
		if (upperLimit == null) {
			if (other.upperLimit != null)
				return false;
		} else if (!upperLimit.equals(other.upperLimit))
			return false;
		return true;
	}
}

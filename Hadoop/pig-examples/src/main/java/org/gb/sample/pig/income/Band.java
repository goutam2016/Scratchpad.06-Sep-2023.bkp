package org.gb.sample.pig.income;

public class Band {

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
}

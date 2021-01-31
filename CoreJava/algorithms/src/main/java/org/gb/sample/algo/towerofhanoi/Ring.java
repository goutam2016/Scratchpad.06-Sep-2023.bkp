package org.gb.sample.algo.towerofhanoi;

class Ring {

	private final int diameter;
	private Peg currentPeg;

	Ring(int diameter) {
		super();
		this.diameter = diameter;
	}

	int getDiameter() {
		return diameter;
	}
	
	Peg getCurrentPeg() {
		return currentPeg;
	}
	void setCurrentPeg(Peg currentPeg) {
		this.currentPeg = currentPeg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + diameter;
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
		Ring other = (Ring) obj;
		if (diameter != other.diameter)
			return false;
		return true;
	}
}

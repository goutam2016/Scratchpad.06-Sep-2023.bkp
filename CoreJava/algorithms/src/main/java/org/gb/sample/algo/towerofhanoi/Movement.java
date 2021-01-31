package org.gb.sample.algo.towerofhanoi;

class Movement {

	private final Ring ring;
	private final String fromPegName;
	private final String toPegName;

	Movement(Ring ring, String fromPegName, String toPegName) {
		super();
		this.ring = ring;
		this.fromPegName = fromPegName;
		this.toPegName = toPegName;
	}

	@Override
	public String toString() {
		return String.format("Ring %d moved from peg %s to peg %s.", ring.getDiameter(), fromPegName, toPegName);
	}
}

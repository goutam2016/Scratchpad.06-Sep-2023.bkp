package org.gb.sample.algo.towerofhanoi;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

class Peg {

	private final String name;
	private final Deque<Ring> rings;

	Peg(String name, int capacity) {
		this.name = name;
		rings = new LinkedBlockingDeque<>(capacity);
	}

	String getName() {
		return name;
	}

	List<Ring> getRingsTopToBottom() {
		return rings.stream().collect(Collectors.toList());
	}

	boolean addToTop(Ring ring) {
		Ring topmostRing = rings.peek();
		boolean added = false;
		
		if (topmostRing == null) {
			added = rings.offerFirst(ring);
		} else if (ring.getDiameter() > topmostRing.getDiameter()) {
			added = false;
		} else {
			added = rings.offerFirst(ring);
		}
		
		if(added) {
			ring.setCurrentPeg(this);
		}
		
		return added;
	}

	Ring removeFromTop() {
		Ring topmostRing = rings.pollFirst();
		return topmostRing;
	}

	@Override
	public String toString() {
		List<Integer> ringDiametersTopToBottom = rings.stream().map(Ring::getDiameter).collect(Collectors.toList());
		return String.format("Peg %s, depth: %d, ring diameters from top to bottom: %s.", name, rings.size(),
				ringDiametersTopToBottom);
	}
}

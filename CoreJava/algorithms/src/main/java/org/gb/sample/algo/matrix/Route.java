package org.gb.sample.algo.matrix;

import java.util.ArrayList;
import java.util.List;

public class Route {

	private List<Position> traversedPositions;

	public Route() {
		super();
		traversedPositions = new ArrayList<>();
	}

	public void pushTraversedPosition(Position traversedPosition) {
		traversedPositions.add(0, traversedPosition);
	}
}

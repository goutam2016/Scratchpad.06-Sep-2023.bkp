package org.gb.sample.algo.matrix;

public class Position {

	private final int rowLoc;
	private final int columnLoc;

	public Position(int rowLoc, int columnLoc) {
		super();
		this.rowLoc = rowLoc;
		this.columnLoc = columnLoc;
	}

	public int getRowLoc() {
		return rowLoc;
	}
	public int getColumnLoc() {
		return columnLoc;
	}
}

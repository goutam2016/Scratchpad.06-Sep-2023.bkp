package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

class Matrix2 {

	private final int rowCount;
	private final int columnCount;
	private final int[][] data;

	Matrix2(int rowCount, int columnCount) {
		super();
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.data = new int[rowCount][columnCount];
	}

	Matrix2(int rowCount, int columnCount, int[][] data) {
		super();
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.data = data;
	}

	int getRowCount() {
		return rowCount;
	}

	int getColumnCount() {
		return columnCount;
	}

	int[][] getData() {
		return data;
	}

	boolean isZeroDimensional() {
		return (rowCount * columnCount == 0);
	}

	void addData(int rowCount, int columnCount, int[][] data) {
		if (rowCount != this.rowCount || columnCount != this.columnCount) {
			throw new IllegalArgumentException(
					String.format("Dimensions of the supplied data array: [%d x %d] do not match with the dimensions of this matrix: [%d x %d].", rowCount,
							columnCount, this.rowCount, this.columnCount));
		}

		for (int i = 0; i < this.rowCount; i++) {
			for (int j = 0; j < this.columnCount; j++) {
				this.data[i][j] += data[i][j];
			}
		}
	}
}

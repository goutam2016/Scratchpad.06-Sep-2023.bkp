package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

class Matrix {

	private final String parenthesizedName;
	private final String simpleProductName;
	private final int rowCount;
	private final int columnCount;
	private final int cumulativeMultiplyCount;

	Matrix(String name, int rowCount, int columnCount) {
		this.parenthesizedName = name;
		this.simpleProductName = name;
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.cumulativeMultiplyCount = 0;
	}

	Matrix(Matrix leftPredecessor, Matrix rightPredecessor, int rowCount, int columnCount, int cumulativeMultiplyCount) {
		StringBuilder parenthesizedNameBuilder = new StringBuilder("(");
		this.parenthesizedName = parenthesizedNameBuilder.append(leftPredecessor.getParenthesizedName()).append(".")
				.append(rightPredecessor.getParenthesizedName()).append(")").toString();
		StringBuilder simpleProductNameBuilder = new StringBuilder(leftPredecessor.getSimpleProductName());
		this.simpleProductName = simpleProductNameBuilder.append(rightPredecessor.getSimpleProductName()).toString();
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.cumulativeMultiplyCount = cumulativeMultiplyCount;
	}

	String getParenthesizedName() {
		return parenthesizedName;
	}

	String getSimpleProductName() {
		return simpleProductName;
	}

	int getRowCount() {
		return rowCount;
	}

	int getColumnCount() {
		return columnCount;
	}

	int getCumulativeMultiplyCount() {
		return cumulativeMultiplyCount;
	}

	@Override
	public String toString() {
		String summaryInfo = "Name: %s, dimensions: %d x %d.";
		return String.format(summaryInfo, simpleProductName, rowCount, columnCount);
	}
}

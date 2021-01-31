package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

abstract class AbstractMatrixMultiplier implements MatrixMultiplier {
	
	@Override
	public int[][] multiply(int firstMatrixRowCount, int firstMatrixColumnCount, int secondMatrixRowCount, int secondMatrixColumnCount, int[][] firstMatrix,
			int[][] secondMatrix) {
		verifyDimensions(firstMatrixRowCount, firstMatrixColumnCount, secondMatrixRowCount, secondMatrixColumnCount, firstMatrix, secondMatrix);

		if (firstMatrixColumnCount != secondMatrixRowCount) {
			throw new IllegalArgumentException(
					"For multiplication compatibility, no. of columns of first matrix should be equal to the no. of rows of second matrix.");
		}

		return doMultiply(firstMatrixRowCount, firstMatrixColumnCount, secondMatrixRowCount, secondMatrixColumnCount, firstMatrix, secondMatrix);
	}

	void verifyDimensions(int firstMatrixRowCount, int firstMatrixColumnCount, int secondMatrixRowCount, int secondMatrixColumnCount,
			int[][] firstMatrix, int[][] secondMatrix) {
		if (firstMatrixRowCount <= 0 || firstMatrixColumnCount <= 0) {
			throw new IllegalArgumentException("First matrix must have at least 1 row and 1 column.");
		} else {
			boolean matchedDimensions = checkDimensions(firstMatrixRowCount, firstMatrixColumnCount, firstMatrix);

			if (!matchedDimensions) {
				throw new IllegalArgumentException("Dimensions of the first matrix do not match the specified row and column counts.");
			}
		}

		if (secondMatrixRowCount <= 0 || secondMatrixColumnCount <= 0) {
			throw new IllegalArgumentException("Second matrix must have at least 1 row and 1 column.");
		} else {
			boolean matchedDimensions = checkDimensions(secondMatrixRowCount, secondMatrixColumnCount, secondMatrix);

			if (!matchedDimensions) {
				throw new IllegalArgumentException("Dimensions of the second matrix do not match the specified row and column counts.");
			}
		}
	}

	private boolean checkDimensions(int rowCount, int columnCount, int[][] matrix) {
		boolean matchedDimensions = true;

		if (matrix.length != rowCount) {
			matchedDimensions = false;
		} else {
			for (int[] row : matrix) {
				if (row.length != columnCount) {
					matchedDimensions = false;
					break;
				}
			}
		}

		return matchedDimensions;
	}
	
	abstract int[][] doMultiply(int firstMatrixRowCount, int firstMatrixColumnCount, int secondMatrixRowCount, int secondMatrixColumnCount, int[][] firstMatrix,
			int[][] secondMatrix);
}

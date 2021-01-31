package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

abstract class AbstractSquareMatrixMultiplier extends AbstractMatrixMultiplier {

	@Override
	void verifyDimensions(int firstMatrixRowCount, int firstMatrixColumnCount, int secondMatrixRowCount, int secondMatrixColumnCount, int[][] firstMatrix,
			int[][] secondMatrix) {

		super.verifyDimensions(firstMatrixRowCount, firstMatrixColumnCount, secondMatrixRowCount, secondMatrixColumnCount, firstMatrix, secondMatrix);

		if (firstMatrixRowCount != firstMatrixColumnCount) {
			throw new IllegalArgumentException("The first matrix is not a square matrix, no. of rows and no. of columns are not equal.");
		}

		if (secondMatrixRowCount != secondMatrixColumnCount) {
			throw new IllegalArgumentException("The second matrix is not a square matrix, no. of rows and no. of columns are not equal.");
		}
	}
	
	@Override
	int[][] doMultiply(int firstMatrixRowCount, int firstMatrixColumnCount, int secondMatrixRowCount, int secondMatrixColumnCount, int[][] firstMatrix,
			int[][] secondMatrix) {
		//As both are square matrices, no. of rows and no. of columns of both will be same.
		int dimension = firstMatrixRowCount;
		return multiplySquareMatrices(dimension, firstMatrix, secondMatrix);
	}

	abstract int[][] multiplySquareMatrices(int dimension, int[][] firstMatrix, int[][] secondMatrix);
}

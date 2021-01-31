package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

class SerialSquareMatrixMultiplier extends AbstractSquareMatrixMultiplier {

	/*
	 * i --> stands for rowIdx of firstMatrix
	 * j --> stands for columnIdx of secondMatrix
	 * k --> stands for columnIdx of firstMatrix = rowIdx of secondMatrix
	 */
	@Override
	int[][] multiplySquareMatrices(final int dimension, final int[][] firstMatrix, final int[][] secondMatrix) {
		int[][] productMatrix = new int[dimension][dimension];

		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				for (int k = 0; k < dimension; k++) {
					productMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}

		return productMatrix;
	}
}

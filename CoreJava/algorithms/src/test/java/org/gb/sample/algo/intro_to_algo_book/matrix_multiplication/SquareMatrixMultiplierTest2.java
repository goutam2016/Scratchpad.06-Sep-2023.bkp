package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class SquareMatrixMultiplierTest2 {

	private static final Logger LOGGER = Logger.getLogger(SquareMatrixMultiplierTest2.class);

	private void printMatrix(double[][] matrix) {
		int rowCnt = matrix.length;

		for (int i = 0; i < rowCnt; i++) {
			double[] row = matrix[i];
			int columnCnt = row.length;
			StringBuilder rowFormatter = new StringBuilder();

			for (int j = 0; j < columnCnt; j++) {
				double cellValue = row[j];
				String cellValueAsStr = String.format("%.2f", cellValue);
				rowFormatter.append(cellValueAsStr).append(" ");
			}

			LOGGER.info(rowFormatter.substring(0));
		}
	}

	private double[][] multiplyMatrices(final int firstMatrixRowCount, final int firstMatrixColumnCount, final int secondMatrixRowCount, final int secondMatrixColumnCount, 
			final double[][] firstMatrix, final double[][] secondMatrix) {
		double[][] productMatrix = new double[firstMatrixRowCount][secondMatrixColumnCount];

		for (int i = 0; i < firstMatrixRowCount; i++) {
			for (int j = 0; j < secondMatrixColumnCount; j++) {
				for (int k = 0; k < firstMatrixColumnCount; k++) {
					productMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}

		return productMatrix;
	}
	
	@Test
	public void runMatrixMultiplication1() {
		// Prepare test data
		final int firstMatrixRowCount = 3; 
		final int firstMatrixColumnCount = 3;
		final int secondMatrixRowCount = 3;
		final int secondMatrixColumnCount = 3;

		final double[][] firstMatrix = new double[firstMatrixRowCount][firstMatrixColumnCount];
		final double[][] secondMatrix = new double[secondMatrixRowCount][secondMatrixColumnCount];

		firstMatrix[0][0] = 3;
		firstMatrix[0][1] = 0;
		firstMatrix[0][2] = 2;
		firstMatrix[1][0] = 2;
		firstMatrix[1][1] = 0;
		firstMatrix[1][2] = -2;
		firstMatrix[2][0] = 0;
		firstMatrix[2][1] = 1;
		firstMatrix[2][2] = 1;

		secondMatrix[0][0] = 0.2;
		secondMatrix[0][1] = 0.2;
		secondMatrix[0][2] = 0;
		secondMatrix[1][0] = -0.2;
		secondMatrix[1][1] = 0.3;
		secondMatrix[1][2] = 1;
		secondMatrix[2][0] = 0.2;
		secondMatrix[2][1] = -0.3;
		secondMatrix[2][2] = 0;

		LOGGER.info("1st matrix");
		printMatrix(firstMatrix);

		LOGGER.info("2nd matrix");
		printMatrix(secondMatrix);
		
		LOGGER.info("Starting multiplication -----------------------------------------\n");

		// Setup expectations
		double[][] productMatrix = multiplyMatrices(firstMatrixRowCount, firstMatrixColumnCount, secondMatrixRowCount, secondMatrixColumnCount, firstMatrix, secondMatrix);

		printMatrix(productMatrix);
	}
	
	@Test
	public void runMatrixMultiplication2() {
		// Prepare test data
		final int firstMatrixRowCount = 3; 
		final int firstMatrixColumnCount = 3;
		final int secondMatrixRowCount = 3;
		final int secondMatrixColumnCount = 3;

		final double[][] firstMatrix = new double[firstMatrixRowCount][firstMatrixColumnCount];
		final double[][] secondMatrix = new double[secondMatrixRowCount][secondMatrixColumnCount];

		firstMatrix[0][0] = 1;
		firstMatrix[0][1] = 2;
		firstMatrix[0][2] = 3;
		firstMatrix[1][0] = 0;
		firstMatrix[1][1] = 1;
		firstMatrix[1][2] = 5;
		firstMatrix[2][0] = 5;
		firstMatrix[2][1] = 6;
		firstMatrix[2][2] = 0;

		secondMatrix[0][0] = -6;
		secondMatrix[0][1] = 3.6;
		secondMatrix[0][2] = 1.4;
		secondMatrix[1][0] = 5;
		secondMatrix[1][1] = -3;
		secondMatrix[1][2] = -1;
		secondMatrix[2][0] = -1;
		secondMatrix[2][1] = 0.8;
		secondMatrix[2][2] = 0.2;

		LOGGER.info("1st matrix");
		printMatrix(firstMatrix);

		LOGGER.info("2nd matrix");
		printMatrix(secondMatrix);
		
		LOGGER.info("Starting multiplication -----------------------------------------\n");

		// Setup expectations
		double[][] productMatrix = multiplyMatrices(firstMatrixRowCount, firstMatrixColumnCount, secondMatrixRowCount, secondMatrixColumnCount, firstMatrix, secondMatrix);

		printMatrix(productMatrix);
	}

	@Test
	public void runMatrixMultiplication3() {
		// Prepare test data
		final int firstMatrixRowCount = 4; 
		final int firstMatrixColumnCount = 4;
		final int secondMatrixRowCount = 4;
		final int secondMatrixColumnCount = 4;

		final double[][] firstMatrix = new double[firstMatrixRowCount][firstMatrixColumnCount];
		final double[][] secondMatrix = new double[secondMatrixRowCount][secondMatrixColumnCount];

		firstMatrix[0][0] = 1;
		firstMatrix[0][1] = 0;
		firstMatrix[0][2] = 0;
		firstMatrix[0][3] = 0;
		firstMatrix[1][0] = 3;
		firstMatrix[1][1] = 1;
		firstMatrix[1][2] = 0;
		firstMatrix[1][3] = 0;
		firstMatrix[2][0] = 1;
		firstMatrix[2][1] = 4;
		firstMatrix[2][2] = 1;
		firstMatrix[2][3] = 0;
		firstMatrix[3][0] = 2;
		firstMatrix[3][1] = 1;
		firstMatrix[3][2] = 7;
		firstMatrix[3][3] = 1;

		secondMatrix[0][0] = 2;
		secondMatrix[0][1] = 3;
		secondMatrix[0][2] = 1;
		secondMatrix[0][3] = 5;
		secondMatrix[1][0] = 0;
		secondMatrix[1][1] = 4;
		secondMatrix[1][2] = 2;
		secondMatrix[1][3] = 4;
		secondMatrix[2][0] = 0;
		secondMatrix[2][1] = 0;
		secondMatrix[2][2] = 1;
		secondMatrix[2][3] = 2;
		secondMatrix[3][0] = 0;
		secondMatrix[3][1] = 0;
		secondMatrix[3][2] = 0;
		secondMatrix[3][3] = 3;

		LOGGER.info("1st matrix");
		printMatrix(firstMatrix);

		LOGGER.info("2nd matrix");
		printMatrix(secondMatrix);
		
		LOGGER.info("Starting multiplication -----------------------------------------\n");

		// Setup expectations
		double[][] productMatrix = multiplyMatrices(firstMatrixRowCount, firstMatrixColumnCount, secondMatrixRowCount, secondMatrixColumnCount, firstMatrix, secondMatrix);

		printMatrix(productMatrix);
	}
}

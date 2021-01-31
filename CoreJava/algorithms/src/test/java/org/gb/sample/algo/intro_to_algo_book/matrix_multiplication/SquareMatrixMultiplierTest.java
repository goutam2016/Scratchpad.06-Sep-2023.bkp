package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SquareMatrixMultiplierTest {

	private static final Logger LOGGER = Logger.getLogger(SquareMatrixMultiplierTest.class);

	private void printMatrix(int[][] matrix) {
		int rowCnt = matrix.length;

		for (int i = 0; i < rowCnt; i++) {
			int[] row = matrix[i];
			int columnCnt = row.length;
			StringBuilder rowFormatter = new StringBuilder();

			for (int j = 0; j < columnCnt; j++) {
				int cellValue = row[j];
				rowFormatter.append(cellValue).append(" ");
			}

			LOGGER.info(rowFormatter.substring(0));
		}
	}

	private void printProductMatrix(String matrixMultiplierName, int[][] productMatrix) {
		LOGGER.info(String.format("Computed by: %s", matrixMultiplierName));
		printMatrix(productMatrix);
	}

	private int[][] multiplySquareMatrices(final int dimension, final int[][] firstMatrix, final int[][] secondMatrix) {
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
	
	private int[][] multiplyMatrices(final int firstMatrixRowCount, final int firstMatrixColumnCount, final int secondMatrixRowCount, final int secondMatrixColumnCount, 
			final int[][] firstMatrix, final int[][] secondMatrix) {
		int[][] productMatrix = new int[firstMatrixRowCount][secondMatrixColumnCount];

		for (int i = 0; i < firstMatrixRowCount; i++) {
			for (int j = 0; j < secondMatrixColumnCount; j++) {
				for (int k = 0; k < firstMatrixColumnCount; k++) {
					productMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}

		return productMatrix;
	}

	private void runMatrixMultiplication(MatrixMultiplier matrixMultiplier) {
		// Prepare test data
		final int squareMatrixDimension = 11;
		final int rowCount = squareMatrixDimension;
		final int columnCount = squareMatrixDimension;

		final int[][] firstMatrix = new int[rowCount][columnCount];
		final int[][] secondMatrix = new int[rowCount][columnCount];

		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				firstMatrix[i][j] = 1; //(rowCount * i) + j + 1;
				secondMatrix[i][j] = 1; //(rowCount * i) + j + 1 + (squareMatrixDimension * squareMatrixDimension);
			}
		}

		LOGGER.info("1st matrix");
		printMatrix(firstMatrix);

		LOGGER.info("2nd matrix");
		printMatrix(secondMatrix);
		
		LOGGER.info("Starting multiplication -----------------------------------------\n");

		// Setup expectations
		int[][] exptdProductMatrix = multiplySquareMatrices(squareMatrixDimension, firstMatrix, secondMatrix);

		// Invoke test target
		int[][] actualProductMatrix = matrixMultiplier.multiply(rowCount, columnCount, rowCount, columnCount, firstMatrix, secondMatrix);

		// Verify results
		printProductMatrix(matrixMultiplier.getClass().getSimpleName(), actualProductMatrix);
		//Assert.assertArrayEquals(exptdProductMatrix, actualProductMatrix);
	}
	
	@Test
	public void runMatrixMultiplication_temp() {
		// Prepare test data
		final int firstMatrixRowCount = 3; 
		final int firstMatrixColumnCount = 3;
		final int secondMatrixRowCount = 3;
		final int secondMatrixColumnCount = 3;

		final int[][] firstMatrix = new int[firstMatrixRowCount][firstMatrixColumnCount];
		final int[][] secondMatrix = new int[secondMatrixRowCount][secondMatrixColumnCount];

		firstMatrix[0][0] = 1;
		firstMatrix[0][1] = 1;
		firstMatrix[0][2] = 1;
		firstMatrix[1][0] = 1;
		firstMatrix[1][1] = 1;
		firstMatrix[1][2] = 1;
		firstMatrix[0][0] = 1;
		firstMatrix[0][1] = 1;
		firstMatrix[0][2] = 1;

		secondMatrix[0][0] = 1;
		secondMatrix[0][1] = 1;

		LOGGER.info("1st matrix");
		printMatrix(firstMatrix);

		LOGGER.info("2nd matrix");
		printMatrix(secondMatrix);
		
		LOGGER.info("Starting multiplication -----------------------------------------\n");

		// Setup expectations
		int[][] productMatrix = multiplyMatrices(firstMatrixRowCount, firstMatrixColumnCount, secondMatrixRowCount, secondMatrixColumnCount, firstMatrix, secondMatrix);

		printMatrix(productMatrix);
	}

	@Test
	public void multiply_with_SerialSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialSquareMatrixMultiplier();
		runMatrixMultiplication(matrixMultiplier);
	}

	@Test
	public void multiply_with_SerialRecursiveSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialRecursiveSquareMatrixMultiplier();
		runMatrixMultiplication(matrixMultiplier);
	}

	@Test
	public void multiply_with_SerialRecursiveArrayCopySquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialRecursiveArrayCopySquareMatrixMultiplier();
		runMatrixMultiplication(matrixMultiplier);
	}
}

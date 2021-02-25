package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SquareMatrixMultiplierTest {

	private static final Logger LOGGER = Logger.getLogger(SquareMatrixMultiplierTest.class);

	private static final int SQR_MATRIX_DIM5 = 5;
	private static final int SQR_MATRIX_DIM16 = 16;
	private static final int SQR_MATRIX_DIM18 = 18;
	private static final int SQR_MATRIX_DIM23 = 23;
	private static final int[][] sqrMatrix_5x5 = new int[SQR_MATRIX_DIM5][SQR_MATRIX_DIM5];
	private static final int[][] pwrOf2SqrMatrix_16x16 = new int[SQR_MATRIX_DIM16][SQR_MATRIX_DIM16];
	private static final int[][] sqrMatrix_18x18 = new int[SQR_MATRIX_DIM18][SQR_MATRIX_DIM18];
	private static final int[][] sqrMatrix_23x23 = new int[SQR_MATRIX_DIM23][SQR_MATRIX_DIM23];
	private static final int[][] sqrMatrix_5x5_Product = new int[SQR_MATRIX_DIM5][SQR_MATRIX_DIM5];
	private static final int[][] pwrOf2SqrMatrix_16x16_Product = new int[SQR_MATRIX_DIM16][SQR_MATRIX_DIM16];
	private static final int[][] sqrMatrix_18x18_Product = new int[SQR_MATRIX_DIM18][SQR_MATRIX_DIM18];
	private static final int[][] sqrMatrix_23x23_Product = new int[SQR_MATRIX_DIM23][SQR_MATRIX_DIM23];

	private static void fillMatrixData(int[][] matrixData, int dimension) {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				matrixData[i][j] = i;
			}
		}
	}

	private static void fillMatrixData(int[][] matrixData, int dimension, int cellValue) {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				matrixData[i][j] = cellValue;
			}
		}
	}

	private static void multiplySquareMatrices(final int dimension, final int[][] firstMatrix, final int[][] secondMatrix, final int[][] productMatrix) {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				for (int k = 0; k < dimension; k++) {
					productMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}
	}

	@BeforeClass
	public static void setupForAll() {
		fillMatrixData(sqrMatrix_5x5, SQR_MATRIX_DIM5, 1);
		fillMatrixData(pwrOf2SqrMatrix_16x16, SQR_MATRIX_DIM16);
		fillMatrixData(sqrMatrix_18x18, SQR_MATRIX_DIM18, 1);
		fillMatrixData(sqrMatrix_23x23, SQR_MATRIX_DIM23);

		multiplySquareMatrices(SQR_MATRIX_DIM5, sqrMatrix_5x5, sqrMatrix_5x5, sqrMatrix_5x5_Product);
		multiplySquareMatrices(SQR_MATRIX_DIM16, pwrOf2SqrMatrix_16x16, pwrOf2SqrMatrix_16x16, pwrOf2SqrMatrix_16x16_Product);
		multiplySquareMatrices(SQR_MATRIX_DIM18, sqrMatrix_18x18, sqrMatrix_18x18, sqrMatrix_18x18_Product);
		multiplySquareMatrices(SQR_MATRIX_DIM23, sqrMatrix_23x23, sqrMatrix_23x23, sqrMatrix_23x23_Product);
	}

	private void printMatrixData(int[][] matrix) {
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

	@Test
	public void multiply_5x5_with_SerialSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialSquareMatrixMultiplier();
		int[][] productMatrixData = matrixMultiplier.multiply(SQR_MATRIX_DIM5, SQR_MATRIX_DIM5, SQR_MATRIX_DIM5, SQR_MATRIX_DIM5, sqrMatrix_5x5, sqrMatrix_5x5);
		LOGGER.info("Expected product matrix:");
		printMatrixData(sqrMatrix_5x5_Product);
		LOGGER.info("Actual product matrix:");
		printMatrixData(productMatrixData);
		Assert.assertArrayEquals(sqrMatrix_5x5_Product, productMatrixData);
	}

	@Test
	public void multiply_16x16_with_SerialSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialSquareMatrixMultiplier();
		int[][] productMatrixData = matrixMultiplier.multiply(SQR_MATRIX_DIM16, SQR_MATRIX_DIM16, SQR_MATRIX_DIM16, SQR_MATRIX_DIM16, pwrOf2SqrMatrix_16x16,
				pwrOf2SqrMatrix_16x16);
		Assert.assertArrayEquals(pwrOf2SqrMatrix_16x16_Product, productMatrixData);
	}

	@Test
	public void multiply_18x18_with_SerialSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialSquareMatrixMultiplier();
		int[][] productMatrixData = matrixMultiplier.multiply(SQR_MATRIX_DIM18, SQR_MATRIX_DIM18, SQR_MATRIX_DIM18, SQR_MATRIX_DIM18, sqrMatrix_18x18,
				sqrMatrix_18x18);
		LOGGER.info("Expected product matrix:");
		printMatrixData(sqrMatrix_18x18_Product);
		LOGGER.info("Actual product matrix:");
		printMatrixData(productMatrixData);
		Assert.assertArrayEquals(sqrMatrix_18x18_Product, productMatrixData);
	}

	@Test
	public void multiply_23x23_with_SerialSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialSquareMatrixMultiplier();
		int[][] productMatrixData = matrixMultiplier.multiply(SQR_MATRIX_DIM23, SQR_MATRIX_DIM23, SQR_MATRIX_DIM23, SQR_MATRIX_DIM23, sqrMatrix_23x23,
				sqrMatrix_23x23);
		Assert.assertArrayEquals(sqrMatrix_23x23_Product, productMatrixData);
	}

	@Test
	public void multiply_16x16_with_SerialRecursivePowerOf2SquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialRecursivePowerOf2SquareMatrixMultiplier();
		int[][] productMatrixData = matrixMultiplier.multiply(SQR_MATRIX_DIM16, SQR_MATRIX_DIM16, SQR_MATRIX_DIM16, SQR_MATRIX_DIM16, pwrOf2SqrMatrix_16x16,
				pwrOf2SqrMatrix_16x16);
		Assert.assertArrayEquals(pwrOf2SqrMatrix_16x16_Product, productMatrixData);
	}

	@Test
	public void multiply_5x5_with_SerialRecursiveSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialRecursiveSquareMatrixMultiplier();
		int[][] productMatrixData = matrixMultiplier.multiply(SQR_MATRIX_DIM5, SQR_MATRIX_DIM5, SQR_MATRIX_DIM5, SQR_MATRIX_DIM5, sqrMatrix_5x5, sqrMatrix_5x5);
		Assert.assertArrayEquals(sqrMatrix_5x5_Product, productMatrixData);
	}

	@Test
	public void multiply_16x16_with_SerialRecursiveSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialSquareMatrixMultiplier();
		int[][] productMatrixData = matrixMultiplier.multiply(SQR_MATRIX_DIM16, SQR_MATRIX_DIM16, SQR_MATRIX_DIM16, SQR_MATRIX_DIM16, pwrOf2SqrMatrix_16x16,
				pwrOf2SqrMatrix_16x16);
		Assert.assertArrayEquals(pwrOf2SqrMatrix_16x16_Product, productMatrixData);
	}

	@Test
	public void multiply_18x18_with_SerialRecursiveSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialSquareMatrixMultiplier();
		int[][] productMatrixData = matrixMultiplier.multiply(SQR_MATRIX_DIM18, SQR_MATRIX_DIM18, SQR_MATRIX_DIM18, SQR_MATRIX_DIM18, sqrMatrix_18x18,
				sqrMatrix_18x18);
		LOGGER.info("Expected product matrix:");
		printMatrixData(sqrMatrix_18x18_Product);
		LOGGER.info("Actual product matrix:");
		printMatrixData(productMatrixData);
		Assert.assertArrayEquals(sqrMatrix_18x18_Product, productMatrixData);
	}

	@Test
	public void multiply_23x23_with_SerialRecursiveSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialSquareMatrixMultiplier();
		int[][] productMatrixData = matrixMultiplier.multiply(SQR_MATRIX_DIM23, SQR_MATRIX_DIM23, SQR_MATRIX_DIM23, SQR_MATRIX_DIM23, sqrMatrix_23x23,
				sqrMatrix_23x23);
		LOGGER.info("Expected product matrix:");
		printMatrixData(sqrMatrix_23x23_Product);
		LOGGER.info("Actual product matrix:");
		printMatrixData(productMatrixData);
		Assert.assertArrayEquals(sqrMatrix_23x23_Product, productMatrixData);
	}
}

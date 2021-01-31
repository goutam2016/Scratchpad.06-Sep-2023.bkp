package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MatrixChainMultiplierTest {

	private static final Logger LOGGER = Logger.getLogger(MatrixChainMultiplierTest.class);

	private static List<Matrix> threeMatricesChain;
	private static List<Matrix> sixMatricesChain;
	private static List<Matrix> tenMatricesChain;

	private static void initThreeMatricesChain() {
		Matrix a = new Matrix("A", 10, 100);
		Matrix b = new Matrix("B", 100, 5);
		Matrix c = new Matrix("C", 5, 50);

		threeMatricesChain = List.of(a, b, c);
	}

	private static void initSixMatricesChain() {
		Matrix a = new Matrix("A", 30, 35);
		Matrix b = new Matrix("B", 35, 15);
		Matrix c = new Matrix("C", 15, 5);
		Matrix d = new Matrix("D", 5, 10);
		Matrix e = new Matrix("E", 10, 20);
		Matrix f = new Matrix("F", 20, 25);

		sixMatricesChain = List.of(a, b, c, d, e, f);
	}

	private static void initTenMatricesChain() {
		Matrix a = new Matrix("A", 40, 20);
		Matrix b = new Matrix("B", 20, 30);
		Matrix c = new Matrix("C", 30, 10);
		Matrix d = new Matrix("D", 10, 30);
		Matrix e = new Matrix("E", 30, 52);
		Matrix f = new Matrix("F", 52, 48);
		Matrix g = new Matrix("G", 48, 71);
		Matrix h = new Matrix("H", 71, 80);
		Matrix i = new Matrix("I", 80, 57);
		Matrix j = new Matrix("J", 57, 11);

		tenMatricesChain = List.of(a, b, c, d, e, f, g, h, i, j);
	}

	@BeforeClass
	public static void setupForAll() {
		initThreeMatricesChain();
		initSixMatricesChain();
		initTenMatricesChain();
	}

	private void computeOptimalOrder(final List<Matrix> matricesChain, final MatrixChainMultiplier matrixChainMultiplier, final int exptdMinMultiplyCount,
			final String exptdOptimalParenthesization) {
		// Invoke test target
		Matrix product = matrixChainMultiplier.computeOptimalOrder(matricesChain);

		// Verify results
		LOGGER.info(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
				product.getCumulativeMultiplyCount()));
		Assert.assertEquals(exptdMinMultiplyCount, product.getCumulativeMultiplyCount());
		Assert.assertEquals(exptdOptimalParenthesization, product.getParenthesizedName());
	}

	private void computeOptimalOrder_3Matrices(final MatrixChainMultiplier matrixChainMultiplier) {
		// Setup expectations
		final int exptdMinMultiplyCount = 7500;
		final String exptdOptimalParenthesization = "((A.B).C)";

		computeOptimalOrder(threeMatricesChain, matrixChainMultiplier, exptdMinMultiplyCount, exptdOptimalParenthesization);
	}

	private void computeOptimalOrder_6Matrices(final MatrixChainMultiplier matrixChainMultiplier) {
		// Setup expectations
		final int exptdMinMultiplyCount = 15125;
		final String exptdOptimalParenthesization = "((A.(B.C)).((D.E).F))";

		computeOptimalOrder(sixMatricesChain, matrixChainMultiplier, exptdMinMultiplyCount, exptdOptimalParenthesization);
	}

	private void computeOptimalOrder_10Matrices(final MatrixChainMultiplier matrixChainMultiplier) {
		// Setup expectations
		final int exptdMinMultiplyCount = 200310;
		final String exptdOptimalParenthesization = "(A.((B.C).((((((D.E).F).G).H).I).J)))";

		computeOptimalOrder(tenMatricesChain, matrixChainMultiplier, exptdMinMultiplyCount, exptdOptimalParenthesization);
	}

	/************************		3 matrices chain multiplication		****************************/
	
	@Test
	public void computeOptimalOrder_SerialRecursiveMultiplier_3Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveMatrixChainMultiplier();
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialBottomUpMultiplier_3Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialBottomUpMatrixChainMultiplier();
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveMapMemoizedMultiplier_3Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveMapMemoizedMatrixChainMultiplier();
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveArrayMemoizedMultiplier_3Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveArrayMemoizedMatrixChainMultiplier();
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJMapMemoizedMultiplier_3Matrices() {
		final int parallelism = 2;
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getFJMapMemoizedMatrixChainMultiplier(parallelism);
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJArrayMemoizedMultiplier_3Matrices() {
		final int parallelism = 2;
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getFJArrayMemoizedMatrixChainMultiplier(parallelism);
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	/************************		6 matrices chain multiplication		****************************/
	
	@Test
	public void computeOptimalOrder_SerialRecursiveMultiplier_6Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveMatrixChainMultiplier();
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialBottomUpMultiplier_6Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialBottomUpMatrixChainMultiplier();
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveMapMemoizedMultiplier_6Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveMapMemoizedMatrixChainMultiplier();
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveArrayMemoizedMultiplier_6Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveArrayMemoizedMatrixChainMultiplier();
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJMapMemoizedMultiplier_6Matrices() {
		final int parallelism = 2;
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getFJMapMemoizedMatrixChainMultiplier(parallelism);
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJArrayMemoizedMultiplier_6Matrices() {
		final int parallelism = 2;
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getFJArrayMemoizedMatrixChainMultiplier(parallelism);
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	/************************		10 matrices chain multiplication		****************************/
	
	@Test
	public void computeOptimalOrder_SerialRecursiveMultiplier_10Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveMatrixChainMultiplier();
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialBottomUpMultiplier_10Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialBottomUpMatrixChainMultiplier();
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveMapMemoizedMultiplier_10Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveMapMemoizedMatrixChainMultiplier();
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveArrayMemoizedMultiplier_10Matrices() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveArrayMemoizedMatrixChainMultiplier();
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJMapMemoizedMultiplier_10Matrices() {
		final int parallelism = 2;
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getFJMapMemoizedMatrixChainMultiplier(parallelism);
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}
	
	@Test
	public void computeOptimalOrder_FJArrayMemoizedMultiplier_10Matrices() {
		final int parallelism = 2;
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getFJArrayMemoizedMatrixChainMultiplier(parallelism);
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}
}

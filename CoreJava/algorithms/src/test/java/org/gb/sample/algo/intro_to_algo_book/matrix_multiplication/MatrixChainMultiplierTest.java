package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class MatrixChainMultiplierTest {

	private static final Logger LOGGER = Logger.getLogger(MatrixChainMultiplierTest.class);

	private static List<Matrix> threeMatricesChain;
	private static List<Matrix> sixMatricesChain;
	private static List<Matrix> tenMatricesChain;
	private static List<Matrix> twentyMatricesChain;

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

	private static void initTwentyMatricesChain() {
		Matrix a = new Matrix("A", 40, 20);
		Matrix b = new Matrix("B", 20, 30);
		Matrix c = new Matrix("C", 30, 10);
		Matrix d = new Matrix("D", 10, 30);
		Matrix e = new Matrix("E", 30, 52);
		Matrix f = new Matrix("F", 52, 48);
		Matrix g = new Matrix("G", 48, 71);
		Matrix h = new Matrix("H", 71, 80);
		Matrix i = new Matrix("I", 80, 57);
		Matrix j = new Matrix("J", 57, 57);
		Matrix k = new Matrix("K", 57, 99);
		Matrix l = new Matrix("L", 99, 38);
		Matrix m = new Matrix("M", 38, 77);
		Matrix n = new Matrix("N", 77, 81);
		Matrix o = new Matrix("O", 81, 25);
		Matrix p = new Matrix("P", 25, 65);
		Matrix q = new Matrix("Q", 65, 66);
		Matrix r = new Matrix("R", 66, 59);
		Matrix s = new Matrix("S", 59, 88);
		Matrix t = new Matrix("T", 88, 54);

		twentyMatricesChain = List.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
	}

	@BeforeClass
	public static void setupForAll() {
		initThreeMatricesChain();
		initSixMatricesChain();
		initTenMatricesChain();
		initTwentyMatricesChain();
	}

	private void computeOptimalOrder(final List<Matrix> matricesChain, final MatrixChainMultiplicationOptimiser matrixChainMultiplier, final int exptdMinMultiplyCount,
			final String exptdOptimalParenthesization) {
		String implName = matrixChainMultiplier.getClass().getSimpleName();
		// Invoke test target
		Matrix product = matrixChainMultiplier.computeOptimalOrder(matricesChain);

		// Verify results
		LOGGER.info(String.format("Using implementation: %s, optimal parenthesization: %s, no. of multiplications: %d.", implName,
				product.getParenthesizedName(), product.getCumulativeMultiplyCount()));
		Assert.assertEquals(exptdMinMultiplyCount, product.getCumulativeMultiplyCount());
		Assert.assertEquals(exptdOptimalParenthesization, product.getParenthesizedName());
	}

	private void computeOptimalOrder_3Matrices(final MatrixChainMultiplicationOptimiser matrixChainMultiplier) {
		// Setup expectations
		final int exptdMinMultiplyCount = 7500;
		final String exptdOptimalParenthesization = "((A.B).C)";

		computeOptimalOrder(threeMatricesChain, matrixChainMultiplier, exptdMinMultiplyCount, exptdOptimalParenthesization);
	}

	private void computeOptimalOrder_6Matrices(final MatrixChainMultiplicationOptimiser matrixChainMultiplier) {
		// Setup expectations
		final int exptdMinMultiplyCount = 15125;
		final String exptdOptimalParenthesization = "((A.(B.C)).((D.E).F))";

		computeOptimalOrder(sixMatricesChain, matrixChainMultiplier, exptdMinMultiplyCount, exptdOptimalParenthesization);
	}

	private void computeOptimalOrder_10Matrices(final MatrixChainMultiplicationOptimiser matrixChainMultiplier) {
		// Setup expectations
		final int exptdMinMultiplyCount = 200310;
		final String exptdOptimalParenthesization = "(A.((B.C).((((((D.E).F).G).H).I).J)))";

		computeOptimalOrder(tenMatricesChain, matrixChainMultiplier, exptdMinMultiplyCount, exptdOptimalParenthesization);
	}

	private void computeOptimalOrder_20Matrices(final MatrixChainMultiplicationOptimiser matrixChainMultiplier) {
		// Setup expectations
		final int exptdMinMultiplyCount = 648590;
		final String exptdOptimalParenthesization = "((A.(B.C)).((((((((((((((((D.E).F).G).H).I).J).K).L).M).N).O).P).Q).R).S).T))";

		computeOptimalOrder(twentyMatricesChain, matrixChainMultiplier, exptdMinMultiplyCount, exptdOptimalParenthesization);
	}

	/*************************
	 * 3 matrices chain multiplication
	 ****************************/

	@Test
	public void computeOptimalOrder_SerialRecursiveMultiplier_3Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveMCMOptimiser();
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	@Ignore
	@Test
	public void computeOptimalOrder_SerialTailRecursiveMultiplier_3Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialTailRecursiveMCMOptimiser();
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialBottomUpMultiplier_3Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialBottomUpMCMOptimiser();
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveMapMemoizedMultiplier_3Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveMapMemoizedMCMOptimiser();
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveArrayMemoizedMultiplier_3Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveArrayMemoizedMCMOptimiser();
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}
	
	@Test
	public void computeOptimalOrder_ParallelBottomUpMultiplier_3Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getParallelBottomUpMCMOptimiser();
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}
	
	@Test
	public void computeOptimalOrder_FJMapMemoizedMultiplier_3Matrices() {
		final int parallelism = 2;
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJMapMemoizedMCMOptimiser(parallelism);
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJArrayMemoizedMultiplier_3Matrices() {
		final int parallelism = 2;
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJArrayMemoizedMCMOptimiser(parallelism);
		computeOptimalOrder_3Matrices(matrixChainMultiplier);
	}

	/*************************
	 * 6 matrices chain multiplication
	 ****************************/

	@Test
	public void computeOptimalOrder_SerialRecursiveMultiplier_6Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveMCMOptimiser();
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialBottomUpMultiplier_6Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialBottomUpMCMOptimiser();
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveMapMemoizedMultiplier_6Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveMapMemoizedMCMOptimiser();
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveArrayMemoizedMultiplier_6Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveArrayMemoizedMCMOptimiser();
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}
	
	@Test
	public void computeOptimalOrder_ParallelBottomUpMultiplier_6Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getParallelBottomUpMCMOptimiser();
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJMapMemoizedMultiplier_6Matrices() {
		final int parallelism = 2;
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJMapMemoizedMCMOptimiser(parallelism);
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJArrayMemoizedMultiplier_6Matrices() {
		final int parallelism = 2;
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJArrayMemoizedMCMOptimiser(parallelism);
		computeOptimalOrder_6Matrices(matrixChainMultiplier);
	}

	/*************************
	 * 10 matrices chain multiplication
	 ****************************/

	@Test
	public void computeOptimalOrder_SerialRecursiveMultiplier_10Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveMCMOptimiser();
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialBottomUpMultiplier_10Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialBottomUpMCMOptimiser();
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveMapMemoizedMultiplier_10Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveMapMemoizedMCMOptimiser();
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveArrayMemoizedMultiplier_10Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveArrayMemoizedMCMOptimiser();
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_ParallelBottomUpMultiplier_10Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getParallelBottomUpMCMOptimiser();
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJMapMemoizedMultiplier_10Matrices() {
		final int parallelism = 4;
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJMapMemoizedMCMOptimiser(parallelism);
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJArrayMemoizedMultiplier_10Matrices() {
		final int parallelism = 4;
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJArrayMemoizedMCMOptimiser(parallelism);
		computeOptimalOrder_10Matrices(matrixChainMultiplier);
	}

	/*************************
	 * 20 matrices chain multiplication
	 ****************************/
	@Test
	public void computeOptimalOrder_SerialRecursiveMultiplier_20Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveMCMOptimiser();
		computeOptimalOrder_20Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialBottomUpMultiplier_20Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialBottomUpMCMOptimiser();
		computeOptimalOrder_20Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveMapMemoizedMultiplier_20Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveMapMemoizedMCMOptimiser();
		computeOptimalOrder_20Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_SerialRecursiveArrayMemoizedMultiplier_20Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveArrayMemoizedMCMOptimiser();
		computeOptimalOrder_20Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_ParallelBottomUpMultiplier_20Matrices() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getParallelBottomUpMCMOptimiser();
		computeOptimalOrder_20Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJMapMemoizedMultiplier_20Matrices() {
		final int parallelism = 4;
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJMapMemoizedMCMOptimiser(parallelism);
		computeOptimalOrder_20Matrices(matrixChainMultiplier);
	}

	@Test
	public void computeOptimalOrder_FJArrayMemoizedMultiplier_20Matrices() {
		final int parallelism = 4;
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJArrayMemoizedMCMOptimiser(parallelism);
		computeOptimalOrder_20Matrices(matrixChainMultiplier);
	}

}

package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

final class MCMFactory {

	private MatrixChainMultiplicationOptimiser serialRecursiveMatrixChainMultiplicationOptimiser;
	private MatrixChainMultiplicationOptimiser serialTailRecursiveMatrixChainMultiplicationOptimiser;
	private MatrixChainMultiplicationOptimiser serialBottomUpMatrixChainMultiplicationOptimiser;
	private MatrixChainMultiplicationOptimiser serialRecursiveMapMemoizedMatrixChainMultiplicationOptimiser;
	private MatrixChainMultiplicationOptimiser serialRecursiveArrayMemoizedMatrixChainMultiplicationOptimiser;
	private MatrixChainMultiplicationOptimiser fjMapMemoizedMatrixChainMultiplicationOptimiser;
	private MatrixChainMultiplicationOptimiser fjArrayMemoizedMatrixChainMultiplicationOptimiser;
	private int fjMapParallelism;
	private int fjArrayParallelism;

	private static final MCMFactory singleton = new MCMFactory();

	private MCMFactory() {
		serialRecursiveMatrixChainMultiplicationOptimiser = new SerialRecursiveMCMOptimiser();
		serialTailRecursiveMatrixChainMultiplicationOptimiser = new SerialTailRecursiveMCMOptimiser();
		serialBottomUpMatrixChainMultiplicationOptimiser = new SerialBottomUpMCMOptimiser();
		serialRecursiveMapMemoizedMatrixChainMultiplicationOptimiser = new SerialRecursiveMapMemoizedMCMOptimiser();
		serialRecursiveArrayMemoizedMatrixChainMultiplicationOptimiser = new SerialRecursiveArrayMemoizedMCMOptimiser();
		fjMapParallelism = 1;
		fjArrayParallelism = 1;
		fjMapMemoizedMatrixChainMultiplicationOptimiser = new FJMapMemoizedMCMOptimiser(fjMapParallelism);
		fjArrayMemoizedMatrixChainMultiplicationOptimiser = new FJArrayMemoizedMCMOptimiser(fjArrayParallelism);
	}

	static MCMFactory getInstance() {
		return singleton;
	}

	MatrixChainMultiplicationOptimiser getSerialRecursiveMatrixChainMultiplicationOptimiser() {
		return serialRecursiveMatrixChainMultiplicationOptimiser;
	}

	MatrixChainMultiplicationOptimiser getSerialTailRecursiveMatrixChainMultiplicationOptimiser() {
		return serialTailRecursiveMatrixChainMultiplicationOptimiser;
	}

	MatrixChainMultiplicationOptimiser getSerialBottomUpMatrixChainMultiplicationOptimiser() {
		return serialBottomUpMatrixChainMultiplicationOptimiser;
	}

	MatrixChainMultiplicationOptimiser getSerialRecursiveMapMemoizedMatrixChainMultiplicationOptimiser() {
		return serialRecursiveMapMemoizedMatrixChainMultiplicationOptimiser;
	}

	MatrixChainMultiplicationOptimiser getSerialRecursiveArrayMemoizedMatrixChainMultiplicationOptimiser() {
		return serialRecursiveArrayMemoizedMatrixChainMultiplicationOptimiser;
	}

	MatrixChainMultiplicationOptimiser getFJMapMemoizedMatrixChainMultiplicationOptimiser(int parallelism) {
		if (parallelism == fjMapParallelism) {
			return fjMapMemoizedMatrixChainMultiplicationOptimiser;
		} else {
			fjMapParallelism = parallelism;
			fjMapMemoizedMatrixChainMultiplicationOptimiser = new FJMapMemoizedMCMOptimiser(fjMapParallelism);
			return fjMapMemoizedMatrixChainMultiplicationOptimiser;
		}
	}

	MatrixChainMultiplicationOptimiser getFJArrayMemoizedMatrixChainMultiplicationOptimiser(int parallelism) {
		if (parallelism == fjArrayParallelism) {
			return fjArrayMemoizedMatrixChainMultiplicationOptimiser;
		} else {
			fjArrayParallelism = parallelism;
			fjArrayMemoizedMatrixChainMultiplicationOptimiser = new FJArrayMemoizedMCMOptimiser(fjArrayParallelism);
			return fjArrayMemoizedMatrixChainMultiplicationOptimiser;
		}
	}

}

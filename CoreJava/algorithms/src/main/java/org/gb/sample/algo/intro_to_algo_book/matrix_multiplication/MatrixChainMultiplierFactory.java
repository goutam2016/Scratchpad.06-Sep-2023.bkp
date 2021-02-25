package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

final class MatrixChainMultiplierFactory {

	private MatrixChainMultiplierFactory() {

	}

	static MatrixChainMultiplier getSerialRecursiveMatrixChainMultiplier() {
		return new SerialRecursiveMatrixChainMultiplier();
	}

	static MatrixChainMultiplier getSerialTailRecursiveMatrixChainMultiplier() {
		return new SerialTailRecursiveMatrixChainMultiplier();
	}

	static MatrixChainMultiplier getSerialBottomUpMatrixChainMultiplier() {
		return new SerialBottomUpMatrixChainMultiplier();
	}

	static MatrixChainMultiplier getSerialRecursiveMapMemoizedMatrixChainMultiplier() {
		return new SerialRecursiveMapMemoizedMatrixChainMultiplier();
	}

	static MatrixChainMultiplier getSerialRecursiveArrayMemoizedMatrixChainMultiplier() {
		return new SerialRecursiveArrayMemoizedMatrixChainMultiplier();
	}

	static MatrixChainMultiplier getFJMapMemoizedMatrixChainMultiplier(int parallelism) {
		return new FJMapMemoizedMatrixChainMultiplier(parallelism);
	}

	static MatrixChainMultiplier getFJArrayMemoizedMatrixChainMultiplier(int parallelism) {
		return new FJArrayMemoizedMatrixChainMultiplier(parallelism);
	}
}

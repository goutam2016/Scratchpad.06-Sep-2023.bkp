package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

class FJMapMemoizedMatrixChainMultiplier extends AbstractFJMatrixChainMultiplier {

	FJMapMemoizedMatrixChainMultiplier(int parallelism) {
		super(parallelism);
	}

	@Override
	ForkJoinTask<Matrix> createMatrixChainMultiplierTask(final List<Matrix> matrixChain) {
		ForkJoinTask<Matrix> matrixChainMultiplierTask = new MapMemoizedMatrixChainMultiplierTask(matrixChain, new HashMap<>());
		return matrixChainMultiplierTask;
	}
}

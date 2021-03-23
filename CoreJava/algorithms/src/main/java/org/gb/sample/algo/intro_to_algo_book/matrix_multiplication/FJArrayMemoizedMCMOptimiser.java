package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.List;
import java.util.concurrent.ForkJoinTask;

class FJArrayMemoizedMCMOptimiser extends AbstractFJMatrixChainMultiplicationOptimiser {

	FJArrayMemoizedMCMOptimiser(int parallelism) {
		super(parallelism);
	}

	@Override
	ForkJoinTask<Matrix> createMatrixChainMultiplierTask(List<Matrix> matrixChain) {
		final int matrixCount = matrixChain.size();
		final Matrix[][] cachedOptimalProducts = new Matrix[matrixCount][matrixCount];
		ForkJoinTask<Matrix> matrixChainMultiplierTask = new ArrayMemoizedMatrixChainMultiplierTask(matrixChain, 0, matrixCount - 1, cachedOptimalProducts);
		return matrixChainMultiplierTask;
	}
}

package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import org.apache.log4j.Logger;

abstract class AbstractFJMatrixChainMultiplier extends AbstractMatrixChainMultiplier {

	private static final Logger LOGGER = Logger.getLogger(FJMapMemoizedMatrixChainMultiplier.class);
	private final int parallelism;

	AbstractFJMatrixChainMultiplier(int parallelism) {
		super();
		this.parallelism = parallelism;
	}

	@Override
	Matrix computeOptimalOrderInternal(final List<Matrix> matrixChain) {
		ForkJoinTask<Matrix> matrixChainMultiplierTask = createMatrixChainMultiplierTask(matrixChain);
		ForkJoinPool fjPool = new ForkJoinPool(parallelism);
		LOGGER.info(String.format("Using ForkJoinPool with parallelism: %d, pool-size: %d.", fjPool.getParallelism(), fjPool.getPoolSize()));
		Matrix optimalProduct = fjPool.invoke(matrixChainMultiplierTask);
		return optimalProduct;
	}

	abstract ForkJoinTask<Matrix> createMatrixChainMultiplierTask(final List<Matrix> matrixChain);
}
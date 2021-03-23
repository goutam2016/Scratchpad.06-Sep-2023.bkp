package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

abstract class AbstractFJMatrixChainMultiplicationOptimiser extends AbstractMatrixChainMultiplicationOptimiser {

	private static final Logger LOGGER = Logger.getLogger(AbstractFJMatrixChainMultiplicationOptimiser.class);
	 final ForkJoinPool fjPool;

	AbstractFJMatrixChainMultiplicationOptimiser(int parallelism) {
		super();
		fjPool = new ForkJoinPool(parallelism);
	}

	@Override
	Matrix computeOptimalOrderInternal(final List<Matrix> matrixChain) {
		ForkJoinTask<Matrix> matrixChainMultiplierTask = createMatrixChainMultiplierTask(matrixChain);
//		LOGGER.info(String.format("Using ForkJoinPool with parallelism: %d, pool-size: %d.", fjPool.getParallelism(),
//				fjPool.getPoolSize()));
		Matrix optimalProduct;
		try {
			optimalProduct = fjPool.invoke(matrixChainMultiplierTask);
		} catch (Exception ex) {
			throw new RuntimeException("Exception occurred during computation of optimal order!", ex);
		}
		return optimalProduct;
	}

	abstract ForkJoinTask<Matrix> createMatrixChainMultiplierTask(final List<Matrix> matrixChain);

	@Override
	protected void finalize() throws Throwable {
		try {
			LOGGER.info(String.format("Class: %s is about to be garbage collected.", getClass().getSimpleName()));
			fjPool.shutdown();
			fjPool.awaitTermination(2, TimeUnit.SECONDS);
		} catch (Exception e) {
			LOGGER.error("Exception caught while waiting for FJPool termination.", e);
		} finally {
			super.finalize();
		}
	}
}
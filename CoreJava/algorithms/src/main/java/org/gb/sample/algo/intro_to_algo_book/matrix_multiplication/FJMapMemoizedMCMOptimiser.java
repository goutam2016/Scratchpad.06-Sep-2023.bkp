package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinTask;

import org.apache.log4j.Logger;

class FJMapMemoizedMCMOptimiser extends AbstractFJMatrixChainMultiplicationOptimiser {

	private static final Logger LOGGER = Logger.getLogger(FJMapMemoizedMCMOptimiser.class);
	
	FJMapMemoizedMCMOptimiser(int parallelism) {
		super(parallelism);
	}

	@Override
	ForkJoinTask<Matrix> createMatrixChainMultiplierTask(final List<Matrix> matrixChain) {
		ForkJoinTask<Matrix> matrixChainMultiplierTask = new MapMemoizedMatrixChainMultiplierTask(matrixChain, new ConcurrentHashMap<>());
		return matrixChainMultiplierTask;
	}
	
	@Override
	Matrix computeOptimalOrderInternal(List<Matrix> matrixChain) {
		MapMemoizedMatrixChainMultiplierTask matrixChainMultiplierTask = (MapMemoizedMatrixChainMultiplierTask) createMatrixChainMultiplierTask(matrixChain);
//		LOGGER.info(String.format("Using ForkJoinPool with parallelism: %d, pool-size: %d.", fjPool.getParallelism(),
//				fjPool.getPoolSize()));
		Matrix optimalProduct;
		try {
			optimalProduct = fjPool.invoke(matrixChainMultiplierTask);
		} catch (Exception ex) {
			throw new RuntimeException("Exception occurred during computation of optimal order!", ex);
		}
//		LOGGER.info("matrixChainMultiplierTask.chainProductComputeCount");
//		LOGGER.info("/***********************************************************/");
//		LOGGER.info("\n");
//		matrixChainMultiplierTask.chainProductComputeCount.forEach((k, v) -> System.out.println(k + " <--> " + v));
//		LOGGER.info("\n");
//		LOGGER.info("matrixChainMultiplierTask.chainProductCacheHitCount");
//		LOGGER.info("/***********************************************************/");
//		LOGGER.info("\n");
//		matrixChainMultiplierTask.chainProductCacheHitCount.forEach((k, v) -> System.out.println(k + " <--> " + v));
//		LOGGER.info("\n");
		return optimalProduct;
	}
}

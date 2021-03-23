package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import org.apache.log4j.Logger;

final class MCMOptimiserFactory {

	private static final Logger LOGGER = Logger.getLogger(MCMOptimiserFactory.class);

	private static MatrixChainMultiplicationOptimiser serialRecursiveMCMOptimiser;
	private static MatrixChainMultiplicationOptimiser serialTailRecursiveMCMOptimiser;
	private static MatrixChainMultiplicationOptimiser serialBottomUpMCMOptimiser;
	private static MatrixChainMultiplicationOptimiser serialRecursiveMapMemoizedMCMOptimiser;
	private static MatrixChainMultiplicationOptimiser serialRecursiveArrayMemoizedMCMOptimiser;
	private static MatrixChainMultiplicationOptimiser parallelBottomUpMCMOptimiser;
	private static MatrixChainMultiplicationOptimiser fjMapMemoizedMCMOptimiser;
	private static MatrixChainMultiplicationOptimiser fjArrayMemoizedMCMOptimiser;
	private static int fjMapParallelism;
	private static int fjArrayParallelism;

	private MCMOptimiserFactory() {

	}

	static {
		serialRecursiveMCMOptimiser = new SerialRecursiveMCMOptimiser();
		serialTailRecursiveMCMOptimiser = new SerialTailRecursiveMCMOptimiser();
		serialBottomUpMCMOptimiser = new SerialBottomUpMCMOptimiser();
		serialRecursiveMapMemoizedMCMOptimiser = new SerialRecursiveMapMemoizedMCMOptimiser();
		serialRecursiveArrayMemoizedMCMOptimiser = new SerialRecursiveArrayMemoizedMCMOptimiser();
		parallelBottomUpMCMOptimiser = new ParallelBottomUpMCMOptimiser(); 
		fjMapParallelism = 1;
		fjArrayParallelism = 1;
		fjMapMemoizedMCMOptimiser = new FJMapMemoizedMCMOptimiser(fjMapParallelism);
		fjArrayMemoizedMCMOptimiser = new FJArrayMemoizedMCMOptimiser(fjArrayParallelism);
	}

	static MatrixChainMultiplicationOptimiser getSerialRecursiveMCMOptimiser() {
		return serialRecursiveMCMOptimiser;
	}

	static MatrixChainMultiplicationOptimiser getSerialTailRecursiveMCMOptimiser() {
		return serialTailRecursiveMCMOptimiser;
	}

	static MatrixChainMultiplicationOptimiser getSerialBottomUpMCMOptimiser() {
		return serialBottomUpMCMOptimiser;
	}

	static MatrixChainMultiplicationOptimiser getSerialRecursiveMapMemoizedMCMOptimiser() {
		return serialRecursiveMapMemoizedMCMOptimiser;
	}

	static MatrixChainMultiplicationOptimiser getSerialRecursiveArrayMemoizedMCMOptimiser() {
		return serialRecursiveArrayMemoizedMCMOptimiser;
	}
	
	static MatrixChainMultiplicationOptimiser getParallelBottomUpMCMOptimiser() {
		return parallelBottomUpMCMOptimiser;
	}

	static MatrixChainMultiplicationOptimiser getFJMapMemoizedMCMOptimiser(int parallelism) {
		//LOGGER.info(String.format("parallelism = %d, fjMapParallelism = %d.", parallelism, fjMapParallelism));
		if (parallelism == fjMapParallelism) {
			//LOGGER.info(String.format("FJMapMemoizedMatrixChainMultiplier with parallelism = %d already exists.", parallelism));
			return fjMapMemoizedMCMOptimiser;
		} else {
			//LOGGER.info(String.format("Instantiating FJMapMemoizedMatrixChainMultiplier with parallelism = %d.", parallelism));
			fjMapParallelism = parallelism;
			fjMapMemoizedMCMOptimiser = new FJMapMemoizedMCMOptimiser(fjMapParallelism);
			System.gc();
			System.runFinalization();
			return fjMapMemoizedMCMOptimiser;
		}
	}
	
	static MatrixChainMultiplicationOptimiser getFJArrayMemoizedMCMOptimiser(int parallelism) {
		//LOGGER.info(String.format("parallelism = %d, fjArrayParallelism = %d.", parallelism, fjArrayParallelism));
		if (parallelism == fjArrayParallelism) {
			//LOGGER.info(String.format("FJArrayMemoizedMatrixChainMultiplier with parallelism = %d already exists.", parallelism));
			return fjArrayMemoizedMCMOptimiser;
		} else {
			//LOGGER.info(String.format("Instantiating FJArrayMemoizedMatrixChainMultiplier with parallelism = %d.", parallelism));
			fjArrayParallelism = parallelism;
			fjArrayMemoizedMCMOptimiser = new FJArrayMemoizedMCMOptimiser(fjArrayParallelism);
			System.gc();
			System.runFinalization();
			return fjArrayMemoizedMCMOptimiser;
		}
	}
}

package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Timeout(timeUnit = TimeUnit.SECONDS, time = 10)
public class MatrixChainMultiplierBenchmark {

	private static final Logger LOGGER = Logger.getLogger(MatrixChainMultiplierBenchmark.class);

	private final List<Matrix> matrixChain;

	public MatrixChainMultiplierBenchmark() {
		this.matrixChain = new ArrayList<>();
	}

	private List<Matrix> createMatrixBatch(char batchPrefix, int batchSize, int firstMatrixRowCount, Random random) {
		final List<Matrix> matrixBatch = new ArrayList<>();
		int prevMatrixColumnCount = 0;

		for (int i = 0; i < batchSize; i++) {
			StringBuilder nameBuilder = new StringBuilder();
			String name = nameBuilder.append(batchPrefix).append(i).toString();
			int rowCount = (i == 0) ? firstMatrixRowCount : prevMatrixColumnCount;
			int columnCount = random.ints(10, 101).findFirst().getAsInt();
			Matrix matrix = new Matrix(name, rowCount, columnCount);
			matrixBatch.add(matrix);
			prevMatrixColumnCount = columnCount;
		}
		return matrixBatch;
	}

	@Setup(Level.Trial)
	public void setupForAll() {
		final int batchSize = 20;
		final Random random = new Random();

		final List<Matrix> aBatch = createMatrixBatch('A', batchSize, 20, random);
		final int aBatchLastColCnt = aBatch.get(batchSize - 1).getColumnCount();

		final List<Matrix> bBatch = createMatrixBatch('B', batchSize, aBatchLastColCnt, random);
		final int bBatchLastColCnt = bBatch.get(batchSize - 1).getColumnCount();

		final List<Matrix> cBatch = createMatrixBatch('C', batchSize, bBatchLastColCnt, random);
		final int cBatchLastColCnt = cBatch.get(batchSize - 1).getColumnCount();

		final List<Matrix> dBatch = createMatrixBatch('D', batchSize, cBatchLastColCnt, random);
		final int dBatchLastColCnt = dBatch.get(batchSize - 1).getColumnCount();

		final List<Matrix> eBatch = createMatrixBatch('E', batchSize, dBatchLastColCnt, random);
		final int eBatchLastColCnt = eBatch.get(batchSize - 1).getColumnCount();

		final List<Matrix> fBatch = createMatrixBatch('F', batchSize, eBatchLastColCnt, random);
		final int fBatchLastColCnt = fBatch.get(batchSize - 1).getColumnCount();

		final List<Matrix> gBatch = createMatrixBatch('G', batchSize, fBatchLastColCnt, random);
		final int gBatchLastColCnt = gBatch.get(batchSize - 1).getColumnCount();

		final List<Matrix> hBatch = createMatrixBatch('H', batchSize, gBatchLastColCnt, random);

		matrixChain.addAll(aBatch);
		matrixChain.addAll(bBatch);
		matrixChain.addAll(cBatch);
		matrixChain.addAll(dBatch);
		matrixChain.addAll(eBatch);
		matrixChain.addAll(fBatch);
		matrixChain.addAll(gBatch);
		matrixChain.addAll(hBatch);
	}

	// @Benchmark
	public void computeOptimalOrder_with_SerialRecursiveMultiplier() {
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveMatrixChainMultiplier();
		Matrix product = matrixChainMultiplier.computeOptimalOrder(matrixChain);

		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
				product.getCumulativeMultiplyCount()));
	}

//	@Benchmark
//	public void computeOptimalOrder_with_SerialBottomUpMultiplier() {
//		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialBottomUpMatrixChainMultiplier();
//		Matrix product = matrixChainMultiplier.computeOptimalOrder(matrixChain);
//
//		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
//				product.getCumulativeMultiplyCount()));
//	}
//
//	@Benchmark
//	public void computeOptimalOrder_with_SerialRecursiveMapMemoizedMultiplier() {
//		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveMapMemoizedMatrixChainMultiplier();
//		Matrix product = matrixChainMultiplier.computeOptimalOrder(matrixChain);
//
//		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
//				product.getCumulativeMultiplyCount()));
//	}
//
//	@Benchmark
//	public void computeOptimalOrder_with_SerialRecursiveArrayMemoizedMultiplier() {
//		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getSerialRecursiveArrayMemoizedMatrixChainMultiplier();
//		Matrix product = matrixChainMultiplier.computeOptimalOrder(matrixChain);
//
//		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
//				product.getCumulativeMultiplyCount()));
//	}

	@Benchmark
	public void computeOptimalOrder_with_FJMapMemoizedMultiplier() {
		final int parallelism = 1;
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getFJMapMemoizedMatrixChainMultiplier(parallelism);
		Matrix product = matrixChainMultiplier.computeOptimalOrder(matrixChain);

		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
				product.getCumulativeMultiplyCount()));
	}

	@Benchmark
	public void computeOptimalOrder_with_FJArrayMemoizedMultiplier() {
		final int parallelism = 1;
		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getFJArrayMemoizedMatrixChainMultiplier(parallelism);
		Matrix product = matrixChainMultiplier.computeOptimalOrder(matrixChain);

		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
				product.getCumulativeMultiplyCount()));
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(MatrixChainMultiplierBenchmark.class.getSimpleName()).forks(1).build();
		new Runner(opt).run();
	}
}

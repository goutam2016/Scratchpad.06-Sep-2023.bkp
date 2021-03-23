package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.gb.sample.algo.JMHOptionsBuilder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Timeout(timeUnit = TimeUnit.SECONDS, time = 10)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@Fork(1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class MatrixChainMultiplierBenchmark {

	private static final Logger LOGGER = Logger.getLogger(MatrixChainMultiplierBenchmark.class);
	
	private static final String JFR_RECORDING_SWITCH_PROPTY = "JFRRecordingOn";
	
	private final List<Matrix> fixedDimsMatrixChain;
	private final List<Matrix> randomDimsMatrixChain;
	
	//@Param({ "2", "4", "8" })
	private int parallelism;

	public MatrixChainMultiplierBenchmark() {
		this.fixedDimsMatrixChain = buildFixedDimsMatrixChain();
		this.randomDimsMatrixChain = buildRandomDimsMatrixChain();
	}

	private List<Matrix> buildFixedDimsMatrixChain() {
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
		return List.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t);
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

	private List<Matrix> buildRandomDimsMatrixChain() {
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

		final List<Matrix> tempMatrixChain = new ArrayList<>();
		tempMatrixChain.addAll(aBatch);
		tempMatrixChain.addAll(bBatch);
		tempMatrixChain.addAll(cBatch);
		tempMatrixChain.addAll(dBatch);
		tempMatrixChain.addAll(eBatch);
		tempMatrixChain.addAll(fBatch);
		tempMatrixChain.addAll(gBatch);
		tempMatrixChain.addAll(hBatch);
		
		return Collections.unmodifiableList(tempMatrixChain);
	}

	// @Benchmark
	public void computeOptimalOrder_with_SerialRecursiveMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveMCMOptimiser();
		Matrix product = matrixChainMultiplier.computeOptimalOrder(randomDimsMatrixChain);

		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
				product.getCumulativeMultiplyCount()));
	}

	//@Benchmark
	public void computeOptimalOrder_for_fixedDimsMtxChn_with_SerialBottomUpMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialBottomUpMCMOptimiser();
		Matrix product = matrixChainMultiplier.computeOptimalOrder(fixedDimsMatrixChain);

		LOGGER.info(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
				product.getCumulativeMultiplyCount()));
	}

	//@Benchmark
	public void computeOptimalOrder_for_fixedDimsMtxChn_with_SerialRecursiveMapMemoizedMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveMapMemoizedMCMOptimiser();
		Matrix product = matrixChainMultiplier.computeOptimalOrder(fixedDimsMatrixChain);

		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
				product.getCumulativeMultiplyCount()));
	}

	//@Benchmark
	public void computeOptimalOrder_for_fixedDimsMtxChn_with_SerialRecursiveArrayMemoizedMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveArrayMemoizedMCMOptimiser();
		Matrix product = matrixChainMultiplier.computeOptimalOrder(fixedDimsMatrixChain);

		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
				product.getCumulativeMultiplyCount()));
	}

	//@Benchmark
	public void computeOptimalOrder_for_fixedDimsMtxChn_with_FJMapMemoizedMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJMapMemoizedMCMOptimiser(parallelism);
		Matrix product = matrixChainMultiplier.computeOptimalOrder(fixedDimsMatrixChain);

		LOGGER.info(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
				product.getCumulativeMultiplyCount()));
	}

	//@Benchmark
	public void computeOptimalOrder_for_fixedDimsMtxChn_with_FJArrayMemoizedMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJArrayMemoizedMCMOptimiser(4);
		Matrix product = matrixChainMultiplier.computeOptimalOrder(fixedDimsMatrixChain);

		LOGGER.info(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
				product.getCumulativeMultiplyCount()));
	}
	
	@Benchmark
	public void computeOptimalOrder_for_randomDimsMtxChn_with_SerialBottomUpMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialBottomUpMCMOptimiser();
		Matrix product = matrixChainMultiplier.computeOptimalOrder(randomDimsMatrixChain);

//		LOGGER.info(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
//				product.getCumulativeMultiplyCount()));
		LOGGER.info(String.format("Optimal no. of multiplications: %d.", product.getCumulativeMultiplyCount()));
	}

	//@Benchmark
	public void computeOptimalOrder_for_randomDimsMtxChn_with_SerialRecursiveMapMemoizedMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveMapMemoizedMCMOptimiser();
		Matrix product = matrixChainMultiplier.computeOptimalOrder(randomDimsMatrixChain);

		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
				product.getCumulativeMultiplyCount()));
	}

	@Benchmark
	public void computeOptimalOrder_for_randomDimsMtxChn_with_SerialRecursiveArrayMemoizedMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getSerialRecursiveArrayMemoizedMCMOptimiser();
		Matrix product = matrixChainMultiplier.computeOptimalOrder(randomDimsMatrixChain);

//		LOGGER.info(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
//				product.getCumulativeMultiplyCount()));
		LOGGER.info(String.format("Optimal no. of multiplications: %d.", product.getCumulativeMultiplyCount()));
	}

	@Benchmark
	public void computeOptimalOrder_for_randomDimsMtxChn_with_ParallelBottomUpMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getParallelBottomUpMCMOptimiser();
		Matrix product = matrixChainMultiplier.computeOptimalOrder(randomDimsMatrixChain);

//		LOGGER.info(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
//				product.getCumulativeMultiplyCount()));
		LOGGER.info(String.format("Optimal no. of multiplications: %d.", product.getCumulativeMultiplyCount()));
	}
	
	@Benchmark
	public void computeOptimalOrder_for_randomDimsMtxChn_with_FJMapMemoizedMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJMapMemoizedMCMOptimiser(1);
		Matrix product = matrixChainMultiplier.computeOptimalOrder(randomDimsMatrixChain);

//		LOGGER.info(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
//				product.getCumulativeMultiplyCount()));
		LOGGER.info(String.format("Optimal no. of multiplications: %d.", product.getCumulativeMultiplyCount()));
	}

	@Benchmark
	public void computeOptimalOrder_for_randomDimsMtxChn_with_FJArrayMemoizedMultiplier() {
		MatrixChainMultiplicationOptimiser matrixChainMultiplier = MCMOptimiserFactory.getFJArrayMemoizedMCMOptimiser(1);
		Matrix product = matrixChainMultiplier.computeOptimalOrder(randomDimsMatrixChain);

//		LOGGER.info(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
//				product.getCumulativeMultiplyCount()));
		LOGGER.info(String.format("Optimal no. of multiplications: %d.", product.getCumulativeMultiplyCount()));
	}

//	@Benchmark
//	public void computeOptimalOrder_with_FJMapMemoizedMultiplier_Parallelism4() {
//		final int parallelism = 4;
//		MatrixChainMultiplier matrixChainMultiplier = MCMFactory.getInstance().getFJMapMemoizedMatrixChainMultiplier(parallelism);
//		//MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getFJMapMemoizedMatrixChainMultiplier(parallelism);
//		Matrix product = matrixChainMultiplier.computeOptimalOrder(matrixChain);
//
//		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
//				product.getCumulativeMultiplyCount()));
//	}
//
//	@Benchmark
//	public void computeOptimalOrder_with_FJArrayMemoizedMultiplier_Parallelism4() {
//		final int parallelism = 4;
//		MatrixChainMultiplier matrixChainMultiplier = MatrixChainMultiplierFactory.getFJArrayMemoizedMatrixChainMultiplier(parallelism);
//		Matrix product = matrixChainMultiplier.computeOptimalOrder(matrixChain);
//
//		LOGGER.debug(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName(),
//				product.getCumulativeMultiplyCount()));
//	}
	
	private static void runPlain() {
		MatrixChainMultiplierBenchmark benchmark = new MatrixChainMultiplierBenchmark();
		int iterations = 20;

		LOGGER.info("/***********************************************************/");
		LOGGER.info("Running SerialBottomUpMultiplier...");
		LOGGER.info("/***********************************************************/");
		LOGGER.info("");

		long serialBottomUpStartInstant = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			// benchmark.computeOptimalOrder_for_fixedDimsMtxChn_with_SerialBottomUpMultiplier();
			benchmark.computeOptimalOrder_for_randomDimsMtxChn_with_SerialBottomUpMultiplier();
		}
		long serialBottomUpEndInstant = System.currentTimeMillis();

		LOGGER.info("");
		LOGGER.info("/***********************************************************/");
		LOGGER.info("Running SerialRecursiveArrayMemoizedMultiplier...");
		LOGGER.info("/***********************************************************/");
		LOGGER.info("");

		long serialRcsvArrayMemoizedStartInstant = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			// benchmark.computeOptimalOrder_for_fixedDimsMtxChn_with_SerialRecursiveArrayMemoizedMultiplier();
			benchmark.computeOptimalOrder_for_randomDimsMtxChn_with_SerialRecursiveArrayMemoizedMultiplier();
		}
		long serialRcsvArrayMemoizedEndInstant = System.currentTimeMillis();

		LOGGER.info("");
		LOGGER.info("/***********************************************************/");
		LOGGER.info("Running ParallelBottomUpMultiplier...");
		LOGGER.info("/***********************************************************/");
		LOGGER.info("");

		long parallelBottomUpStartInstant = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			// benchmark.computeOptimalOrder_for_fixedDimsMtxChn_with_SerialBottomUpMultiplier();
			benchmark.computeOptimalOrder_for_randomDimsMtxChn_with_ParallelBottomUpMultiplier();
		}
		long parallelBottomUpEndInstant = System.currentTimeMillis();

		LOGGER.info("");
		LOGGER.info("/***********************************************************/");
		LOGGER.info("Running FJArrayMemoizedMultiplier...");
		LOGGER.info("/***********************************************************/");
		LOGGER.info("");

		long fjArrayMemoizedStartInstant = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			// benchmark.computeOptimalOrder_for_fixedDimsMtxChn_with_FJArrayMemoizedMultiplier();
			benchmark.computeOptimalOrder_for_randomDimsMtxChn_with_FJArrayMemoizedMultiplier();
		}
		long fjArrayMemoizedEndInstant = System.currentTimeMillis();

		LOGGER.info("");
		LOGGER.info("/***********************************************************/");
		LOGGER.info("Running FJMapMemoizedMultiplier...");
		LOGGER.info("/***********************************************************/");
		LOGGER.info("");

		long fjMapMemoizedStartInstant = System.currentTimeMillis();
		for (int i = 0; i < iterations; i++) {
			// benchmark.computeOptimalOrder_for_fixedDimsMtxChn_with_FJMapMemoizedMultiplier();
			benchmark.computeOptimalOrder_for_randomDimsMtxChn_with_FJMapMemoizedMultiplier();
		}
		long fjMapMemoizedEndInstant = System.currentTimeMillis();

		LOGGER.info("\n");

		long serialBtmUpMillisecsPerCall = (serialBottomUpEndInstant - serialBottomUpStartInstant) / iterations;
		long serialRcsvArrayMemoizedMillisecsPerCall = (serialRcsvArrayMemoizedEndInstant - serialRcsvArrayMemoizedStartInstant) / iterations;
		long parallelBtmUpMillisecsPerCall = (parallelBottomUpEndInstant - parallelBottomUpStartInstant) / iterations;
		long fjArrayMemoizedMillisecsPerCall = (fjArrayMemoizedEndInstant - fjArrayMemoizedStartInstant) / iterations;
		long fjMapMemoizedMillisecsPerCall = (fjMapMemoizedEndInstant - fjMapMemoizedStartInstant) / iterations;

		String perfResult = "%s: duration per call: %d ms.";

		LOGGER.info(String.format(perfResult, "SerialBottomUpMultiplier", serialBtmUpMillisecsPerCall));
		LOGGER.info(String.format(perfResult, "SerialRecursiveArrayMemoizedMultiplier", serialRcsvArrayMemoizedMillisecsPerCall));
		LOGGER.info(String.format(perfResult, "ParallelBottomUpMultiplier", parallelBtmUpMillisecsPerCall));
		LOGGER.info(String.format(perfResult, "FJArrayMemoizedMultiplier", fjArrayMemoizedMillisecsPerCall));
		LOGGER.info(String.format(perfResult, "FJMapMemoizedMultiplier", fjMapMemoizedMillisecsPerCall));
	}
	
	private static void runJMH(String[] args) throws RunnerException {
		ChainedOptionsBuilder optBuilder = new OptionsBuilder().include(MatrixChainMultiplierBenchmark.class.getSimpleName());

		if (args.length > 0) {
			Map<String, String> argNameVsValue = JMHOptionsBuilder.parseCmdLineArgs(args);
			String jfrRecordingOn = argNameVsValue.getOrDefault(JFR_RECORDING_SWITCH_PROPTY, "false");

			if (Boolean.parseBoolean(jfrRecordingOn)) {
				optBuilder = JMHOptionsBuilder.addJFRProfiler(argNameVsValue, optBuilder, MatrixChainMultiplierBenchmark.class.getSimpleName());
			}
		}

		Options opt = optBuilder.build();
		new Runner(opt).run();
	}

	public static void main(String[] args) throws RunnerException {
		runPlain();
		//runJMH(args);
	}
}

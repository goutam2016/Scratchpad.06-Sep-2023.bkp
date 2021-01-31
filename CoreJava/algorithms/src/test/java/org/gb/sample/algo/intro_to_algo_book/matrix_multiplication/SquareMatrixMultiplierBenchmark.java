package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import org.apache.log4j.Logger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class SquareMatrixMultiplierBenchmark {

	private static final Logger LOGGER = Logger.getLogger(SquareMatrixMultiplierBenchmark.class);

	private void runMatrixMultiplication(MatrixMultiplier matrixMultiplier) {
		final int squareMatrixDimension = 512;
		final int rowCount = squareMatrixDimension;
		final int columnCount = squareMatrixDimension;

		final int[][] firstMatrix = new int[rowCount][columnCount];
		final int[][] secondMatrix = new int[rowCount][columnCount];

		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				firstMatrix[i][j] = 1;
				secondMatrix[i][j] = 1;
			}
		}

		matrixMultiplier.multiply(rowCount, columnCount, rowCount, columnCount, firstMatrix, secondMatrix);
	}

	@BenchmarkMode(Mode.AverageTime)
	@Benchmark
	public void multiply_with_SerialSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialSquareMatrixMultiplier();
		runMatrixMultiplication(matrixMultiplier);
	}

	@BenchmarkMode(Mode.AverageTime)
	@Benchmark
	public void multiply_with_SerialRecursiveSquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialRecursiveSquareMatrixMultiplier();
		runMatrixMultiplication(matrixMultiplier);
	}

	@BenchmarkMode(Mode.AverageTime)
	@Benchmark
	public void multiply_with_SerialRecursiveArrayCopySquareMatrixMultiplier() {
		MatrixMultiplier matrixMultiplier = MatrixMultiplierFactory.getSerialRecursiveArrayCopySquareMatrixMultiplier();
		runMatrixMultiplication(matrixMultiplier);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(SquareMatrixMultiplierBenchmark.class.getSimpleName()).forks(1).build();
		new Runner(opt).run();
	}

}

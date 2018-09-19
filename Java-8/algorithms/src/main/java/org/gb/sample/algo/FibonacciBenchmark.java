package org.gb.sample.algo;

import org.apache.log4j.Logger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class FibonacciBenchmark {

	private static final Logger LOGGER = Logger.getLogger(FibonacciBenchmark.class);

	@Benchmark
	public void createSequence() {
		Fibonacci.createSequence(500);
	}

	@Benchmark
	public void createSequence2() {
		Fibonacci.createSequence2(500);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(FibonacciBenchmark.class.getSimpleName()).forks(1).build();
		new Runner(opt).run();
	}

}

package org.gb.sample.algo.matrix;

import org.apache.log4j.Logger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class TraverserBenchmark {
	
	private static final Logger LOGGER = Logger.getLogger(TraverserBenchmark.class);
	
	@Benchmark
	public void topLeftToBottomRight_12by12() {
		int totalRows = 12;
		int totalColumns = 12;
		int totalRoutes = Traverser.topLeftToBottomRight(totalRows, totalColumns);
	}
	
	@Benchmark
	public void topLeftToBottomRightParallel_12by12() {
		int totalRows = 12;
		int totalColumns = 12;
		int totalRoutes = Traverser.topLeftToBottomRightParallel(totalRows, totalColumns);
	}	

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(TraverserBenchmark.class.getSimpleName()).forks(1).build();
		new Runner(opt).run();
	}

}

package org.gb.sample.algo.permutation;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class PermutationsBenchmark {

	private static final Logger LOGGER = Logger.getLogger(PermutationsBenchmark.class);

	@Benchmark
	public void permute() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j');
		Permutations.permute(characters);
	}

	@Benchmark
	public void permuteParallel() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j');
		Permutations.permuteParallel(characters);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(PermutationsBenchmark.class.getSimpleName()).forks(1).build();
		new Runner(opt).run();
	}
}

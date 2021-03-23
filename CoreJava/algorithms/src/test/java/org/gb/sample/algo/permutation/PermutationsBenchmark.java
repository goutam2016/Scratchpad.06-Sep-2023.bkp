package org.gb.sample.algo.permutation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gb.sample.algo.JMHOptionsBuilder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Warmup(iterations = 3)
@Measurement(iterations = 3)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
public class PermutationsBenchmark {

	private static final Logger LOGGER = Logger.getLogger(PermutationsBenchmark.class);
	
	private static final String JFR_RECORDING_SWITCH_PROPTY = "JFRRecordingOn";

	/*@BenchmarkMode(Mode.AverageTime)
	@Benchmark
	public void permute() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j');
		Permutations.permute(characters);
	}
	
	@BenchmarkMode(Mode.AverageTime)
	@Benchmark
	public void permuteAndReturnList() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j');
		Permutations.permuteAndReturnList(characters);
	}*/
	
/*	@BenchmarkMode(Mode.AverageTime)
	@Benchmark
	public void computePermutations() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l');
		Permutations.computePermutations(characters);
	}
*/	
	@Benchmark
	public void computePermutationsWithCaching() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r');
		Permutations.computePermutationsWithCaching(characters);
	}
	
/*	@BenchmarkMode(Mode.AverageTime)
	@Benchmark
	public void computePermutationsFJ() throws InterruptedException {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l');
		Permutations.computePermutationsFJ(characters);
	}
*/	
	@Benchmark
	public void computePermutationsFJWithCaching() throws InterruptedException {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r');
		Permutations.computePermutationsFJWithCaching(characters);
	}
	
	/*@BenchmarkMode(Mode.AverageTime)
	@Benchmark
	public void permute_11() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k');
		Permutations.permuteOptimized(characters);
	}*/

	/*@Benchmark
	public void permuteDP_NonCacheblCharCombLgth_1() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
		Permutations.permuteDP(characters, 1);
	}

	@Benchmark
	public void permuteDP_NonCacheblCharCombLgth_2() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
		Permutations.permuteDP(characters, 2);
	}

	@Benchmark
	public void permuteDP_NonCacheblCharCombLgth_3() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
		Permutations.permuteDP(characters, 3);
	}

	@Benchmark
	public void permuteDP_NonCacheblCharCombLgth_4() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
		Permutations.permuteDP(characters, 4);
	}

	@Benchmark
	public void permuteDP_NonCacheblCharCombLgth_5() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
		Permutations.permuteDP(characters, 5);
	}*/

	public static void main(String[] args) throws RunnerException {
		ChainedOptionsBuilder optBuilder = new OptionsBuilder().include(PermutationsBenchmark.class.getSimpleName());
		
		if (args.length > 0) {
			Map<String, String> argNameVsValue = JMHOptionsBuilder.parseCmdLineArgs(args);
			String jfrRecordingOn = argNameVsValue.getOrDefault(JFR_RECORDING_SWITCH_PROPTY, "false");
			
			if(Boolean.parseBoolean(jfrRecordingOn)) {
				optBuilder = JMHOptionsBuilder.addJFRProfiler(argNameVsValue, optBuilder, PermutationsBenchmark.class.getSimpleName());
			}
		}

		Options opt = optBuilder.build();
		new Runner(opt).run();
	}
}

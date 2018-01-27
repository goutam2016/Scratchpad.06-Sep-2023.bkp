package org.gb.sample.algo.permutation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import org.apache.log4j.Logger;

public class PermutationComputingTask extends RecursiveTask<Long> {

	private static final long serialVersionUID = 5730463137743989410L;
	private static final Logger LOGGER = Logger.getLogger(PermutationComputingTask.class);

	private List<Character> characters;

	PermutationComputingTask(List<Character> characters) {
		this.characters = characters;
	}

	@Override
	protected Long compute() {
		long numOfPermutations = 0;

		if (characters.size() == 1) {
			numOfPermutations = 1;
		} else if (characters.size() == 2) {
			Character firstChar = characters.get(0);
			Character secondChar = characters.get(1);

			if (firstChar.equals(secondChar)) {
				numOfPermutations = 1;
			} else {
				numOfPermutations = 2;
			}
		} else {
			List<ForkJoinTask<Long>> nextPermComputingTasks = new ArrayList<>();
			Set<Character> distinctChars = new HashSet<>();

			for (int charIdx = 0; charIdx < characters.size(); charIdx++) {
				List<Character> permutableCharacters = new ArrayList<>(characters);
				Character fixedCharacter = permutableCharacters.remove(charIdx);

				if (distinctChars.contains(fixedCharacter)) {
					continue;
				} else {
					distinctChars.add(fixedCharacter);
					ForkJoinTask<Long> nextPermComputingTask = new PermutationComputingTask(permutableCharacters);
					nextPermComputingTasks.add(nextPermComputingTask);
				}
			}

			Collection<ForkJoinTask<Long>> invokedNextPermComputingTasks = ForkJoinTask
					.invokeAll(nextPermComputingTasks);

			for (ForkJoinTask<Long> permComputingTask : invokedNextPermComputingTasks) {
				numOfPermutations += permComputingTask.join().longValue();
			}
		}

		// LOGGER.info(String.format("No. of worker threads computing
		// permutations: %d", ForkJoinTask.getPool().getRunningThreadCount()));
		return numOfPermutations;
	}

}

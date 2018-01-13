package org.gb.sample.algo.permutation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import org.apache.log4j.Logger;

public class PermutationComputer extends RecursiveTask<Set<String>> {

	private static final long serialVersionUID = 5730463137743989410L;
	private static final Logger LOGGER = Logger.getLogger(PermutationComputer.class);

	private List<Character> permutableCharacters;

	PermutationComputer(List<Character> permutableCharacters) {
		this.permutableCharacters = permutableCharacters;
	}

	@Override
	protected Set<String> compute() {
		Set<String> permutations = new TreeSet<>();

		if (permutableCharacters.size() <= 5) {
			permutations.addAll(permute(permutableCharacters));
			return permutations;
		}

		List<Character> fixedCharacters = new ArrayList<>();
		List<ForkJoinTask<Set<String>>> nextPermutationComputers = new ArrayList<>();

		for (int charIdx = 0; charIdx < permutableCharacters.size(); charIdx++) {
			List<Character> nextPermutableCharacters = new ArrayList<>(permutableCharacters);
			Character fixedCharacter = nextPermutableCharacters.remove(charIdx);
			fixedCharacters.add(fixedCharacter);
			ForkJoinTask<Set<String>> nextPermutationComputer = new PermutationComputer(nextPermutableCharacters);
			nextPermutationComputers.add(nextPermutationComputer);
		}

		Collection<ForkJoinTask<Set<String>>> invokedNextPermutationComputers = ForkJoinTask
				.invokeAll(nextPermutationComputers);

		int nextPermutationComputerIndex = 0;
		for (ForkJoinTask<Set<String>> invokedNextPermutationComputer : invokedNextPermutationComputers) {
			Set<String> partialPermutations = invokedNextPermutationComputer.join();
			for (String partialPermutation : partialPermutations) {
				String permutation = String.valueOf(fixedCharacters.get(nextPermutationComputerIndex).charValue())
						.concat(partialPermutation);
				permutations.add(permutation);
			}

			nextPermutationComputerIndex++;
		}

		return permutations;
	}

	private Set<String> permute(List<Character> characters) {
		Set<String> permutations = new TreeSet<>();

		if (characters.size() == 1) {
			String alignment = String.valueOf(characters.get(0).charValue());
			permutations.add(alignment);
		} else if (characters.size() == 2) {
			String forwardAlignment = String.valueOf(characters.get(0).charValue())
					.concat(String.valueOf(characters.get(1).charValue()));
			String reverseAlignment = String.valueOf(characters.get(1).charValue())
					.concat(String.valueOf(characters.get(0).charValue()));
			permutations.add(forwardAlignment);
			permutations.add(reverseAlignment);
		} else {
			for (int charIdx = 0; charIdx < characters.size(); charIdx++) {
				List<Character> permutableCharacters = new ArrayList<>(characters);
				Character fixedCharacter = permutableCharacters.remove(charIdx);
				Set<String> partialPermutations = permute(permutableCharacters);
				for (String partialPermutation : partialPermutations) {
					String permutation = String.valueOf(fixedCharacter.charValue()).concat(partialPermutation);
					permutations.add(permutation);
				}
			}
		}

		return permutations;
	}

}

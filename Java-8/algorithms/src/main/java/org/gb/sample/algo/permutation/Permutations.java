package org.gb.sample.algo.permutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class Permutations {

	public static Set<String> permute(List<Character> characters) {
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
	
	public static Set<String> permuteDP(List<Character> characters) {
		return null;
	}

	public static Set<String> permuteOrGetFromCache(List<Character> originalCharacters, List<Character> characters,
			Map<String, String> positionCombinationVsPermutations) {
		
		StringBuilder positionCombination = new StringBuilder();
		
		for (Character character : characters) {
			if(positionCombination.length() > 0) {
				positionCombination.append(".");
			}
			
			int position = originalCharacters.indexOf(character);
			positionCombination.append(position);
		}
		
		return null;
	}

	public static List<String> permuteAndReturnList(List<Character> characters) {
		List<String> permutations = new ArrayList<>();

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
				List<String> partialPermutations = permuteAndReturnList(permutableCharacters);
				for (String partialPermutation : partialPermutations) {
					String permutation = String.valueOf(fixedCharacter.charValue()).concat(partialPermutation);
					permutations.add(permutation);
				}
			}
		}

		return permutations;
	}

	public static Set<String> permuteParallel(List<Character> characters) {
		ForkJoinPool forkJoinPool = new ForkJoinPool(4);
		ForkJoinTask<Set<String>> permutationComputer = new PermutationComputer(characters);
		Set<String> permutations = forkJoinPool.invoke(permutationComputer);
		return permutations;
	}
}

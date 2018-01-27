package org.gb.sample.algo.permutation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
	
	public static List<String> permuteAndReturnList(List<Character> characters) {
		List<String> permutations = new ArrayList<>();

		if (characters.size() == 1) {
			String alignment = String.valueOf(characters.get(0).charValue());
			permutations.add(alignment);
		} else if (characters.size() == 2) {
			Character firstChar = characters.get(0);
			Character secondChar = characters.get(1);

			String forwardAlignment = null;
			String reverseAlignment = null;

			if (firstChar.equals(secondChar)) {
				forwardAlignment = String.valueOf(characters.get(0).charValue())
						.concat(String.valueOf(characters.get(1).charValue()));
			} else {
				forwardAlignment = String.valueOf(characters.get(0).charValue())
						.concat(String.valueOf(characters.get(1).charValue()));
				reverseAlignment = String.valueOf(characters.get(1).charValue())
						.concat(String.valueOf(characters.get(0).charValue()));
			}

			permutations.add(forwardAlignment);

			if (reverseAlignment != null) {
				permutations.add(reverseAlignment);
			}
		} else {
			Set<Character> distinctChars = new HashSet<>();
			for (int charIdx = 0; charIdx < characters.size(); charIdx++) {
				List<Character> permutableCharacters = new ArrayList<>(characters);
				Character fixedCharacter = permutableCharacters.remove(charIdx);
				
				if(distinctChars.contains(fixedCharacter)) {
					continue;
				}
				
				distinctChars.add(fixedCharacter);
				List<String> partialPermutations = permuteAndReturnList(permutableCharacters);
				for (String partialPermutation : partialPermutations) {
					String permutation = String.valueOf(fixedCharacter.charValue()).concat(partialPermutation);
					permutations.add(permutation);
				}
			}
		}

		return permutations;
	}
	
	public static long computePermutations(List<Character> characters) {
		long numOfPermutations = 0;
		
		if(characters.size()==1) {
			numOfPermutations = 1;
		} else if (characters.size()==2) {
			Character firstChar = characters.get(0);
			Character secondChar = characters.get(1);
			
			if(firstChar.equals(secondChar)) {
				numOfPermutations = 1;
			} else {
				numOfPermutations = 2;
			}
		} else {
			Set<Character> distinctChars = new HashSet<>();
			for (int charIdx = 0; charIdx < characters.size(); charIdx++) {
				List<Character> permutableCharacters = new ArrayList<>(characters);
				Character fixedCharacter = permutableCharacters.remove(charIdx);
				
				if(distinctChars.contains(fixedCharacter)) {
					continue;
				} else {
					distinctChars.add(fixedCharacter);
					numOfPermutations += computePermutations(permutableCharacters);
				}
			}
		}
		
		return numOfPermutations;
	}
	
	public static long computePermutationsWithCaching(List<Character> characters) {
		long numOfPermutations = computePermsOrGetFromCache(new HashMap<>(), characters);
		return numOfPermutations;
	}
	
	private static long computePermsOrGetFromCache(Map<List<Character>, Long> numOfPermsPerCharComb,
			List<Character> characters) {
		if (numOfPermsPerCharComb.containsKey(characters)) {
			return numOfPermsPerCharComb.get(characters).longValue();
		}

		long numOfPermutations = 0;

		if (characters.size() == 1) {
			numOfPermutations = 1;
			return numOfPermutations;
		} else if (characters.size() == 2) {
			Character firstChar = characters.get(0);
			Character secondChar = characters.get(1);

			if (firstChar.equals(secondChar)) {
				numOfPermutations = 1;
			} else {
				numOfPermutations = 2;
			}
			return numOfPermutations;
		}
		
		Set<Character> distinctChars = new HashSet<>();
		
		for (int charIdx = 0; charIdx < characters.size(); charIdx++) {
			List<Character> permutableCharacters = new ArrayList<>(characters);
			Character fixedCharacter = permutableCharacters.remove(charIdx);

			if (distinctChars.contains(fixedCharacter)) {
				continue;
			} else {
				distinctChars.add(fixedCharacter);
				numOfPermutations += computePermsOrGetFromCache(numOfPermsPerCharComb, permutableCharacters);
			}
		}

		numOfPermsPerCharComb.put(characters, numOfPermutations);

		return numOfPermutations;
	}

	public static long computePermutationsFJ(List<Character> characters) throws InterruptedException {
		ForkJoinPool forkJoinPool = new ForkJoinPool(8);
		ForkJoinTask<Long> permutationComputer = new PermutationComputingTask(characters);
		Long numOfPermutations = forkJoinPool.invoke(permutationComputer);
		forkJoinPool.shutdown();
		forkJoinPool.awaitTermination(5, TimeUnit.SECONDS);
		return numOfPermutations;
	}
	
	public static long computePermutationsFJWithCaching(List<Character> characters) throws InterruptedException {
		ForkJoinPool forkJoinPool = new ForkJoinPool(8);
		ForkJoinTask<Long> permutationComputer = new CacheblPermutComputeTask(characters, new ConcurrentHashMap<>());
		Long numOfPermutations = forkJoinPool.invoke(permutationComputer);
		forkJoinPool.shutdown();
		forkJoinPool.awaitTermination(5, TimeUnit.SECONDS);
		return numOfPermutations;
	}

}

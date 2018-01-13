package org.gb.sample.algo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.gb.sample.algo.permutation.Permutations;
import org.junit.Assert;
import org.junit.Test;

public class PermutationsTest {

	private static final Logger LOGGER = Logger.getLogger(PermutationsTest.class);

	@Test
	public void permute() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd');
		Set<String> permutations = Permutations.permute(characters);

		Assert.assertEquals(24, permutations.size());
	}

	@Test
	public void permuteAndReturnList() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e');
		List<String> permutations = Permutations.permuteAndReturnList(characters);

		Assert.assertEquals(120, permutations.size());
	}

	@Test
	public void permuteParallel() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e');
		Set<String> permutations = Permutations.permuteParallel(characters);

		Assert.assertEquals(120, permutations.size());
	}	
}

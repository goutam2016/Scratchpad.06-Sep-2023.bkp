package org.gb.sample.algo.permutation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.gb.sample.algo.permutation.Permutations;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class PermutationsTest {

	private static final Logger LOGGER = Logger.getLogger(PermutationsTest.class);

	@Test
	public void permute() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'f', 'f');
		Set<String> permutations = Permutations.permute(characters);

		Assert.assertEquals(6720, permutations.size());
	}
	
	@Test
	public void permuteAndReturnList() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'f', 'f');
		List<String> permutations = Permutations.permuteAndReturnList(characters);

		Assert.assertEquals(6720, permutations.size());
	}
	
	@Test
	public void computePermutations() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'f', 'f');
		long numOfPermutations = Permutations.computePermutations(characters);

		Assert.assertEquals(6720, numOfPermutations);
	}
	
	@Test
	public void computePermutationsWithCaching() {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r');
		long numOfPermutations = Permutations.computePermutationsWithCaching(characters);

		Assert.assertEquals(6402373705728000L, numOfPermutations);
	}
	
	@Test
	public void computePermutationsFJ() throws InterruptedException {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k');
		long numOfPermutations = Permutations.computePermutationsFJ(characters);

		Assert.assertEquals(39916800, numOfPermutations);
	}	
	
	@Test
	public void computePermutationsFJWithCaching() throws InterruptedException {
		List<Character> characters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r');
		long numOfPermutations = Permutations.computePermutationsFJWithCaching(characters);

		Assert.assertEquals(6402373705728000L, numOfPermutations);
	}	
}

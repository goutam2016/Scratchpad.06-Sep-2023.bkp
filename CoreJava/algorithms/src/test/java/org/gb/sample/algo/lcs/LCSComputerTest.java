package org.gb.sample.algo.lcs;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class LCSComputerTest {

	private static final Logger LOGGER = Logger.getLogger(LCSComputerTest.class);

	private static final char[] LETTERS = new char[26];
	private static final Random RANDOM_LETTER_PICKER = new Random();

	@BeforeClass
	public static void setupForAll() {
		char firstLetter = 'A';

		for (int i = 0; i < LETTERS.length; i++) {
			char letter = (char) (firstLetter + i);
			LETTERS[i] = letter;
		}
	}

	@Test
	public void computeLCS_Test1() {
		List<Character> seq1 = Arrays.asList('A', 'B', 'C', 'B', 'D', 'A', 'B');
		List<Character> seq2 = Arrays.asList('B', 'D', 'C', 'A', 'B', 'A');
		List<Character> exptdLCS = Arrays.asList('B', 'D', 'A', 'B');

		List<Character> lcs = LCSComputer.computeLCS(seq1.size() - 1, seq2.size() - 1, seq1, seq2);

		Assert.assertEquals(exptdLCS, lcs);
		LOGGER.info("Computed LCS: " + lcs);
	}

	@Test
	public void computeLCS_Test2() {
		List<Character> seq1 = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F');
		List<Character> seq2 = Arrays.asList('C', 'D', 'E', 'F', 'G', 'H', 'I', 'J');
		List<Character> exptdLCS = Arrays.asList('C', 'D', 'E', 'F');

		List<Character> lcs = LCSComputer.computeLCS(seq1.size() - 1, seq2.size() - 1, seq1, seq2);

		Assert.assertEquals(exptdLCS, lcs);
		LOGGER.info("Computed LCS: " + lcs);
	}

	@Test
	public void computeLCS_RandomisedTest1() {
		final int firstSeqLength = 20;
		final int secondSeqLength = 15;

		List<Character> seq1 = Stream.iterate(LETTERS.length, UnaryOperator.identity()).limit(firstSeqLength)
				.map(RANDOM_LETTER_PICKER::nextInt).map(i -> LETTERS[i]).collect(Collectors.toList());
		List<Character> seq2 = Stream.iterate(LETTERS.length, UnaryOperator.identity()).limit(secondSeqLength)
				.map(RANDOM_LETTER_PICKER::nextInt).map(i -> LETTERS[i]).collect(Collectors.toList());
		LOGGER.info("seq1: " + seq1);
		LOGGER.info("seq2: " + seq2);
		List<Character> lcs = LCSComputer.computeLCS(seq1.size() - 1, seq2.size() - 1, seq1, seq2);
		LOGGER.info("Computed LCS: " + lcs);
	}
	
	//Takes too much time for unit testing. 
	@Ignore
	@Test
	public void findLCS_RandomisedTest1() {
		final int firstSeqLength = 40;
		final int secondSeqLength = 50;

		List<Character> seq1 = Stream.iterate(LETTERS.length, UnaryOperator.identity()).limit(firstSeqLength)
				.map(RANDOM_LETTER_PICKER::nextInt).map(i -> LETTERS[i]).collect(Collectors.toList());
		List<Character> seq2 = Stream.iterate(LETTERS.length, UnaryOperator.identity()).limit(secondSeqLength)
				.map(RANDOM_LETTER_PICKER::nextInt).map(i -> LETTERS[i]).collect(Collectors.toList());
		LOGGER.info("seq1: " + seq1);
		LOGGER.info("seq2: " + seq2);
		List<Character> lcs = LCSComputer.findLCS(seq1, seq2);
		LOGGER.info("Computed LCS: " + lcs);
	}

	//Takes too much time for unit testing.
	@Ignore
	@Test
	public void computeLCS_RandomisedTest2() {
		final int firstSeqLength = 40;
		final int secondSeqLength = 50;

		List<Character> seq1 = Stream.iterate(LETTERS.length, UnaryOperator.identity()).limit(firstSeqLength)
				.map(RANDOM_LETTER_PICKER::nextInt).map(i -> LETTERS[i]).collect(Collectors.toList());
		List<Character> seq2 = Stream.iterate(LETTERS.length, UnaryOperator.identity()).limit(secondSeqLength)
				.map(RANDOM_LETTER_PICKER::nextInt).map(i -> LETTERS[i]).collect(Collectors.toList());
		List<Character> lcs = LCSComputer.computeLCS(seq1.size() - 1, seq2.size() - 1, seq1, seq2);
		LOGGER.info("Computed LCS: " + lcs);
	}

}

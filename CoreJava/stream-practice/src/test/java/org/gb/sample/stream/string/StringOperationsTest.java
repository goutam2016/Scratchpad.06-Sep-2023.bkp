package org.gb.sample.stream.string;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gb.sample.stream.string.StringOperations;
import org.junit.Assert;
import org.junit.Test;

public class StringOperationsTest {

	private static final Logger LOGGER = Logger.getLogger(StringOperationsTest.class);

	@Test
	public void capitalizeAllItems() {
		String[] nameArr = { "Gulai", "ppo", "durBa", "Mumun", "SwapanKaku" };
		List<String> names = Arrays.asList(nameArr);
		String[] expectedNameArr = { "GULAI", "PPO", "DURBA", "MUMUN", "SWAPANKAKU" };
		List<String> expectedNames = Arrays.asList(expectedNameArr);

		List<String> capitalizedNames = StringOperations.capitalizeAllItems(names);
		Assert.assertEquals(expectedNames, capitalizedNames);
	}

	@Test
	public void findMaxLengthItem() {
		String[] nameArr = { "Gulai", "ppo", "durBa", "Mumun", "SwapanKaku" };
		List<String> names = Arrays.asList(nameArr);

		String longestName = StringOperations.findMaxLengthItem(names);
		Assert.assertEquals("SwapanKaku", longestName);
	}
	
	@Test
	public void findMaxLengthItem2() {
		String[] nameArr = { "John Lennon", "Paul McCartney", "George Harrison", "Ringo Starr", "Pete Best", "Staurt Sutcliffe" };
		List<String> names = Arrays.asList(nameArr);

		String longestName = StringOperations.findMaxLengthItem(names);
		Assert.assertEquals("Staurt Sutcliffe", longestName);
	}	

	@Test
	public void sortAscending() {
		String[] nameArr = { "Gulai", "ppo", "durBa", "Mumun", "SwapanKaku" };
		List<String> names = Arrays.asList(nameArr);
		String[] expectedNameArr = { "durBa", "Gulai", "Mumun", "ppo", "SwapanKaku" };
		List<String> expectedNames = Arrays.asList(expectedNameArr);

		List<String> sortedNames = StringOperations.sort(names, StringOperations.SortOrder.ASCENDING);
		Assert.assertEquals(expectedNames, sortedNames);
	}

	@Test
	public void sortDescending() {
		String[] nameArr = { "Gulai", "ppo", "durBa", "Mumun", "SwapanKaku" };
		List<String> names = Arrays.asList(nameArr);
		String[] expectedNameArr = { "SwapanKaku", "ppo", "Mumun", "Gulai", "durBa" };
		List<String> expectedNames = Arrays.asList(expectedNameArr);

		List<String> sortedNames = StringOperations.sort(names, StringOperations.SortOrder.DESCENDING);
		Assert.assertEquals(expectedNames, sortedNames);
	}
	
	@Test
	public void groupByItemLength() {
		String[] nameArr = { "Gulai", "ppo", "durBa", "Mumun", "SwapanKaku" };
		List<String> names = Arrays.asList(nameArr);
		Map<Integer, List<String>> expectedNamesByLength = new HashMap<>();
		expectedNamesByLength.put(3, Arrays.asList("ppo"));
		expectedNamesByLength.put(5, Arrays.asList("Gulai", "durBa", "Mumun"));
		expectedNamesByLength.put(10, Arrays.asList("SwapanKaku"));

		Map<Integer, List<String>> namesByLength = StringOperations.groupByItemLength(names);
		Assert.assertEquals(expectedNamesByLength, namesByLength);
	}
	
	@Test
	public void countLowercaseLetters() {
		String name = "SwapanKaku";
		int lowercaseLetterCount = StringOperations.countLowercaseLetters(name);
		
		Assert.assertEquals(8, lowercaseLetterCount);
	}
	
	@Test
	public void findItemWithMaxLowercaseLetters() {
		String[] nameArr = { "Gulai", "ppo", "durBa", "Mumun", "SwapanKaku" };
		List<String> names = Arrays.asList(nameArr);
		String maxLowercaseLettersNames = StringOperations.findItemWithMaxLowercaseLetters(names);
		
		Assert.assertEquals("SwapanKaku", maxLowercaseLettersNames);
	}
	
	@Test
	public void groupCountByWord() {
		String[] nameArr = { "John", "Paul", "George", "John", "Paul", "John" };
		List<String> names = Arrays.asList(nameArr);
		
		Map<String, Integer> countByWord = StringOperations.groupCountByWord(names);
		LOGGER.info(countByWord);
	}
	
	@Test
	public void capitalizeAndConcatenate() {
		String[] nameArr = { "Gulai", "ppo", "durBa", "Mumun", "SwapanKaku" };
		List<String> names = Arrays.asList(nameArr);
		
		String namesConcatenated = StringOperations.capitalizeAndConcatenate(names);
		LOGGER.info(namesConcatenated);
	}
	
	@Test
	public void concatenateWithSeparator() {
		String[] nameArr = { "Gulai", "ppo", "durBa", "Mumun", "SwapanKaku" };
		List<String> names = Arrays.asList(nameArr);
		
		String namesConcatenated = StringOperations.concatenateWithSeparator(names);
		LOGGER.info(namesConcatenated);		
	}
	
	@Test
	public void getTotalLengthOfAllItems() {
		String[] nameArr = { "Gulai", "ppo", "durBa", "Mumun", "SwapanKaku" };
		List<String> names = Arrays.asList(nameArr);

		int totalLengthOfAllNames = StringOperations.getTotalLengthOfAllItems(names);
		LOGGER.info("totalLengthOfAllNames: " + totalLengthOfAllNames);
	}
	
	@Test
	public void countItemsHavingChar() {
		String[] nameArr = { "Gulai", "ppo", "durBa", "Mumun", "SwapanKaku" };
		List<String> names = Arrays.asList(nameArr);

		int itemCount = StringOperations.countItemsHavingChar(names, 'G');
		LOGGER.info("itemCount: " + itemCount);
	}
}

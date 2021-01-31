package org.gb.sample.stream.string;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StringOperations {

	enum SortOrder {
		ASCENDING, DESCENDING
	}

	public static List<String> capitalizeAllItems(List<String> items) {
		Function<String, String> operation = (String item) -> item.toUpperCase();
		List<String> capitalizedItems = items.stream().map(operation).collect(Collectors.toList());
		return capitalizedItems;
	}

	public static String findMaxLengthItem(List<String> items) {
		Comparator<String> comparator = Comparator.comparing(String::length);
		String maxLengthItem = items.stream().max(comparator).get();
		return maxLengthItem;
	}

	public static List<String> sort(List<String> items, SortOrder order) {
		Comparator<String> comparator = (String firstItem, String secondItem) -> (order == SortOrder.ASCENDING) ? firstItem
				.compareToIgnoreCase(secondItem) : secondItem.compareToIgnoreCase(firstItem);
		List<String> sortedItems = items.stream().sorted(comparator).collect(Collectors.toList());
		return sortedItems;
	}

	public static Map<Integer, List<String>> groupByItemLength(List<String> items) {
		Map<Integer, List<String>> itemsByLength = items.stream().collect(Collectors.groupingBy(String::length));
		return itemsByLength;
	}

	public static int countLowercaseLetters(String lineOfText) {
		return (int) lineOfText.chars().filter(Character::isLowerCase).count();
	}

	public static String findItemWithMaxLowercaseLetters(List<String> items) {
		ToIntFunction<String> lowercaseLetterCounter = (String lineOfText) -> (int) lineOfText.chars()
				.filter(Character::isLowerCase).count();
		return items.stream().max(Comparator.comparingInt(lowercaseLetterCounter)).orElse(null);
	}

	public static Map<String, Integer> groupCountByWord(List<String> words) {
		Collector<String, ?, Integer> wordCountCollector = Collectors.collectingAndThen(Collectors.counting(),
				Long::intValue);
		return words.stream().collect(Collectors.groupingBy(Function.identity(), wordCountCollector));
	}

	public static String capitalizeAndConcatenate(List<String> items) {
		BinaryOperator<String> itemJoiner = (String item1, String item2) -> "".concat(item1).concat(item2)
				.toUpperCase();
		return items.stream().reduce(itemJoiner).get();
	}

	public static String concatenateWithSeparator(List<String> items) {
		BinaryOperator<String> itemJoiner = (String itemsConcatenated, String nextItem) -> itemsConcatenated
				.concat(",").concat(nextItem);
		return items.stream().reduce(itemJoiner).get();
	}

	public static int getTotalLengthOfAllItems(List<String> items) {
		return items.stream().mapToInt(item -> item.length()).sum();
	}

	public static int countItemsHavingChar(List<String> items, char reqdChar) {
		return (int) items.stream().filter(item -> item.contains(String.valueOf(reqdChar))).count();
	}
}

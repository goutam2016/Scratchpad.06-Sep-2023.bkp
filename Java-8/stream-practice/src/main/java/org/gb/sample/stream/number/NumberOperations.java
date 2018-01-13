package org.gb.sample.stream.number;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class NumberOperations {

	public static int sequentialMultiplyThrough(List<Integer> numbers) {
		BinaryOperator<Integer> accumulator = (Integer firstNumber, Integer secondNumber) -> firstNumber * secondNumber;
		return numbers.stream().reduce(5, accumulator);
	}

	public static int parallelMultiplyThrough(List<Integer> numbers) {
		BiFunction<Integer, Integer, Integer> accumulator = (Integer firstNumber, Integer secondNumber) -> firstNumber
				* secondNumber;
		BinaryOperator<Integer> combiner = (Integer firstNumber, Integer secondNumber) -> firstNumber * secondNumber;
		return numbers.parallelStream().reduce(1, accumulator, combiner) * 5;
	}
}

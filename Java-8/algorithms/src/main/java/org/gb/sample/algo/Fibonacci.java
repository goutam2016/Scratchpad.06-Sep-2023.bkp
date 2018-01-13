package org.gb.sample.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Fibonacci {

	public static List<Integer> createSequence(int range) {
		if (range < 2) {
			throw new IllegalArgumentException("Range below 2 not allowed");
		}

		List<Integer> fibonacciSeq = new ArrayList<>();

		for (int i = 0; i < range; i++) {
			if (i < 2) {
				fibonacciSeq.add(i);
				continue;
			}
			int nextValue = fibonacciSeq.get(i - 2).intValue() + fibonacciSeq.get(i - 1).intValue();
			fibonacciSeq.add(nextValue);
		}

		return fibonacciSeq;
	}

	public static Map<Integer, Integer> createSequence2(int range) {
		if (range < 2) {
			throw new IllegalArgumentException("Range below 2 not allowed");
		}

		Map<Integer, Integer> fibonacciSeq = new HashMap<>();

		Function<Integer, Integer> valueMapper = (Integer position) -> (position < 2) ? position : fibonacciSeq.get(
				position - 2).intValue()
				+ fibonacciSeq.get(position - 1).intValue();

		for (int i = 0; i < range; i++) {
			fibonacciSeq.computeIfAbsent(i, valueMapper);
		}

		return fibonacciSeq;
	}
}

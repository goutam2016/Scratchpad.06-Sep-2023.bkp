package org.gb.sample.algo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gb.sample.algo.Fibonacci;
import org.junit.Assert;
import org.junit.Test;

public class FibonacciTest {
	
	private static final Logger LOGGER = Logger.getLogger(FibonacciTest.class);

	@Test
	public void createSequence() {
		List<Integer> computedFibonacciSeq = Fibonacci.createSequence(12);
		
		List<Integer> expectedFibonacciSeq = Arrays.asList(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89);
		Assert.assertEquals(expectedFibonacciSeq, computedFibonacciSeq);
	}
	
	@Test
	public void createSequence2() {
		Map<Integer, Integer> computedFibonacciSeq = Fibonacci.createSequence2(12);
		
		Map<Integer, Integer> expectedFibonacciSeq = new HashMap<>();
		expectedFibonacciSeq.put(0, 0);
		expectedFibonacciSeq.put(1, 1);
		expectedFibonacciSeq.put(2, 1);
		expectedFibonacciSeq.put(3, 2);
		expectedFibonacciSeq.put(4, 3);
		expectedFibonacciSeq.put(5, 5);
		expectedFibonacciSeq.put(6, 8);
		expectedFibonacciSeq.put(7, 13);
		expectedFibonacciSeq.put(8, 21);
		expectedFibonacciSeq.put(9, 34);
		expectedFibonacciSeq.put(10, 55);
		expectedFibonacciSeq.put(11, 89);
		
		Assert.assertEquals(expectedFibonacciSeq, computedFibonacciSeq);
	}
}

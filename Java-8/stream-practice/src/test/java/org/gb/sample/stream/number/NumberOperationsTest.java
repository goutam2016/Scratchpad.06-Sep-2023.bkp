package org.gb.sample.stream.number;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class NumberOperationsTest {

	private static final Logger LOGGER = Logger.getLogger(NumberOperationsTest.class);

	@Test
	public void sequentialMultiplyThrough() {
		List<Integer> numbers = Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5);

		int result = NumberOperations.sequentialMultiplyThrough(numbers);
		LOGGER.info("result: " + result);
		
		Assert.assertEquals(72000, result);
	}
	
	@Test
	public void parallelMultiplyThrough() {
		List<Integer> numbers = Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5);
		
		int result = NumberOperations.parallelMultiplyThrough(numbers);
		LOGGER.info("result: " + result);
	}
}

package org.gb.sample.algo;

import org.apache.log4j.Logger;
import org.junit.Test;

public class ArrayOperationsTest {

	private static final Logger LOGGER = Logger.getLogger(ArrayOperationsTest.class);

	@Test
	public void rotate() {
		int arrLength = 100;
		int[] numbers = new int[arrLength];

		for (int i = 0; i < arrLength; i++) {
			numbers[i] = i + 1;
		}

		int[] rotatedNumbers = ArrayOperations.rotate(numbers, 20);
		StringBuilder formattedRotatedNumbers = new StringBuilder("{ ");
		for (int index = 0; index < rotatedNumbers.length; index++) {
			int number = rotatedNumbers[index];
			formattedRotatedNumbers.append(number);

			if (index < rotatedNumbers.length - 1) {
				formattedRotatedNumbers.append(", ");
			}
		}
		formattedRotatedNumbers.append(" }");
		LOGGER.info(formattedRotatedNumbers);
	}
}

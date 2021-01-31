package org.gb.sample.algo.matrix;

import org.apache.log4j.Logger;
import org.gb.sample.algo.matrix.MatrixOperations;
import org.junit.Test;

public class MatrixOperationsTest {

	private static final Logger LOGGER = Logger.getLogger(MatrixOperationsTest.class);

	@Test
	public void getElementsInSpiralOrder() {
		int rows = 10;
		int columns = 25;
		int[][] matrix = prepareMatrix(rows, columns);
		int[] elementsInSpiralOrder = MatrixOperations.getElementsInSpiralOrder(rows, columns, matrix);
		String formattedElements = prepareFormattedString(elementsInSpiralOrder);
		LOGGER.info(formattedElements);
	}

	private int[][] prepareMatrix(final int rows, final int columns) {
		int[][] matrix = new int[rows][columns];
		int number = 1;

		for (int rowIdx = 0; rowIdx < rows; rowIdx++) {
			for (int columnIdx = 0; columnIdx < columns; columnIdx++) {
				matrix[rowIdx][columnIdx] = number;
				number++;
			}
		}

		return matrix;
	}

	private String prepareFormattedString(int[] elements) {
		StringBuilder formattedElements = new StringBuilder("{ ");
		for (int index = 0; index < elements.length; index++) {
			int number = elements[index];
			formattedElements.append(number);

			if (index < elements.length - 1) {
				formattedElements.append(", ");
			}
		}
		formattedElements.append(" }");
		return formattedElements.toString();
	}
}

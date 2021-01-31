package org.gb.sample.algo.matrix;

import java.util.ArrayList;
import java.util.List;

public class MatrixOperations {

	public static int[] getElementsInSpiralOrder(final int rows, final int columns, int[][] matrix) {
		List<Integer> elementsInSpiralOrder = new ArrayList<>();

		int lowestRowIdxVisited = -1;
		int highestRowIdxVisited = rows;
		int lowestColumnIdxVisited = -1;
		int highestColumnIdxVisited = columns;
		int currentRowIdx = lowestRowIdxVisited + 1;
		int currentColumnIdx = lowestColumnIdxVisited + 1;
		final int totalElements = rows * columns;

		while (true) {
			if (elementsInSpiralOrder.size() == totalElements) {
				break;
			}

			// Moving left to right on a row.
			while (currentColumnIdx < highestColumnIdxVisited) {
				int element = matrix[currentRowIdx][currentColumnIdx];
				elementsInSpiralOrder.add(element);
				currentColumnIdx++;
			}

			currentColumnIdx--;
			lowestRowIdxVisited = currentRowIdx;
			currentRowIdx++;

			// Moving up to down on a column.
			while (currentRowIdx < highestRowIdxVisited) {
				int element = matrix[currentRowIdx][currentColumnIdx];
				elementsInSpiralOrder.add(element);
				currentRowIdx++;
			}

			currentRowIdx--;
			highestColumnIdxVisited = currentColumnIdx;
			currentColumnIdx--;

			// Moving right to left on a row.
			while (currentColumnIdx > lowestColumnIdxVisited) {
				int element = matrix[currentRowIdx][currentColumnIdx];
				elementsInSpiralOrder.add(element);
				currentColumnIdx--;
			}

			currentColumnIdx++;
			highestRowIdxVisited = currentRowIdx;
			currentRowIdx--;

			// Moving down to up on a column.
			while (currentRowIdx > lowestRowIdxVisited) {
				int element = matrix[currentRowIdx][currentColumnIdx];
				elementsInSpiralOrder.add(element);
				currentRowIdx--;
			}

			currentRowIdx++;
			lowestColumnIdxVisited = currentColumnIdx;
			currentColumnIdx++;
		}

		return elementsInSpiralOrder.stream().mapToInt(element -> element.intValue()).toArray();
	}
}

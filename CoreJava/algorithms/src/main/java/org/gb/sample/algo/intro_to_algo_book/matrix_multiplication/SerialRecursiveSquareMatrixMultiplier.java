package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import org.apache.log4j.Logger;

class SerialRecursiveSquareMatrixMultiplier extends AbstractSquareMatrixMultiplier {

	private static final Logger LOGGER = Logger.getLogger(SerialRecursiveSquareMatrixMultiplier.class);

	@Override
	int[][] multiplySquareMatrices(final int dimension, final int[][] firstMatrix, final int[][] secondMatrix) {
		int[][] productMatrix = new int[dimension][dimension];
		fillProductMatrix(dimension, dimension, dimension, dimension, 0, 0, 0, 0, 0, 0, firstMatrix, secondMatrix, productMatrix);
		return productMatrix;
	}

	private void fillProductMatrix(final int partialMatrix1RowCount, final int partialMatrix1ColumnCount, final int partialMatrix2RowCount, final int partialMatrix2ColumnCount,
			final int firstMatrixStartRowIdx, final int firstMatrixStartColumnIdx, final int secondMatrixStartRowIdx, final int secondMatrixStartColumnIdx,
			final int productMatrixRowIdx, final int productMatrixColumnIdx, final int[][] firstMatrix, final int[][] secondMatrix,
			final int[][] productMatrix) {

		LOGGER.info(String.format(
				"partialMatrix1RowCount: %d, partialMatrix1ColumnCount: %d, partialMatrix2RowCount: %d, partialMatrix2ColumnCount: %d, productMatrixRowIdx: %d, productMatrixColumnIdx: %d",
				partialMatrix1RowCount, partialMatrix1ColumnCount, partialMatrix2RowCount, partialMatrix2ColumnCount, productMatrixRowIdx,
				productMatrixColumnIdx));
		if (partialMatrix1RowCount == 1 && partialMatrix1ColumnCount == 1 && partialMatrix2RowCount == 1 && partialMatrix2ColumnCount == 1) {
			int firstMatrixCellValue = firstMatrix[firstMatrixStartRowIdx][firstMatrixStartColumnIdx];
			int secondMatrixCellValue = secondMatrix[secondMatrixStartRowIdx][secondMatrixStartColumnIdx];
			LOGGER.info(String.format("Setting value for: productMatrix[%d][%d]", productMatrixRowIdx, productMatrixColumnIdx));
			LOGGER.info(String.format("Multiplying firstMatrix[%d][%d] = %d and secondMatrix[%d][%d] = %d", firstMatrixStartRowIdx, firstMatrixStartColumnIdx,
					firstMatrixCellValue, secondMatrixStartRowIdx, secondMatrixStartColumnIdx, secondMatrixCellValue));
			productMatrix[productMatrixRowIdx][productMatrixColumnIdx] += firstMatrixCellValue * secondMatrixCellValue;
		} else {
			int partialMatrix1HalvedRowCount = partialMatrix1RowCount / 2;
			int partialMatrix1RemainderRowCount = partialMatrix1RowCount - partialMatrix1HalvedRowCount;
			int partialMatrix1HalvedColumnCount = partialMatrix1ColumnCount / 2;
			int partialMatrix1RemainderColumnCount = partialMatrix1ColumnCount - partialMatrix1HalvedColumnCount;

			int partialMatrix2HalvedRowCount = partialMatrix2RowCount / 2;
			int partialMatrix2RemainderRowCount = partialMatrix2RowCount - partialMatrix2HalvedRowCount;
			int partialMatrix2HalvedColumnCount = partialMatrix2ColumnCount / 2;
			int partialMatrix2RemainderColumnCount = partialMatrix2ColumnCount - partialMatrix2HalvedColumnCount;
			
			/*
			 * Dimensions for subMatrix_1_11 = partialMatrix1RemainderRowCount x partialMatrix1RemainderColumnCount
			 * Dimensions for subMatrix_1_12 = partialMatrix1RemainderRowCount x partialMatrix1HalvedColumnCount
			 * Dimensions for subMatrix_1_21 = partialMatrix1HalvedRowCount x partialMatrix1RemainderColumnCount
			 * Dimensions for subMatrix_1_22 = partialMatrix1HalvedRowCount x partialMatrix1HalvedColumnCount
			 * 
			 * Dimensions for subMatrix_2_11 = partialMatrix2RemainderRowCount x partialMatrix2RemainderColumnCount
			 * Dimensions for subMatrix_2_12 = partialMatrix2RemainderRowCount x partialMatrix2HalvedColumnCount
			 * Dimensions for subMatrix_2_21 = partialMatrix2HalvedRowCount x partialMatrix2RemainderColumnCount
			 * Dimensions for subMatrix_2_22 = partialMatrix2HalvedRowCount x partialMatrix2HalvedColumnCount
			 */
			
			int subMatrix_1_11_RowCount = (partialMatrix1RowCount == 1) ? 1 : partialMatrix1RemainderRowCount;
			int subMatrix_1_11_ColumnCount = (partialMatrix1ColumnCount == 1) ? 1 : partialMatrix1RemainderColumnCount;

			int subMatrix_1_12_RowCount = subMatrix_1_11_RowCount;
			int subMatrix_1_12_ColumnCount = partialMatrix1ColumnCount - subMatrix_1_11_ColumnCount;

			int subMatrix_1_21_RowCount = partialMatrix1RowCount - subMatrix_1_11_RowCount;
			int subMatrix_1_21_ColumnCount = subMatrix_1_11_ColumnCount;

			int subMatrix_1_22_RowCount = partialMatrix1RowCount - subMatrix_1_11_RowCount;
			int subMatrix_1_22_ColumnCount = partialMatrix1ColumnCount - subMatrix_1_11_ColumnCount;

			int subMatrix_2_11_RowCount = (partialMatrix2RowCount == 1) ? 1 : partialMatrix2RemainderRowCount;
			int subMatrix_2_11_ColumnCount = (partialMatrix2ColumnCount == 1) ? 1 : partialMatrix2RemainderColumnCount;

			int subMatrix_2_12_RowCount = subMatrix_2_11_RowCount;
			int subMatrix_2_12_ColumnCount = partialMatrix2ColumnCount - subMatrix_2_11_ColumnCount;

			int subMatrix_2_21_RowCount = partialMatrix2RowCount - subMatrix_2_11_RowCount;
			int subMatrix_2_21_ColumnCount = subMatrix_2_11_ColumnCount;

			int subMatrix_2_22_RowCount = partialMatrix2RowCount - subMatrix_2_11_RowCount;
			int subMatrix_2_22_ColumnCount = partialMatrix2ColumnCount - subMatrix_2_11_ColumnCount;
			
			LOGGER.info("");
			
			LOGGER.info(String.format(
					"subMatrix_1_11_RowCount: %d, subMatrix_1_11_ColumnCount: %d, subMatrix_1_12_RowCount: %d, subMatrix_1_12_ColumnCount: %d, "
							+ "subMatrix_1_21_RowCount: %d, subMatrix_1_21_ColumnCount: %d, subMatrix_1_22_RowCount: %d, subMatrix_1_22_ColumnCount: %d",
					subMatrix_1_11_RowCount, subMatrix_1_11_ColumnCount, subMatrix_1_12_RowCount, subMatrix_1_12_ColumnCount, subMatrix_1_21_RowCount,
					subMatrix_1_21_ColumnCount, subMatrix_1_22_RowCount, subMatrix_1_22_ColumnCount));
			LOGGER.info(String.format(
					"subMatrix_2_11_RowCount: %d, subMatrix_2_11_ColumnCount: %d, subMatrix_2_12_RowCount: %d, subMatrix_2_12_ColumnCount: %d, "
							+ "subMatrix_2_21_RowCount: %d, subMatrix_2_21_ColumnCount: %d, subMatrix_2_22_RowCount: %d, subMatrix_2_22_ColumnCount: %d",
					subMatrix_2_11_RowCount, subMatrix_2_11_ColumnCount, subMatrix_2_12_RowCount, subMatrix_2_12_ColumnCount, subMatrix_2_21_RowCount,
					subMatrix_2_21_ColumnCount, subMatrix_2_22_RowCount, subMatrix_2_22_ColumnCount));

			LOGGER.info("");
			/*
			 * C(11) = A(11) * B(11) + A(12) * B(21)
			 */
			LOGGER.info("------C(11)------");
			LOGGER.info("Calculating A(11) * B(11)");
			// A(11) * B(11)
			if (subMatrix_1_11_RowCount * subMatrix_1_11_ColumnCount != 0 && subMatrix_2_11_RowCount * subMatrix_2_11_ColumnCount != 0) {
				LOGGER.info(String.format(
						"Calling fillProductMatrix for A(11) * B(11), productMatrixRowIdx: %d, productMatrixColumnIdx: %d, subMatrix_1_11_RowCount: %d, subMatrix_1_11_ColumnCount: %d\n",
						productMatrixRowIdx, productMatrixColumnIdx, subMatrix_1_11_RowCount, subMatrix_1_11_ColumnCount));
				fillProductMatrix(subMatrix_1_11_RowCount, subMatrix_1_11_ColumnCount, subMatrix_2_11_RowCount, subMatrix_2_11_ColumnCount,
						firstMatrixStartRowIdx, firstMatrixStartColumnIdx, secondMatrixStartRowIdx, secondMatrixStartColumnIdx, productMatrixRowIdx,
						productMatrixColumnIdx, firstMatrix, secondMatrix, productMatrix);
			} else {
				LOGGER.info("One or both of A(11) and B(11) have zero dimensions, skipping calculations.");
			}

			LOGGER.info("");
			LOGGER.info("Calculating A(12) * B(21)");
			// A(12) * B(21)
			if (subMatrix_1_12_RowCount * subMatrix_1_12_ColumnCount != 0 && subMatrix_2_21_RowCount * subMatrix_2_21_ColumnCount != 0) {
				LOGGER.info(String.format(
						"Calling fillProductMatrix for A(12) * B(21), productMatrixRowIdx: %d, productMatrixColumnIdx: %d, subMatrix_1_11_RowCount: %d, subMatrix_1_11_ColumnCount: %d\n",
						productMatrixRowIdx, productMatrixColumnIdx, subMatrix_1_11_RowCount, subMatrix_1_11_ColumnCount));
				fillProductMatrix(subMatrix_1_12_RowCount, subMatrix_1_12_ColumnCount, subMatrix_2_21_RowCount, subMatrix_2_21_ColumnCount,
						firstMatrixStartRowIdx, firstMatrixStartColumnIdx + subMatrix_1_11_ColumnCount, secondMatrixStartRowIdx + subMatrix_2_11_RowCount,
						secondMatrixStartColumnIdx, productMatrixRowIdx, productMatrixColumnIdx, firstMatrix, secondMatrix, productMatrix);
			} else {
				LOGGER.info("One or both of A(12) and B(21) have zero dimensions, skipping calculations.");
			}

			LOGGER.info("");
			/*
			 * C(12) = A(11) * B(12) + A(12) * B(22)
			 */
			LOGGER.info("------C(12)------");
			LOGGER.info("Calculating A(11) * B(12)");
			// A(11) * B(12)
			if (subMatrix_1_11_RowCount * subMatrix_1_11_ColumnCount != 0 && subMatrix_2_12_RowCount * subMatrix_2_12_ColumnCount != 0) {
				LOGGER.info(String.format(
						"Calling fillProductMatrix for A(11) * B(12), productMatrixRowIdx: %d, productMatrixColumnIdx: %d, subMatrix_1_11_RowCount: %d, subMatrix_1_11_ColumnCount: %d\n",
						productMatrixRowIdx, productMatrixColumnIdx + subMatrix_1_11_ColumnCount, subMatrix_1_11_RowCount, subMatrix_1_11_ColumnCount));
				fillProductMatrix(subMatrix_1_11_RowCount, subMatrix_1_11_ColumnCount, subMatrix_2_12_RowCount, subMatrix_2_12_ColumnCount,
						firstMatrixStartRowIdx, firstMatrixStartColumnIdx, secondMatrixStartRowIdx, secondMatrixStartColumnIdx + subMatrix_2_11_ColumnCount,
						productMatrixRowIdx, productMatrixColumnIdx + subMatrix_1_11_ColumnCount, firstMatrix, secondMatrix, productMatrix);
			} else {
				LOGGER.info("One or both of A(11) and B(12) have zero dimensions, skipping calculations.");
			}

			LOGGER.info("");
			LOGGER.info("Calculating A(12) * B(22)");
			// A(12) * B(22)
			if (subMatrix_1_12_RowCount * subMatrix_1_12_ColumnCount != 0 && subMatrix_2_22_RowCount * subMatrix_2_22_ColumnCount != 0) {
				LOGGER.info(String.format(
						"Calling fillProductMatrix for A(12) * B(22), productMatrixRowIdx: %d, productMatrixColumnIdx: %d, subMatrix_1_11_RowCount: %d, subMatrix_1_11_ColumnCount: %d\n",
						productMatrixRowIdx, productMatrixColumnIdx + subMatrix_1_11_ColumnCount, subMatrix_1_11_RowCount, subMatrix_1_11_ColumnCount));
				fillProductMatrix(subMatrix_1_12_RowCount, subMatrix_1_12_ColumnCount, subMatrix_2_22_RowCount, subMatrix_2_22_ColumnCount,
						firstMatrixStartRowIdx, firstMatrixStartColumnIdx + subMatrix_1_11_ColumnCount, secondMatrixStartRowIdx + subMatrix_2_11_RowCount,
						secondMatrixStartColumnIdx + subMatrix_2_11_ColumnCount, productMatrixRowIdx, productMatrixColumnIdx + subMatrix_1_11_ColumnCount,
						firstMatrix, secondMatrix, productMatrix);
			} else {
				LOGGER.info("One or both of A(12) and B(22) have zero dimensions, skipping calculations.");
			}
		}
	}
}

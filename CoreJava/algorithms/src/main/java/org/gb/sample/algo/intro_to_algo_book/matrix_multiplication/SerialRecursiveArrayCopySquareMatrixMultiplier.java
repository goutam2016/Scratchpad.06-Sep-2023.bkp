package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import org.apache.log4j.Logger;

class SerialRecursiveArrayCopySquareMatrixMultiplier extends AbstractSquareMatrixMultiplier {
	
	private static final Logger LOGGER = Logger.getLogger(SerialRecursiveArrayCopySquareMatrixMultiplier.class);

	@Override
	int[][] multiplySquareMatrices(int dimension, int[][] firstMatrix, int[][] secondMatrix) {
		return computeProductMatrix(dimension, dimension, dimension, dimension, 0, 0, 0, 0, firstMatrix, secondMatrix);
	}

	private int[][] computeProductMatrix(final int partialMatrix1RowCount, final int partialMatrix1ColumnCount, final int partialMatrix2RowCount, final int partialMatrix2ColumnCount, 
			final int firstMatrixStartRowIdx, final int firstMatrixStartColumnIdx, final int secondMatrixStartRowIdx, final int secondMatrixStartColumnIdx, 
			final int[][] firstMatrix, final int[][] secondMatrix) {
		if (partialMatrix1RowCount == 1 && partialMatrix1ColumnCount == 1 && partialMatrix2RowCount == 1 && partialMatrix2ColumnCount == 1) {
			int[][] productMatrix = new int[1][1];
			productMatrix[0][0] = firstMatrix[firstMatrixStartRowIdx][firstMatrixStartColumnIdx]
					* secondMatrix[secondMatrixStartRowIdx][secondMatrixStartColumnIdx];
			return productMatrix;
		} else {
			int partialMatrix1HalvedRowCount = partialMatrix1RowCount / 2;
			int partialMatrix1RemainderRowCount = partialMatrix1RowCount - partialMatrix1HalvedRowCount;
			int partialMatrix1HalvedColumnCount = partialMatrix1ColumnCount / 2;
			int partialMatrix1RemainderColumnCount = partialMatrix1ColumnCount - partialMatrix1HalvedColumnCount;

			int partialMatrix2HalvedRowCount = partialMatrix2RowCount / 2;
			int partialMatrix2RemainderRowCount = partialMatrix2RowCount - partialMatrix2HalvedRowCount;
			int partialMatrix2HalvedColumnCount = partialMatrix2ColumnCount / 2;
			int partialMatrix2RemainderColumnCount = partialMatrix2ColumnCount - partialMatrix2HalvedColumnCount;

			int subMatrix_1_11_RowCount = partialMatrix1RemainderRowCount;
			int subMatrix_1_11_ColumnCount = partialMatrix1RemainderColumnCount;

			int subMatrix_1_12_RowCount = subMatrix_1_11_RowCount;
			int subMatrix_1_12_ColumnCount = partialMatrix1ColumnCount - subMatrix_1_11_ColumnCount;

			int subMatrix_1_21_RowCount = partialMatrix1RowCount - subMatrix_1_11_RowCount;
			int subMatrix_1_21_ColumnCount = subMatrix_1_11_ColumnCount;

			int subMatrix_1_22_RowCount = partialMatrix1RowCount - subMatrix_1_11_RowCount;
			int subMatrix_1_22_ColumnCount = partialMatrix1ColumnCount - subMatrix_1_11_ColumnCount;

			int subMatrix_2_11_RowCount = partialMatrix2RemainderRowCount;
			int subMatrix_2_11_ColumnCount = partialMatrix2RemainderColumnCount;

			int subMatrix_2_12_RowCount = subMatrix_2_11_RowCount;
			int subMatrix_2_12_ColumnCount = partialMatrix2ColumnCount - subMatrix_2_11_ColumnCount;

			int subMatrix_2_21_RowCount = partialMatrix2RowCount - subMatrix_2_11_RowCount;
			int subMatrix_2_21_ColumnCount = subMatrix_2_11_ColumnCount;

			int subMatrix_2_22_RowCount = partialMatrix2RowCount - subMatrix_2_11_RowCount;
			int subMatrix_2_22_ColumnCount = partialMatrix2ColumnCount - subMatrix_2_11_ColumnCount;
			
			// A(11) * B(11)
			int[][] partialProductMatrix1 = new int[subMatrix_1_11_RowCount][subMatrix_2_11_ColumnCount];

			if (subMatrix_1_11_RowCount * subMatrix_1_11_ColumnCount != 0 && subMatrix_2_11_RowCount * subMatrix_2_11_ColumnCount != 0) {
				partialProductMatrix1 = computeProductMatrix(subMatrix_1_11_RowCount, subMatrix_1_11_ColumnCount, subMatrix_2_11_RowCount,
						subMatrix_2_11_ColumnCount, firstMatrixStartRowIdx, firstMatrixStartColumnIdx, secondMatrixStartRowIdx, secondMatrixStartColumnIdx,
						firstMatrix, secondMatrix);
			}

			// A(12) * B(21)
			int[][] partialProductMatrix2 = new int[subMatrix_1_12_RowCount][subMatrix_2_21_ColumnCount];
			
			if (subMatrix_1_12_RowCount * subMatrix_1_12_ColumnCount != 0 && subMatrix_2_21_RowCount * subMatrix_2_21_ColumnCount != 0) {
				partialProductMatrix2 = computeProductMatrix(subMatrix_1_12_RowCount, subMatrix_1_12_ColumnCount, subMatrix_2_21_RowCount,
						subMatrix_2_21_ColumnCount, firstMatrixStartRowIdx, firstMatrixStartColumnIdx + subMatrix_1_11_ColumnCount,
						secondMatrixStartRowIdx + subMatrix_2_11_RowCount, secondMatrixStartColumnIdx, firstMatrix, secondMatrix);
			}

			// A(11) * B(12)
			int[][] partialProductMatrix3 = new int[subMatrix_1_11_RowCount][subMatrix_2_12_ColumnCount];
			
			if (subMatrix_1_11_RowCount * subMatrix_1_11_ColumnCount != 0 && subMatrix_2_12_RowCount * subMatrix_2_12_ColumnCount != 0) {
				partialProductMatrix3 = computeProductMatrix(subMatrix_1_11_RowCount, subMatrix_1_11_ColumnCount, subMatrix_2_12_RowCount,
						subMatrix_2_12_ColumnCount, firstMatrixStartRowIdx, firstMatrixStartColumnIdx, secondMatrixStartRowIdx,
						secondMatrixStartColumnIdx + subMatrix_2_11_ColumnCount, firstMatrix, secondMatrix);
			}

			// A(12) * B(22)
			int[][] partialProductMatrix4 = new int[subMatrix_1_12_RowCount][subMatrix_2_22_ColumnCount];
			
			if (subMatrix_1_12_RowCount * subMatrix_1_12_ColumnCount != 0 && subMatrix_2_22_RowCount * subMatrix_2_22_ColumnCount != 0) {
				partialProductMatrix4 = computeProductMatrix(subMatrix_1_12_RowCount, subMatrix_1_12_ColumnCount, subMatrix_2_22_RowCount,
						subMatrix_2_22_ColumnCount, firstMatrixStartRowIdx, firstMatrixStartColumnIdx + subMatrix_1_11_ColumnCount,
						secondMatrixStartRowIdx + subMatrix_2_11_RowCount, secondMatrixStartColumnIdx + subMatrix_2_11_ColumnCount, firstMatrix, secondMatrix);
			}

			// A(21) * B(11)
			int[][] partialProductMatrix5 = new int[subMatrix_1_21_RowCount][subMatrix_2_11_ColumnCount];
			
			if (subMatrix_1_21_RowCount * subMatrix_1_21_ColumnCount != 0 && subMatrix_2_11_RowCount * subMatrix_2_11_ColumnCount != 0) {
				partialProductMatrix5 = computeProductMatrix(subMatrix_1_21_RowCount, subMatrix_1_21_ColumnCount, subMatrix_2_11_RowCount,
						subMatrix_2_11_ColumnCount, firstMatrixStartRowIdx + subMatrix_1_11_RowCount, firstMatrixStartColumnIdx, secondMatrixStartRowIdx,
						secondMatrixStartColumnIdx, firstMatrix, secondMatrix);
			}

			// A(22) * B(21)
			int[][] partialProductMatrix6 = new int[subMatrix_1_22_RowCount][subMatrix_2_21_ColumnCount];
			
			if (subMatrix_1_22_RowCount * subMatrix_1_22_ColumnCount != 0 && subMatrix_2_21_RowCount * subMatrix_2_21_ColumnCount != 0) {
				partialProductMatrix6 = computeProductMatrix(subMatrix_1_22_RowCount, subMatrix_1_22_ColumnCount, subMatrix_2_22_RowCount,
						subMatrix_2_22_ColumnCount, firstMatrixStartRowIdx + subMatrix_1_11_RowCount, firstMatrixStartColumnIdx + subMatrix_1_22_ColumnCount,
						secondMatrixStartRowIdx + subMatrix_2_11_RowCount, secondMatrixStartColumnIdx, firstMatrix, secondMatrix);
			}

			// A(21) * B(12)
			int[][] partialProductMatrix7 = new int[subMatrix_1_21_RowCount][subMatrix_2_12_ColumnCount];
			
			if (subMatrix_1_21_RowCount * subMatrix_1_21_ColumnCount != 0 && subMatrix_2_12_RowCount * subMatrix_2_12_ColumnCount != 0) {
				partialProductMatrix7 = computeProductMatrix(subMatrix_1_21_RowCount, subMatrix_1_21_ColumnCount, subMatrix_2_12_RowCount,
						subMatrix_2_12_ColumnCount, firstMatrixStartRowIdx + subMatrix_1_11_RowCount, firstMatrixStartColumnIdx,
						secondMatrixStartRowIdx, secondMatrixStartColumnIdx + subMatrix_2_11_ColumnCount, firstMatrix, secondMatrix);
			}

			// A(22) * B(22)
			int[][] partialProductMatrix8 = new int[subMatrix_1_22_RowCount][subMatrix_2_22_ColumnCount];
			
			if (subMatrix_1_22_RowCount * subMatrix_1_22_ColumnCount != 0 && subMatrix_2_22_RowCount * subMatrix_2_22_ColumnCount != 0) {
				partialProductMatrix8 = computeProductMatrix(subMatrix_1_22_RowCount, subMatrix_1_22_ColumnCount, subMatrix_2_22_RowCount,
						subMatrix_2_22_ColumnCount, firstMatrixStartRowIdx + subMatrix_1_11_RowCount, firstMatrixStartColumnIdx + subMatrix_1_22_ColumnCount,
						secondMatrixStartRowIdx + subMatrix_2_11_RowCount, secondMatrixStartColumnIdx + subMatrix_2_11_ColumnCount, firstMatrix, secondMatrix);
			}

			// C(11) = A(11) * B(11) + A(12) * B(21)
			int[][] quarterProductMatrix1 = addMatrices(subMatrix_1_11_RowCount, subMatrix_2_11_ColumnCount, partialProductMatrix1, partialProductMatrix2);
			
			// C(12) = A(11) * B(12) + A(12) * B(22)
			int[][] quarterProductMatrix2 = addMatrices(subMatrix_1_11_RowCount, subMatrix_2_12_ColumnCount, partialProductMatrix3, partialProductMatrix4);
			
			// C(21) = A(21) * B(11) + A(22) * B(21)
			int[][] quarterProductMatrix3 = addMatrices(subMatrix_1_21_RowCount, subMatrix_2_11_ColumnCount, partialProductMatrix5, partialProductMatrix6);
			
			// C(22) = A(21) * B(12) + A(22) * B(22)
			int[][] quarterProductMatrix4 = addMatrices(subMatrix_1_21_RowCount, subMatrix_2_12_ColumnCount, partialProductMatrix7, partialProductMatrix8);

			int[][] productMatrix = new int[partialMatrix1RowCount][partialMatrix2ColumnCount];
			
			for (int i = 0; i < subMatrix_1_11_RowCount; i++) {
				for (int j = 0; j < subMatrix_2_11_ColumnCount; j++) {
					productMatrix[i][j] = quarterProductMatrix1[i][j];
				}
			}
			for (int i = 0; i < subMatrix_1_11_RowCount; i++) {
				for (int j = subMatrix_2_11_ColumnCount; j < partialMatrix2ColumnCount; j++) {
					productMatrix[i][j] = quarterProductMatrix2[i][j - subMatrix_2_11_ColumnCount];
				}
			}
			for (int i = subMatrix_1_11_RowCount; i < partialMatrix1RowCount; i++) {
				for (int j = 0; j < subMatrix_2_11_ColumnCount; j++) {
					productMatrix[i][j] = quarterProductMatrix3[i - subMatrix_1_11_RowCount][j];
				}
			}
			for (int i = subMatrix_1_11_RowCount; i < partialMatrix1RowCount; i++) {
				for (int j = subMatrix_2_11_ColumnCount; j < partialMatrix2ColumnCount; j++) {
					productMatrix[i][j] = quarterProductMatrix4[i - subMatrix_1_11_RowCount][j - subMatrix_2_11_ColumnCount];
				}
			}

			return productMatrix;
		}
	}

	private int[][] addMatrices(int rowCount, int columnCount, int[][] matrix1, int[][] matrix2) {
		LOGGER.info(String.format("rowCount: %d, columnCount: %d", rowCount, columnCount));
		
		LOGGER.info("matrix1 -->");
		printMatrix(matrix1);

		LOGGER.info("matrix2 -->");
		printMatrix(matrix2);
		
		LOGGER.info("");

		int[][] resultMatrix = new int[rowCount][columnCount];

		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				resultMatrix[i][j] = matrix1[i][j];
				
				if(matrix2.length > i) {
					if(matrix2[0].length > j) {
						resultMatrix[i][j] += matrix2[i][j];
					}
				}
			}
		}
		
		return resultMatrix;
	}
	
	private void printMatrix(int[][] matrix) {
		int rowCnt = matrix.length;

		for (int i = 0; i < rowCnt; i++) {
			int[] row = matrix[i];
			int columnCnt = row.length;
			StringBuilder rowFormatter = new StringBuilder();

			for (int j = 0; j < columnCnt; j++) {
				int cellValue = row[j];
				rowFormatter.append(cellValue).append(" ");
			}

			LOGGER.info(rowFormatter.substring(0));
		}
	}

}

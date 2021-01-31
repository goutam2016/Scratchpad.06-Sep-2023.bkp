package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

class SerialRecursiveArrayCopySquareMatrixMultiplier_bkp extends AbstractSquareMatrixMultiplier {

	@Override
	int[][] multiplySquareMatrices(int dimension, int[][] firstMatrix, int[][] secondMatrix) {
		return computeProductMatrix(dimension, 0, 0, 0, 0, firstMatrix, secondMatrix);
	}

	private int[][] computeProductMatrix(final int dimension, final int firstMatrixStartRowIdx, final int firstMatrixStartColumnIdx,
			final int secondMatrixStartRowIdx, final int secondMatrixStartColumnIdx, final int[][] firstMatrix, final int[][] secondMatrix) {
		if (dimension == 1) {
			int[][] productMatrix = new int[1][1];
			productMatrix[0][0] = firstMatrix[firstMatrixStartRowIdx][firstMatrixStartColumnIdx]
					* secondMatrix[secondMatrixStartRowIdx][secondMatrixStartColumnIdx];
			return productMatrix;
		} else {
			int halvedDimension = dimension / 2;

			int[][] partialProductMatrix1 = computeProductMatrix(halvedDimension, firstMatrixStartRowIdx, firstMatrixStartColumnIdx, secondMatrixStartRowIdx,
					secondMatrixStartColumnIdx, firstMatrix, secondMatrix);

			int[][] partialProductMatrix2 = computeProductMatrix(halvedDimension, firstMatrixStartRowIdx, firstMatrixStartColumnIdx + halvedDimension,
					secondMatrixStartRowIdx + halvedDimension, secondMatrixStartColumnIdx, firstMatrix, secondMatrix);

			int[][] partialProductMatrix3 = computeProductMatrix(halvedDimension, firstMatrixStartRowIdx, firstMatrixStartColumnIdx, secondMatrixStartRowIdx,
					secondMatrixStartColumnIdx + halvedDimension, firstMatrix, secondMatrix);

			int[][] partialProductMatrix4 = computeProductMatrix(halvedDimension, firstMatrixStartRowIdx, firstMatrixStartColumnIdx + halvedDimension,
					secondMatrixStartRowIdx + halvedDimension, secondMatrixStartColumnIdx + halvedDimension, firstMatrix, secondMatrix);

			int[][] partialProductMatrix5 = computeProductMatrix(halvedDimension, firstMatrixStartRowIdx + halvedDimension, firstMatrixStartColumnIdx,
					secondMatrixStartRowIdx, secondMatrixStartColumnIdx, firstMatrix, secondMatrix);

			int[][] partialProductMatrix6 = computeProductMatrix(halvedDimension, firstMatrixStartRowIdx + halvedDimension,
					firstMatrixStartColumnIdx + halvedDimension, secondMatrixStartRowIdx + halvedDimension, secondMatrixStartColumnIdx, firstMatrix,
					secondMatrix);

			int[][] partialProductMatrix7 = computeProductMatrix(halvedDimension, firstMatrixStartRowIdx + halvedDimension, firstMatrixStartColumnIdx,
					secondMatrixStartRowIdx, secondMatrixStartColumnIdx + halvedDimension, firstMatrix, secondMatrix);

			int[][] partialProductMatrix8 = computeProductMatrix(halvedDimension, firstMatrixStartRowIdx + halvedDimension,
					firstMatrixStartColumnIdx + halvedDimension, secondMatrixStartRowIdx + halvedDimension, secondMatrixStartColumnIdx + halvedDimension,
					firstMatrix, secondMatrix);

			int[][] quarterProductMatrix1 = addMatrices(halvedDimension, partialProductMatrix1, partialProductMatrix2);
			int[][] quarterProductMatrix2 = addMatrices(halvedDimension, partialProductMatrix3, partialProductMatrix4);
			int[][] quarterProductMatrix3 = addMatrices(halvedDimension, partialProductMatrix5, partialProductMatrix6);
			int[][] quarterProductMatrix4 = addMatrices(halvedDimension, partialProductMatrix7, partialProductMatrix8);

			int[][] productMatrix = new int[dimension][dimension];

			for (int i = 0; i < halvedDimension; i++) {
				for (int j = 0; j < halvedDimension; j++) {
					productMatrix[i][j] = quarterProductMatrix1[i][j];
				}
			}
			for (int i = 0; i < halvedDimension; i++) {
				for (int j = halvedDimension; j < dimension; j++) {
					productMatrix[i][j] = quarterProductMatrix2[i][j - halvedDimension];
				}
			}
			for (int i = halvedDimension; i < dimension; i++) {
				for (int j = 0; j < halvedDimension; j++) {
					productMatrix[i][j] = quarterProductMatrix3[i - halvedDimension][j];
				}
			}
			for (int i = halvedDimension; i < dimension; i++) {
				for (int j = halvedDimension; j < dimension; j++) {
					productMatrix[i][j] = quarterProductMatrix4[i - halvedDimension][j - halvedDimension];
				}
			}

			return productMatrix;
		}
	}

	private int[][] addMatrices(int dimension, int[][] matrix1, int[][] matrix2) {
		int[][] resultMatrix = new int[dimension][dimension];

		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				resultMatrix[i][j] = matrix1[i][j] + matrix2[i][j];
			}
		}

		return resultMatrix;
	}
}

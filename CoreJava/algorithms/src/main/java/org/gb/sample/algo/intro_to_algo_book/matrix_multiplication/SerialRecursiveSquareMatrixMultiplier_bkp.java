package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

class SerialRecursiveSquareMatrixMultiplier_bkp extends AbstractSquareMatrixMultiplier {

	@Override
	int[][] multiplySquareMatrices(final int dimension, final int[][] firstMatrix, final int[][] secondMatrix) {
		int[][] productMatrix = new int[dimension][dimension];
		fillProductMatrix(dimension, 0, 0, 0, 0, 0, 0, firstMatrix, secondMatrix, productMatrix);
		return productMatrix;
	}

	private void fillProductMatrix(final int dimension, final int firstMatrixStartRowIdx, final int firstMatrixStartColumnIdx,
			final int secondMatrixStartRowIdx, final int secondMatrixStartColumnIdx, final int productMatrixRowIdx, final int productMatrixColumnIdx,
			final int[][] firstMatrix, final int[][] secondMatrix, final int[][] productMatrix) {
		if (dimension == 1) {
			productMatrix[productMatrixRowIdx][productMatrixColumnIdx] += firstMatrix[firstMatrixStartRowIdx][firstMatrixStartColumnIdx]
					* secondMatrix[secondMatrixStartRowIdx][secondMatrixStartColumnIdx];
		} else {
			int halvedDimension = dimension / 2;

			fillProductMatrix(halvedDimension, firstMatrixStartRowIdx, firstMatrixStartColumnIdx, secondMatrixStartRowIdx, secondMatrixStartColumnIdx,
					productMatrixRowIdx, productMatrixColumnIdx, firstMatrix, secondMatrix, productMatrix);

			fillProductMatrix(halvedDimension, firstMatrixStartRowIdx, firstMatrixStartColumnIdx + halvedDimension, secondMatrixStartRowIdx + halvedDimension,
					secondMatrixStartColumnIdx, productMatrixRowIdx, productMatrixColumnIdx, firstMatrix, secondMatrix, productMatrix);

			fillProductMatrix(halvedDimension, firstMatrixStartRowIdx, firstMatrixStartColumnIdx, secondMatrixStartRowIdx,
					secondMatrixStartColumnIdx + halvedDimension, productMatrixRowIdx, productMatrixColumnIdx + halvedDimension, firstMatrix, secondMatrix,
					productMatrix);

			fillProductMatrix(halvedDimension, firstMatrixStartRowIdx, firstMatrixStartColumnIdx + halvedDimension, secondMatrixStartRowIdx + halvedDimension,
					secondMatrixStartColumnIdx + halvedDimension, productMatrixRowIdx, productMatrixColumnIdx + halvedDimension, firstMatrix, secondMatrix,
					productMatrix);

			fillProductMatrix(halvedDimension, firstMatrixStartRowIdx + halvedDimension, firstMatrixStartColumnIdx, secondMatrixStartRowIdx,
					secondMatrixStartColumnIdx, productMatrixRowIdx + halvedDimension, productMatrixColumnIdx, firstMatrix, secondMatrix, productMatrix);

			fillProductMatrix(halvedDimension, firstMatrixStartRowIdx + halvedDimension, firstMatrixStartColumnIdx + halvedDimension,
					secondMatrixStartRowIdx + halvedDimension, secondMatrixStartColumnIdx, productMatrixRowIdx + halvedDimension, productMatrixColumnIdx,
					firstMatrix, secondMatrix, productMatrix);

			fillProductMatrix(halvedDimension, firstMatrixStartRowIdx + halvedDimension, firstMatrixStartColumnIdx, secondMatrixStartRowIdx,
					secondMatrixStartColumnIdx + halvedDimension, productMatrixRowIdx + halvedDimension, productMatrixColumnIdx + halvedDimension, firstMatrix,
					secondMatrix, productMatrix);

			fillProductMatrix(halvedDimension, firstMatrixStartRowIdx + halvedDimension, firstMatrixStartColumnIdx + halvedDimension,
					secondMatrixStartRowIdx + halvedDimension, secondMatrixStartColumnIdx + halvedDimension, productMatrixRowIdx + halvedDimension,
					productMatrixColumnIdx + halvedDimension, firstMatrix, secondMatrix, productMatrix);
		}
	}
}

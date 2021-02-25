package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import org.apache.log4j.Logger;

class SerialRecursiveSquareMatrixMultiplier_bkp extends AbstractSquareMatrixMultiplier {

	private static final Logger LOGGER = Logger.getLogger(SerialRecursiveSquareMatrixMultiplier_bkp.class);

	@Override
	int[][] multiplySquareMatrices(final int dimension, final int[][] firstMatrixData, final int[][] secondMatrixData) {
		Matrix2 firstMatrix = new Matrix2(dimension, dimension, firstMatrixData);
		Matrix2 secondMatrix = new Matrix2(dimension, dimension, secondMatrixData);
		Matrix2 productMatrix = computeProductMatrix(firstMatrix, secondMatrix);
		return productMatrix.getData();
	}

	private Matrix2 computeProductMatrix(Matrix2 firstMatrix, Matrix2 secondMatrix) {
		int firstMtxRowCnt = firstMatrix.getRowCount();
		int firstMtxColCnt = firstMatrix.getColumnCount();
		int secondMtxRowCnt = secondMatrix.getRowCount();
		int secondMtxColCnt = secondMatrix.getColumnCount();
		int[][] firstMatrixData = firstMatrix.getData();
		int[][] secondMatrixData = secondMatrix.getData();

		if (firstMtxRowCnt == 1 && firstMtxColCnt == 1 && secondMtxRowCnt == 1 && secondMtxColCnt == 1) {
			int firstMtxVal = firstMatrixData[0][0];
			int secondMtxVal = secondMatrixData[0][0];
			int[][] prodMtxData = { { firstMtxVal * secondMtxVal } };
			return new Matrix2(1, 1, prodMtxData);
		}

		int firstMtxHalvedRowCnt = firstMtxRowCnt / 2;
		int firstMtxRemainderRowCnt = firstMtxRowCnt - firstMtxHalvedRowCnt;
		int firstMtxHalvedColCnt = firstMtxColCnt / 2;
		int firstMtxRemainderColCnt = firstMtxColCnt - firstMtxHalvedColCnt;

		int secondMtxHalvedRowCnt = secondMtxRowCnt / 2;
		int secondMtxRemainderRowCnt = secondMtxRowCnt - secondMtxHalvedRowCnt;
		int secondMtxHalvedColCnt = secondMtxColCnt / 2;
		int secondMtxRemainderColCnt = secondMtxColCnt - secondMtxHalvedColCnt;

//		LOGGER.info(String.format("firstMtxHalvedRowCnt: %d, firstMtxRemainderRowCnt: %d, firstMtxHalvedColCnt: %d, firstMtxRemainderColCnt: %d",
//				firstMtxHalvedRowCnt, firstMtxRemainderRowCnt, firstMtxHalvedColCnt, firstMtxRemainderColCnt));
//		LOGGER.info(String.format("secondMtxHalvedRowCnt: %d, secondMtxRemainderRowCnt: %d, secondMtxHalvedColCnt: %d, secondMtxRemainderColCnt: %d",
//				secondMtxHalvedRowCnt, secondMtxRemainderRowCnt, secondMtxHalvedColCnt, secondMtxRemainderColCnt));

		int firstMtxTopLeftQtrRowCnt = firstMtxRemainderRowCnt;
		int firstMtxTopLeftQtrColCnt = firstMtxRemainderColCnt;
		int firstMtxTopRightQtrRowCnt = firstMtxTopLeftQtrRowCnt;
		int firstMtxTopRightQtrColCnt = firstMtxColCnt - firstMtxTopLeftQtrColCnt;
		int firstMtxBottomLeftQtrRowCnt = firstMtxRowCnt - firstMtxTopLeftQtrRowCnt;
		int firstMtxBottomLeftQtrColCnt = firstMtxTopLeftQtrColCnt;
		int firstMtxBottomRightQtrRowCnt = firstMtxBottomLeftQtrRowCnt;
		int firstMtxBottomRightQtrColCnt = firstMtxTopRightQtrColCnt;

//		LOGGER.info(String.format("firstMtxTopLeftQtrRowCnt: %d, firstMtxTopLeftQtrColCnt: %d, firstMtxTopRightQtrRowCnt: %d, firstMtxTopRightQtrColCnt: %d",
//				firstMtxTopLeftQtrRowCnt, firstMtxTopLeftQtrColCnt, firstMtxTopRightQtrRowCnt, firstMtxTopRightQtrColCnt));
//		LOGGER.info(String.format(
//				"firstMtxBottomLeftQtrRowCnt: %d, firstMtxBottomLeftQtrColCnt: %d, firstMtxBottomRightQtrRowCnt: %d, firstMtxBottomRightQtrColCnt: %d",
//				firstMtxBottomLeftQtrRowCnt, firstMtxBottomLeftQtrColCnt, firstMtxBottomRightQtrRowCnt, firstMtxBottomRightQtrColCnt));

		int secondMtxTopLeftQtrRowCnt = secondMtxRemainderRowCnt;
		int secondMtxTopLeftQtrColCnt = secondMtxRemainderColCnt;
		int secondMtxTopRightQtrRowCnt = secondMtxTopLeftQtrRowCnt;
		int secondMtxTopRightQtrColCnt = secondMtxColCnt - secondMtxTopLeftQtrColCnt;
		int secondMtxBottomLeftQtrRowCnt = secondMtxRowCnt - secondMtxTopLeftQtrRowCnt;
		int secondMtxBottomLeftQtrColCnt = secondMtxTopLeftQtrColCnt;
		int secondMtxBottomRightQtrRowCnt = secondMtxBottomLeftQtrRowCnt;
		int secondMtxBottomRightQtrColCnt = secondMtxTopRightQtrColCnt;

//		LOGGER.info(
//				String.format("secondMtxTopLeftQtrRowCnt: %d, secondMtxTopLeftQtrColCnt: %d, secondMtxTopRightQtrRowCnt: %d, secondMtxTopRightQtrColCnt: %d",
//						secondMtxTopLeftQtrRowCnt, secondMtxTopLeftQtrColCnt, secondMtxTopRightQtrRowCnt, secondMtxTopRightQtrColCnt));
//		LOGGER.info(String.format(
//				"secondMtxBottomLeftQtrRowCnt: %d, secondMtxBottomLeftQtrColCnt: %d, secondMtxBottomRightQtrRowCnt: %d, secondMtxBottomRightQtrColCnt: %d",
//				secondMtxBottomLeftQtrRowCnt, secondMtxBottomLeftQtrColCnt, secondMtxBottomRightQtrRowCnt, secondMtxBottomRightQtrColCnt));

		int[][] firstMtxTopLeftQtrMtxData = new int[firstMtxTopLeftQtrRowCnt][firstMtxTopLeftQtrColCnt];
		fill(firstMatrixData, 0, 0, firstMtxTopLeftQtrMtxData, firstMtxTopLeftQtrRowCnt, firstMtxTopLeftQtrColCnt);

		int[][] firstMtxTopRightQtrMtxData = new int[firstMtxTopRightQtrRowCnt][firstMtxTopRightQtrColCnt];
		fill(firstMatrixData, 0, firstMtxTopLeftQtrColCnt, firstMtxTopRightQtrMtxData, firstMtxTopRightQtrRowCnt, firstMtxTopRightQtrColCnt);

		int[][] firstMtxBottomLeftQtrMtxData = new int[firstMtxBottomLeftQtrRowCnt][firstMtxBottomLeftQtrColCnt];
		fill(firstMatrixData, firstMtxTopLeftQtrRowCnt, 0, firstMtxBottomLeftQtrMtxData, firstMtxBottomLeftQtrRowCnt, firstMtxBottomLeftQtrColCnt);

		int[][] firstMtxBottomRightQtrMtxData = new int[firstMtxBottomRightQtrRowCnt][firstMtxBottomRightQtrColCnt];
		fill(firstMatrixData, firstMtxTopLeftQtrRowCnt, firstMtxTopLeftQtrColCnt, firstMtxBottomRightQtrMtxData, firstMtxBottomRightQtrRowCnt,
				firstMtxBottomRightQtrColCnt);

		Matrix2 firstMtxTopLeftQtrMtx = new Matrix2(firstMtxTopLeftQtrRowCnt, firstMtxTopLeftQtrColCnt, firstMtxTopLeftQtrMtxData);
		Matrix2 firstMtxTopRightQtrMtx = new Matrix2(firstMtxTopRightQtrRowCnt, firstMtxTopRightQtrColCnt, firstMtxTopRightQtrMtxData);
		Matrix2 firstMtxBottomLeftQtrMtx = new Matrix2(firstMtxBottomLeftQtrRowCnt, firstMtxBottomLeftQtrColCnt, firstMtxBottomLeftQtrMtxData);
		Matrix2 firstMtxBottomRightQtrMtx = new Matrix2(firstMtxBottomRightQtrRowCnt, firstMtxBottomRightQtrColCnt, firstMtxBottomRightQtrMtxData);

//		System.out.println("Printing quarter matrices of first matrix.");
//		print(firstMtxTopLeftQtrMtxData);
//		System.out.println();
//		print(firstMtxTopRightQtrMtxData);
//		System.out.println();
//		print(firstMtxBottomLeftQtrMtxData);
//		System.out.println();
//		print(firstMtxBottomRightQtrMtxData);
//		System.out.println();

		int[][] secondMtxTopLeftQtrMtxData = new int[secondMtxTopLeftQtrRowCnt][secondMtxTopLeftQtrColCnt];
		fill(secondMatrixData, 0, 0, secondMtxTopLeftQtrMtxData, secondMtxTopLeftQtrRowCnt, secondMtxTopLeftQtrColCnt);

		int[][] secondMtxTopRightQtrMtxData = new int[secondMtxTopRightQtrRowCnt][secondMtxTopRightQtrColCnt];
		fill(secondMatrixData, 0, secondMtxTopLeftQtrColCnt, secondMtxTopRightQtrMtxData, secondMtxTopRightQtrRowCnt, secondMtxTopRightQtrColCnt);

		int[][] secondMtxBottomLeftQtrMtxData = new int[secondMtxBottomLeftQtrRowCnt][secondMtxBottomLeftQtrColCnt];
		fill(secondMatrixData, secondMtxTopLeftQtrRowCnt, 0, secondMtxBottomLeftQtrMtxData, secondMtxBottomLeftQtrRowCnt, secondMtxBottomLeftQtrColCnt);

		int[][] secondMtxBottomRightQtrMtxData = new int[secondMtxBottomRightQtrRowCnt][secondMtxBottomRightQtrColCnt];
		fill(secondMatrixData, secondMtxTopLeftQtrRowCnt, secondMtxTopLeftQtrColCnt, secondMtxBottomRightQtrMtxData, secondMtxBottomRightQtrRowCnt,
				secondMtxBottomRightQtrColCnt);

		Matrix2 secondMtxTopLeftQtrMtx = new Matrix2(secondMtxTopLeftQtrRowCnt, secondMtxTopLeftQtrColCnt, secondMtxTopLeftQtrMtxData);
		Matrix2 secondMtxTopRightQtrMtx = new Matrix2(secondMtxTopRightQtrRowCnt, secondMtxTopRightQtrColCnt, secondMtxTopRightQtrMtxData);
		Matrix2 secondMtxBottomLeftQtrMtx = new Matrix2(secondMtxBottomLeftQtrRowCnt, secondMtxBottomLeftQtrColCnt, secondMtxBottomLeftQtrMtxData);
		Matrix2 secondMtxBottomRightQtrMtx = new Matrix2(secondMtxBottomRightQtrRowCnt, secondMtxBottomRightQtrColCnt, secondMtxBottomRightQtrMtxData);

//		System.out.println("Printing quarter matrices of second matrix.");
//		print(secondMtxTopLeftQtrMtxData);
//		System.out.println();
//		print(secondMtxTopRightQtrMtxData);
//		System.out.println();
//		print(secondMtxBottomLeftQtrMtxData);
//		System.out.println();
//		print(secondMtxBottomRightQtrMtxData);

		int[][] partialProdMtxTopLeftData = new int[firstMtxTopLeftQtrRowCnt][secondMtxTopLeftQtrColCnt];

		if (firstMtxTopLeftQtrRowCnt * firstMtxTopLeftQtrColCnt != 0 && secondMtxTopLeftQtrRowCnt * secondMtxTopLeftQtrColCnt != 0) {
			// A(11) * B(11)
			int[][] partialProdMtxTopLeft1Data = computeProductMatrix(firstMtxTopLeftQtrMtx, secondMtxTopLeftQtrMtx).getData();
			// C(11) += A(11) * B(11)
			partialProdMtxTopLeftData = addMatricesData(firstMtxTopLeftQtrRowCnt, secondMtxTopLeftQtrColCnt, partialProdMtxTopLeftData, partialProdMtxTopLeft1Data);
		}

		if (firstMtxTopRightQtrRowCnt * firstMtxTopRightQtrColCnt != 0 && secondMtxBottomLeftQtrRowCnt * secondMtxBottomLeftQtrColCnt != 0) {
			// A(12) * B(21)
			int[][] partialProdMtxTopLeft2Data = computeProductMatrix(firstMtxTopRightQtrMtx, secondMtxBottomLeftQtrMtx).getData();
			// C(11) += A(12) * B(21)
			partialProdMtxTopLeftData = addMatricesData(firstMtxTopRightQtrRowCnt, secondMtxBottomLeftQtrColCnt, partialProdMtxTopLeftData, partialProdMtxTopLeft2Data);
		}

		int[][] partialProdMtxTopRightData = new int[firstMtxTopLeftQtrRowCnt][secondMtxTopRightQtrColCnt];

		if (firstMtxTopLeftQtrRowCnt * firstMtxTopLeftQtrColCnt != 0 && secondMtxTopRightQtrRowCnt * secondMtxTopRightQtrColCnt != 0) {
			// A(11) * B(12)
			int[][] partialProdMtxTopRight1Data = computeProductMatrix(firstMtxTopLeftQtrMtx, secondMtxTopRightQtrMtx).getData();
			// C(12) += A(11) * B(12)
			partialProdMtxTopRightData = addMatricesData(firstMtxTopLeftQtrRowCnt, secondMtxTopRightQtrColCnt, partialProdMtxTopRightData, partialProdMtxTopRight1Data);
		}

		if (firstMtxTopRightQtrRowCnt * firstMtxTopRightQtrColCnt != 0 && secondMtxBottomRightQtrRowCnt * secondMtxBottomRightQtrColCnt != 0) {
			// A(12) * B(22)
			int[][] partialProdMtxTopRight2Data = computeProductMatrix(firstMtxTopRightQtrMtx, secondMtxBottomRightQtrMtx).getData();
			// C(12) += A(12) * B(22)
			partialProdMtxTopRightData = addMatricesData(firstMtxTopRightQtrRowCnt, secondMtxBottomRightQtrColCnt, partialProdMtxTopRightData, partialProdMtxTopRight2Data);
		}

		int[][] partialProdMtxBottomLeftData = new int[firstMtxBottomLeftQtrRowCnt][secondMtxTopLeftQtrColCnt];

		if (firstMtxBottomLeftQtrRowCnt * firstMtxBottomLeftQtrColCnt != 0 && secondMtxTopLeftQtrRowCnt * secondMtxTopLeftQtrColCnt != 0) {
			// A(21) * B(11)
			int[][] partialProdMtxBottomLeft1Data = computeProductMatrix(firstMtxBottomLeftQtrMtx, secondMtxTopLeftQtrMtx).getData();
			// C(21) += A(21) * B(11)
			partialProdMtxBottomLeftData = addMatricesData(firstMtxBottomLeftQtrRowCnt, secondMtxTopLeftQtrColCnt, partialProdMtxBottomLeftData, partialProdMtxBottomLeft1Data);
		}

		if (firstMtxBottomRightQtrRowCnt * firstMtxBottomRightQtrColCnt != 0 && secondMtxBottomLeftQtrRowCnt * secondMtxBottomLeftQtrColCnt != 0) {
			// A(22) * B(21)
			int[][] partialProdMtxBottomLeft2Data = computeProductMatrix(firstMtxBottomRightQtrMtx, secondMtxBottomLeftQtrMtx).getData();
			// C(21) += A(22) * B(21)
			partialProdMtxBottomLeftData = addMatricesData(firstMtxBottomRightQtrRowCnt, secondMtxBottomLeftQtrColCnt, partialProdMtxBottomLeftData, partialProdMtxBottomLeft2Data);
		}

		int[][] partialProdMtxBottomRightData = new int[firstMtxBottomLeftQtrRowCnt][secondMtxTopRightQtrColCnt];

		if (firstMtxBottomLeftQtrRowCnt * firstMtxBottomLeftQtrColCnt != 0 && secondMtxTopRightQtrRowCnt * secondMtxTopRightQtrColCnt != 0) {
			// A(21) * B(12)
			int[][] partialProdMtxBottomRigh1Data = computeProductMatrix(firstMtxBottomLeftQtrMtx, secondMtxTopRightQtrMtx).getData();
			// C(22) += A(21) * B(12)
			partialProdMtxBottomRightData = addMatricesData(firstMtxBottomLeftQtrRowCnt, secondMtxTopRightQtrColCnt, partialProdMtxBottomRightData, partialProdMtxBottomRigh1Data);
		}		

		if (firstMtxBottomRightQtrRowCnt * firstMtxBottomRightQtrColCnt != 0 && secondMtxBottomRightQtrRowCnt * secondMtxBottomRightQtrColCnt != 0) {
			// A(22) * B(22)
			int[][] partialProdMtxBottomRigh2Data = computeProductMatrix(firstMtxBottomRightQtrMtx, secondMtxBottomRightQtrMtx).getData();
			// C(22) += A(22) * B(22)
			partialProdMtxBottomRightData = addMatricesData(firstMtxBottomRightQtrRowCnt, secondMtxBottomRightQtrColCnt, partialProdMtxBottomRightData, partialProdMtxBottomRigh2Data);
		}

//		LOGGER.info(
//				String.format("Dimensions for partialProdMtxTopLeft1: %d x %d", partialProdMtxTopLeft1.getRowCount(), partialProdMtxTopLeft1.getColumnCount()));
//		LOGGER.info(
//				String.format("Dimensions for partialProdMtxTopLeft2: %d x %d", partialProdMtxTopLeft2.getRowCount(), partialProdMtxTopLeft2.getColumnCount()));
//		LOGGER.info(String.format("Dimensions for partialProdMtxTopRight1: %d x %d", partialProdMtxTopRight1.getRowCount(),
//				partialProdMtxTopRight1.getColumnCount()));
//		LOGGER.info(String.format("Dimensions for partialProdMtxTopRight2: %d x %d", partialProdMtxTopRight2.getRowCount(),
//				partialProdMtxTopRight2.getColumnCount()));
//		LOGGER.info(String.format("Dimensions for partialProdMtxBottomLeft1: %d x %d", partialProdMtxBottomLeft1.getRowCount(),
//				partialProdMtxBottomLeft1.getColumnCount()));
//		LOGGER.info(String.format("Dimensions for partialProdMtxBottomLeft2: %d x %d", partialProdMtxBottomLeft2.getRowCount(),
//				partialProdMtxBottomLeft2.getColumnCount()));
//		LOGGER.info(String.format("Dimensions for partialProdMtxBottomRight1: %d x %d", partialProdMtxBottomRight1.getRowCount(),
//				partialProdMtxBottomRight1.getColumnCount()));
//		LOGGER.info(String.format("Dimensions for partialProdMtxBottomRight2: %d x %d", partialProdMtxBottomRight2.getRowCount(),
//				partialProdMtxBottomRight2.getColumnCount()));

		int[][] productMatrixData = new int[firstMtxRowCnt][secondMtxColCnt];

		for (int i = 0; i < firstMtxTopLeftQtrRowCnt; i++) {
			for (int j = 0; j < secondMtxTopLeftQtrColCnt; j++) {
				productMatrixData[i][j] = partialProdMtxTopLeftData[i][j];
			}
		}
		for (int i = 0; i < firstMtxTopLeftQtrRowCnt; i++) {
			for (int j = secondMtxTopLeftQtrColCnt; j < secondMtxColCnt; j++) {
				productMatrixData[i][j] = partialProdMtxTopRightData[i][j - secondMtxTopLeftQtrColCnt];
			}
		}
		for (int i = firstMtxTopLeftQtrRowCnt; i < firstMtxRowCnt; i++) {
			for (int j = 0; j < secondMtxTopLeftQtrColCnt; j++) {
				productMatrixData[i][j] = partialProdMtxBottomLeftData[i - firstMtxTopLeftQtrRowCnt][j];
			}
		}
		for (int i = firstMtxTopLeftQtrRowCnt; i < firstMtxRowCnt; i++) {
			for (int j = secondMtxTopLeftQtrColCnt; j < secondMtxColCnt; j++) {
				productMatrixData[i][j] = partialProdMtxBottomRightData[i - firstMtxTopLeftQtrRowCnt][j - secondMtxTopLeftQtrColCnt];
			}
		}

		return new Matrix2(firstMtxRowCnt, secondMtxColCnt, productMatrixData);
	}

	private void fill(int[][] sourceData, int sourceRowOffset, int sourceColOffset, int[][] destData, int destRowCnt, int destColCnt) {
		for (int i = 0; i < destRowCnt; i++) {
			for (int j = 0; j < destColCnt; j++) {
				destData[i][j] = sourceData[i + sourceRowOffset][j + sourceColOffset];
			}
		}
	}

	private int[][] addMatricesData(int rowCount, int columnCount, int[][] matrix1, int[][] matrix2) {
		LOGGER.info(String.format("rowCount: %d, columnCount: %d", rowCount, columnCount));
		
		int[][] resultMatrix = new int[rowCount][columnCount];

		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				resultMatrix[i][j] = matrix1[i][j] + matrix2[i][j];
			}
		}
		
		return resultMatrix;
	}

	private void print(int[][] data) {
		int rowCnt = data.length;

		for (int i = 0; i < rowCnt; i++) {
			int[] row = data[i];
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

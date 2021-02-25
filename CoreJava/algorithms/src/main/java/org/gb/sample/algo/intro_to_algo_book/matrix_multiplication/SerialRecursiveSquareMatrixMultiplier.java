package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

class SerialRecursiveSquareMatrixMultiplier extends AbstractSquareMatrixMultiplier {

	private static final Logger LOGGER = Logger.getLogger(SerialRecursiveSquareMatrixMultiplier.class);

	@Override
	int[][] multiplySquareMatrices(final int dimension, final int[][] firstMatrixData, final int[][] secondMatrixData) {
		Matrix2 firstMatrix = new Matrix2(dimension, dimension, firstMatrixData);
		Matrix2 secondMatrix = new Matrix2(dimension, dimension, secondMatrixData);
		Matrix2 productMatrix = computeProductMatrix(firstMatrix, secondMatrix);
		return productMatrix.getData();
	}

	private int[][] slice(final int[][] sourceData, final int sourceRowOffset, final int sourceColOffset, final int destRowCnt, final int destColCnt) {
		Stream<int[]> slicedRows = IntStream.range(sourceRowOffset, sourceRowOffset + destRowCnt).mapToObj(i -> sourceData[i]);
		Stream<int[]> slicedCells = slicedRows.map(row -> Arrays.copyOfRange(row, sourceColOffset, sourceColOffset + destColCnt));
		return slicedCells.toArray(int[][]::new);
	}

	private int[][] prepareProductMatrix(final int firstMtxRowCnt, final int secondMtxColCnt, final int topPortionRowCnt, final Matrix2 partialProdMtxTopLeft,
			final Matrix2 partialProdMtxTopRight, final Matrix2 partialProdMtxBottomLeft, final Matrix2 partialProdMtxBottomRight) {
		final int[][] productMatrixData = new int[firstMtxRowCnt][secondMtxColCnt];

		for (int destRowIdx = 0; destRowIdx < topPortionRowCnt; destRowIdx++) {
			int[] partialProdMtxTopLeftRow = partialProdMtxTopLeft.getData()[destRowIdx];
			int[] partialProdMtxTopRightRow = partialProdMtxTopRight.getData()[destRowIdx];
			System.arraycopy(partialProdMtxTopLeftRow, 0, productMatrixData[destRowIdx], 0, partialProdMtxTopLeftRow.length);
			System.arraycopy(partialProdMtxTopRightRow, 0, productMatrixData[destRowIdx], partialProdMtxTopLeftRow.length, partialProdMtxTopRightRow.length);
		}

		for (int destRowIdx = topPortionRowCnt; destRowIdx < firstMtxRowCnt; destRowIdx++) {
			int[] partialProdMtxBottomLeftRow = partialProdMtxBottomLeft.getData()[destRowIdx - topPortionRowCnt];
			int[] partialProdMtxBottomRightRow = partialProdMtxBottomRight.getData()[destRowIdx - topPortionRowCnt];
			System.arraycopy(partialProdMtxBottomLeftRow, 0, productMatrixData[destRowIdx], 0, partialProdMtxBottomLeftRow.length);
			System.arraycopy(partialProdMtxBottomRightRow, 0, productMatrixData[destRowIdx], partialProdMtxBottomLeftRow.length,
					partialProdMtxBottomRightRow.length);
		}
		return productMatrixData;
	}

	private Matrix2 computeProductMatrix(Matrix2 firstMatrix, Matrix2 secondMatrix) {
		final int firstMtxRowCnt = firstMatrix.getRowCount();
		final int firstMtxColCnt = firstMatrix.getColumnCount();
		final int secondMtxRowCnt = secondMatrix.getRowCount();
		final int secondMtxColCnt = secondMatrix.getColumnCount();
		final int[][] firstMatrixData = firstMatrix.getData();
		final int[][] secondMatrixData = secondMatrix.getData();

		if (firstMatrix.isZeroDimensional() || secondMatrix.isZeroDimensional()) {
			return new Matrix2(0, 0);
		} else if (firstMtxRowCnt == 1 && firstMtxColCnt == 1 && secondMtxRowCnt == 1 && secondMtxColCnt == 1) {
			int firstMtxVal = firstMatrixData[0][0];
			int secondMtxVal = secondMatrixData[0][0];
			int[][] prodMtxData = { { firstMtxVal * secondMtxVal } };
			return new Matrix2(1, 1, prodMtxData);
		}

		final int firstMtxHalvedRowCnt = firstMtxRowCnt / 2;
		final int firstMtxRemainderRowCnt = firstMtxRowCnt - firstMtxHalvedRowCnt;
		final int firstMtxHalvedColCnt = firstMtxColCnt / 2;
		final int firstMtxRemainderColCnt = firstMtxColCnt - firstMtxHalvedColCnt;

		final int secondMtxHalvedRowCnt = secondMtxRowCnt / 2;
		final int secondMtxRemainderRowCnt = secondMtxRowCnt - secondMtxHalvedRowCnt;
		final int secondMtxHalvedColCnt = secondMtxColCnt / 2;
		final int secondMtxRemainderColCnt = secondMtxColCnt - secondMtxHalvedColCnt;

		final int firstMtxTopLeftQtrRowCnt = firstMtxRemainderRowCnt;
		final int firstMtxTopLeftQtrColCnt = firstMtxRemainderColCnt;
		final int firstMtxTopRightQtrRowCnt = firstMtxTopLeftQtrRowCnt;
		final int firstMtxTopRightQtrColCnt = firstMtxColCnt - firstMtxTopLeftQtrColCnt;
		final int firstMtxBottomLeftQtrRowCnt = firstMtxRowCnt - firstMtxTopLeftQtrRowCnt;
		final int firstMtxBottomLeftQtrColCnt = firstMtxTopLeftQtrColCnt;
		final int firstMtxBottomRightQtrRowCnt = firstMtxBottomLeftQtrRowCnt;
		final int firstMtxBottomRightQtrColCnt = firstMtxTopRightQtrColCnt;

		final int secondMtxTopLeftQtrRowCnt = secondMtxRemainderRowCnt;
		final int secondMtxTopLeftQtrColCnt = secondMtxRemainderColCnt;
		final int secondMtxTopRightQtrRowCnt = secondMtxTopLeftQtrRowCnt;
		final int secondMtxTopRightQtrColCnt = secondMtxColCnt - secondMtxTopLeftQtrColCnt;
		final int secondMtxBottomLeftQtrRowCnt = secondMtxRowCnt - secondMtxTopLeftQtrRowCnt;
		final int secondMtxBottomLeftQtrColCnt = secondMtxTopLeftQtrColCnt;
		final int secondMtxBottomRightQtrRowCnt = secondMtxBottomLeftQtrRowCnt;
		final int secondMtxBottomRightQtrColCnt = secondMtxTopRightQtrColCnt;

		final int[][] firstMtxTopLeftQtrMtxData = slice(firstMatrixData, 0, 0, firstMtxTopLeftQtrRowCnt, firstMtxTopLeftQtrColCnt);
		final int[][] firstMtxTopRightQtrMtxData = slice(firstMatrixData, 0, firstMtxTopLeftQtrColCnt, firstMtxTopRightQtrRowCnt, firstMtxTopRightQtrColCnt);
		final int[][] firstMtxBottomLeftQtrMtxData = slice(firstMatrixData, firstMtxTopLeftQtrRowCnt, 0, firstMtxBottomLeftQtrRowCnt,
				firstMtxBottomLeftQtrColCnt);
		final int[][] firstMtxBottomRightQtrMtxData = slice(firstMatrixData, firstMtxTopLeftQtrRowCnt, firstMtxTopLeftQtrColCnt, firstMtxBottomRightQtrRowCnt,
				firstMtxBottomRightQtrColCnt);

		final int[][] secondMtxTopLeftQtrMtxData = slice(secondMatrixData, 0, 0, secondMtxTopLeftQtrRowCnt, secondMtxTopLeftQtrColCnt);
		final int[][] secondMtxTopRightQtrMtxData = slice(secondMatrixData, 0, secondMtxTopLeftQtrColCnt, secondMtxTopRightQtrRowCnt,
				secondMtxTopRightQtrColCnt);
		final int[][] secondMtxBottomLeftQtrMtxData = slice(secondMatrixData, secondMtxTopLeftQtrRowCnt, 0, secondMtxBottomLeftQtrRowCnt,
				secondMtxBottomLeftQtrColCnt);
		final int[][] secondMtxBottomRightQtrMtxData = slice(secondMatrixData, secondMtxTopLeftQtrRowCnt, secondMtxTopLeftQtrColCnt,
				secondMtxBottomRightQtrRowCnt, secondMtxBottomRightQtrColCnt);

		final Matrix2 firstMtxTopLeftQtrMtx = new Matrix2(firstMtxTopLeftQtrRowCnt, firstMtxTopLeftQtrColCnt, firstMtxTopLeftQtrMtxData);
		final Matrix2 firstMtxTopRightQtrMtx = new Matrix2(firstMtxTopRightQtrRowCnt, firstMtxTopRightQtrColCnt, firstMtxTopRightQtrMtxData);
		final Matrix2 firstMtxBottomLeftQtrMtx = new Matrix2(firstMtxBottomLeftQtrRowCnt, firstMtxBottomLeftQtrColCnt, firstMtxBottomLeftQtrMtxData);
		final Matrix2 firstMtxBottomRightQtrMtx = new Matrix2(firstMtxBottomRightQtrRowCnt, firstMtxBottomRightQtrColCnt, firstMtxBottomRightQtrMtxData);

		final Matrix2 secondMtxTopLeftQtrMtx = new Matrix2(secondMtxTopLeftQtrRowCnt, secondMtxTopLeftQtrColCnt, secondMtxTopLeftQtrMtxData);
		final Matrix2 secondMtxTopRightQtrMtx = new Matrix2(secondMtxTopRightQtrRowCnt, secondMtxTopRightQtrColCnt, secondMtxTopRightQtrMtxData);
		final Matrix2 secondMtxBottomLeftQtrMtx = new Matrix2(secondMtxBottomLeftQtrRowCnt, secondMtxBottomLeftQtrColCnt, secondMtxBottomLeftQtrMtxData);
		final Matrix2 secondMtxBottomRightQtrMtx = new Matrix2(secondMtxBottomRightQtrRowCnt, secondMtxBottomRightQtrColCnt, secondMtxBottomRightQtrMtxData);

		final Matrix2 partialProdMtxTopLeft = new Matrix2(firstMtxTopLeftQtrRowCnt, secondMtxTopLeftQtrColCnt);
		final Matrix2 partialProdMtxTopLeft1 = computeProductMatrix(firstMtxTopLeftQtrMtx, secondMtxTopLeftQtrMtx);
		final Matrix2 partialProdMtxTopLeft2 = computeProductMatrix(firstMtxTopRightQtrMtx, secondMtxBottomLeftQtrMtx);

		if (!partialProdMtxTopLeft1.isZeroDimensional()) {
			partialProdMtxTopLeft.addData(firstMtxTopRightQtrRowCnt, secondMtxBottomLeftQtrColCnt, partialProdMtxTopLeft1.getData());
		}
		if (!partialProdMtxTopLeft2.isZeroDimensional()) {
			partialProdMtxTopLeft.addData(firstMtxTopRightQtrRowCnt, secondMtxBottomLeftQtrColCnt, partialProdMtxTopLeft2.getData());
		}

		final Matrix2 partialProdMtxTopRight = new Matrix2(firstMtxTopLeftQtrRowCnt, secondMtxTopRightQtrColCnt);
		final Matrix2 partialProdMtxTopRight1 = computeProductMatrix(firstMtxTopLeftQtrMtx, secondMtxTopRightQtrMtx);
		final Matrix2 partialProdMtxTopRight2 = computeProductMatrix(firstMtxTopRightQtrMtx, secondMtxBottomRightQtrMtx);

		if (!partialProdMtxTopRight1.isZeroDimensional()) {
			partialProdMtxTopRight.addData(firstMtxTopRightQtrRowCnt, secondMtxBottomRightQtrColCnt, partialProdMtxTopRight1.getData());
		}
		if (!partialProdMtxTopRight2.isZeroDimensional()) {
			partialProdMtxTopRight.addData(firstMtxTopRightQtrRowCnt, secondMtxBottomRightQtrColCnt, partialProdMtxTopRight2.getData());
		}

		final Matrix2 partialProdMtxBottomLeft = new Matrix2(firstMtxBottomLeftQtrRowCnt, secondMtxTopLeftQtrColCnt);
		final Matrix2 partialProdMtxBottomLeft1 = computeProductMatrix(firstMtxBottomLeftQtrMtx, secondMtxTopLeftQtrMtx);
		final Matrix2 partialProdMtxBottomLeft2 = computeProductMatrix(firstMtxBottomRightQtrMtx, secondMtxBottomLeftQtrMtx);

		if (!partialProdMtxBottomLeft1.isZeroDimensional()) {
			partialProdMtxBottomLeft.addData(firstMtxBottomRightQtrRowCnt, secondMtxBottomLeftQtrColCnt, partialProdMtxBottomLeft1.getData());
		}
		if (!partialProdMtxBottomLeft2.isZeroDimensional()) {
			partialProdMtxBottomLeft.addData(firstMtxBottomRightQtrRowCnt, secondMtxBottomLeftQtrColCnt, partialProdMtxBottomLeft2.getData());
		}

		final Matrix2 partialProdMtxBottomRight = new Matrix2(firstMtxBottomLeftQtrRowCnt, secondMtxTopRightQtrColCnt);
		final Matrix2 partialProdMtxBottomRight1 = computeProductMatrix(firstMtxBottomLeftQtrMtx, secondMtxTopRightQtrMtx);
		final Matrix2 partialProdMtxBottomRight2 = computeProductMatrix(firstMtxBottomRightQtrMtx, secondMtxBottomRightQtrMtx);

		if (!partialProdMtxBottomRight1.isZeroDimensional()) {
			partialProdMtxBottomRight.addData(firstMtxBottomRightQtrRowCnt, secondMtxBottomRightQtrColCnt, partialProdMtxBottomRight1.getData());
		}
		if (!partialProdMtxBottomRight2.isZeroDimensional()) {
			partialProdMtxBottomRight.addData(firstMtxBottomRightQtrRowCnt, secondMtxBottomRightQtrColCnt, partialProdMtxBottomRight2.getData());
		}

		final int[][] productMatrixData = prepareProductMatrix(firstMtxRowCnt, secondMtxColCnt, firstMtxTopLeftQtrRowCnt, partialProdMtxTopLeft,
				partialProdMtxTopRight, partialProdMtxBottomLeft, partialProdMtxBottomRight);

		return new Matrix2(firstMtxRowCnt, secondMtxColCnt, productMatrixData);
	}
}

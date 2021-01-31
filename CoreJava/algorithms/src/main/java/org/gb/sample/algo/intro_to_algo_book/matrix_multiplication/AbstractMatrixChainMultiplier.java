package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.List;

abstract class AbstractMatrixChainMultiplier implements MatrixChainMultiplier {

	private int verifyCompatibility(List<Matrix> matrixChain) {
		int matrixCount = matrixChain.size();
		int incompatibleIdx = -1;

		for (int idx = 0; idx < (matrixCount - 1); idx++) {
			Matrix matrix = matrixChain.get(idx);
			Matrix nextMatrix = matrixChain.get(idx + 1);

			if (matrix.getColumnCount() != nextMatrix.getRowCount()) {
				incompatibleIdx = idx;
				break;
			}
		}

		return incompatibleIdx;
	}

	@Override
	public Matrix computeOptimalOrder(List<Matrix> matrixChain) {
		if (matrixChain.size() == 1) {
			Matrix matrix = matrixChain.get(0);
			return new Matrix(matrix.getSimpleProductName(), matrix.getRowCount(), matrix.getColumnCount());
		}

		int incompatibleIdx = verifyCompatibility(matrixChain);

		if (incompatibleIdx != -1) {
			Matrix incompatibleMatrix1 = matrixChain.get(incompatibleIdx);
			Matrix incompatibleMatrix2 = matrixChain.get(incompatibleIdx + 1);
			String errorMsg = String.format("Matrices at index %d: %s and index: %d: %s are incompatible. Chain multiplication aborted.", incompatibleIdx,
					incompatibleMatrix1.getSimpleProductName(), incompatibleIdx + 1, incompatibleMatrix2.getSimpleProductName());
			throw new IllegalArgumentException(errorMsg);
		}

		return computeOptimalOrderInternal(matrixChain);
	}

	abstract Matrix computeOptimalOrderInternal(List<Matrix> matrixChain);
}

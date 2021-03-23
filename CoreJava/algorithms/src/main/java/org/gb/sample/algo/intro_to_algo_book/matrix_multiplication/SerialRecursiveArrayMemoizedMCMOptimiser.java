package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.List;
import org.javatuples.Quartet;

class SerialRecursiveArrayMemoizedMCMOptimiser extends AbstractMatrixChainMultiplicationOptimiser {

	private Quartet<Integer, Integer, Matrix, Matrix> computeOptimalSplit(final List<Matrix> matrixChain, final int sgmtBeginIdx, final int sgmtEndIdx,
			final Matrix[][] cachedOptimalProducts, int minMultiplyCount) {
		int optimalSplitIdx = 0;
		Matrix leftSubchainOptimalProduct = null;
		Matrix rightSubchainOptimalProduct = null;
		for (int splitIdx = sgmtBeginIdx; splitIdx < sgmtEndIdx; splitIdx++) {
			Matrix leftSubchainHead = matrixChain.get(sgmtBeginIdx);
			Matrix leftSubchainTail = matrixChain.get(splitIdx);
			Matrix rightSubchainTail = matrixChain.get(sgmtEndIdx);

			Matrix leftSubchainProduct = computeOptimalOrderOrGetFromCache(matrixChain, sgmtBeginIdx, splitIdx, cachedOptimalProducts);
			Matrix rightSubchainProduct = computeOptimalOrderOrGetFromCache(matrixChain, splitIdx + 1, sgmtEndIdx, cachedOptimalProducts);
			int subchainsMultiplyCount = leftSubchainHead.getRowCount() * leftSubchainTail.getColumnCount() * rightSubchainTail.getColumnCount();
			int cumltvMultiplyCount = leftSubchainProduct.getCumulativeMultiplyCount() + rightSubchainProduct.getCumulativeMultiplyCount()
					+ subchainsMultiplyCount;

			if (cumltvMultiplyCount < minMultiplyCount) {
				optimalSplitIdx = splitIdx;
				minMultiplyCount = cumltvMultiplyCount;
				leftSubchainOptimalProduct = leftSubchainProduct;
				rightSubchainOptimalProduct = rightSubchainProduct;
			}
		}
		return Quartet.with(optimalSplitIdx, minMultiplyCount, leftSubchainOptimalProduct, rightSubchainOptimalProduct);
	}

	private Matrix computeOptimalOrderOrGetFromCache(final List<Matrix> matrixChain, final int sgmtBeginIdx, final int sgmtEndIdx,
			final Matrix[][] cachedOptimalProducts) {
		Matrix optimalProduct = null;
		if (sgmtBeginIdx == sgmtEndIdx) {
			optimalProduct = matrixChain.get(sgmtBeginIdx);
		} else {
			if (cachedOptimalProducts[sgmtBeginIdx][sgmtEndIdx] != null) {
				optimalProduct = cachedOptimalProducts[sgmtBeginIdx][sgmtEndIdx];
			} else if (sgmtEndIdx - sgmtBeginIdx == 1) {
				Matrix leftMatrix = matrixChain.get(sgmtBeginIdx);
				Matrix rightMatrix = matrixChain.get(sgmtEndIdx);
				int multiplyCount = leftMatrix.getRowCount() * leftMatrix.getColumnCount() * rightMatrix.getColumnCount();

				optimalProduct = new Matrix(leftMatrix, rightMatrix, leftMatrix.getRowCount(), rightMatrix.getColumnCount(), multiplyCount);
				cachedOptimalProducts[sgmtBeginIdx][sgmtEndIdx] = optimalProduct;
			} else {
				Quartet<Integer, Integer, Matrix, Matrix> optimalSplitResult = computeOptimalSplit(matrixChain, sgmtBeginIdx, sgmtEndIdx, cachedOptimalProducts,
						Integer.MAX_VALUE);

				int minMultiplyCount = optimalSplitResult.getValue1().intValue();
				Matrix leftSubchainOptimalProduct = optimalSplitResult.getValue2();
				Matrix rightSubchainOptimalProduct = optimalSplitResult.getValue3();
				Matrix leftSubchainHead = matrixChain.get(sgmtBeginIdx);
				Matrix rightSubchainTail = matrixChain.get(sgmtEndIdx);

				optimalProduct = new Matrix(leftSubchainOptimalProduct, rightSubchainOptimalProduct, leftSubchainHead.getRowCount(),
						rightSubchainTail.getColumnCount(), minMultiplyCount);
				cachedOptimalProducts[sgmtBeginIdx][sgmtEndIdx] = optimalProduct;
			}
		}

		return optimalProduct;
	}

	@Override
	Matrix computeOptimalOrderInternal(final List<Matrix> matrixChain) {
		final int matrixCount = matrixChain.size();
		final Matrix[][] cachedOptimalProducts = new Matrix[matrixCount][matrixCount];
		return computeOptimalOrderOrGetFromCache(matrixChain, 0, matrixCount - 1, cachedOptimalProducts);
	}

}

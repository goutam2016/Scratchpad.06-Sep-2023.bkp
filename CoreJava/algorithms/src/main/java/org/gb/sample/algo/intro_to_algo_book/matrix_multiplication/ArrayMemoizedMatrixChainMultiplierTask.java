package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import org.javatuples.Quartet;

class ArrayMemoizedMatrixChainMultiplierTask extends RecursiveTask<Matrix> {

	private static final long serialVersionUID = 6828524138674606385L;

	private final List<Matrix> matrixChain;
	private final int sgmtBeginIdx;
	private final int sgmtEndIdx;
	private final Matrix[][] cachedOptimalProducts;

	ArrayMemoizedMatrixChainMultiplierTask(List<Matrix> matrixChain, int sgmtBeginIdx, int sgmtEndIdx, Matrix[][] cachedOptimalProducts) {
		super();
		this.matrixChain = matrixChain;
		this.sgmtBeginIdx = sgmtBeginIdx;
		this.sgmtEndIdx = sgmtEndIdx;
		this.cachedOptimalProducts = cachedOptimalProducts;
	}

	private Quartet<Integer, Integer, Matrix, Matrix> computeOptimalSplit(int minMultiplyCount) {
		int optimalSplitIdx = 0;
		Matrix leftSubchainOptimalProduct = null;
		Matrix rightSubchainOptimalProduct = null;
		for (int splitIdx = sgmtBeginIdx; splitIdx < sgmtEndIdx; splitIdx++) {
			Matrix leftSubchainHead = matrixChain.get(sgmtBeginIdx);
			Matrix leftSubchainTail = matrixChain.get(splitIdx);
			Matrix rightSubchainTail = matrixChain.get(sgmtEndIdx);

			ForkJoinTask<Matrix> leftSubchainMultiplierTask = new ArrayMemoizedMatrixChainMultiplierTask(matrixChain, sgmtBeginIdx, splitIdx,
					cachedOptimalProducts).fork();
			ForkJoinTask<Matrix> rightSubchainMultiplierTask = new ArrayMemoizedMatrixChainMultiplierTask(matrixChain, splitIdx + 1, sgmtEndIdx,
					cachedOptimalProducts).fork();

			Matrix leftSubchainProduct = leftSubchainMultiplierTask.join();
			Matrix rightSubchainProduct = rightSubchainMultiplierTask.join();
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

	@Override
	protected Matrix compute() {
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
				Quartet<Integer, Integer, Matrix, Matrix> optimalSplitResult = computeOptimalSplit(Integer.MAX_VALUE);

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
}

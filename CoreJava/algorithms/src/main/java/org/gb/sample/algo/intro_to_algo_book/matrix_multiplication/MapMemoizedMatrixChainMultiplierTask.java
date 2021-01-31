package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

import org.javatuples.Quartet;

class MapMemoizedMatrixChainMultiplierTask extends RecursiveTask<Matrix> {

	private static final long serialVersionUID = -1069551865224474387L;

	private final List<Matrix> matrixChain;
	private final Map<String, Matrix> cachedOptimalProducts;

	MapMemoizedMatrixChainMultiplierTask(List<Matrix> matrixChain, Map<String, Matrix> cachedOptimalProducts) {
		super();
		this.matrixChain = matrixChain;
		this.cachedOptimalProducts = cachedOptimalProducts;
	}

	private Quartet<Integer, Integer, Matrix, Matrix> computeOptimalSplit(int minMultiplyCount) {
		final int matrixCount = matrixChain.size();
		int optimalSplitIdx = 0;
		Matrix leftSubchainOptimalProduct = null;
		Matrix rightSubchainOptimalProduct = null;
		for (int splitIdx = 0; splitIdx < (matrixCount - 1); splitIdx++) {
			List<Matrix> leftSubchain = matrixChain.subList(0, splitIdx + 1);
			List<Matrix> rightSubchain = matrixChain.subList(splitIdx + 1, matrixCount);
			Matrix leftSubchainHead = matrixChain.get(0);
			Matrix leftSubchainTail = matrixChain.get(splitIdx);
			Matrix rightSubchainTail = matrixChain.get(matrixCount - 1);

			ForkJoinTask<Matrix> leftSubchainMultiplierTask = new MapMemoizedMatrixChainMultiplierTask(leftSubchain, cachedOptimalProducts).fork();
			ForkJoinTask<Matrix> rightSubchainMultiplierTask = new MapMemoizedMatrixChainMultiplierTask(rightSubchain, cachedOptimalProducts).fork();

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
		final int matrixCount = matrixChain.size();
		Matrix optimalProduct = null;
		if (matrixCount == 1) {
			optimalProduct = matrixChain.get(0);
		} else {
			String chainProductName = matrixChain.stream().map(Matrix::getSimpleProductName).collect(Collectors.joining());

			if (cachedOptimalProducts.containsKey(chainProductName)) {
				optimalProduct = cachedOptimalProducts.get(chainProductName);
			} else if (matrixCount == 2) {
				Matrix leftMatrix = matrixChain.get(0);
				Matrix rightMatrix = matrixChain.get(1);
				int multiplyCount = leftMatrix.getRowCount() * leftMatrix.getColumnCount() * rightMatrix.getColumnCount();

				optimalProduct = new Matrix(leftMatrix, rightMatrix, leftMatrix.getRowCount(), rightMatrix.getColumnCount(), multiplyCount);
				cachedOptimalProducts.put(optimalProduct.getSimpleProductName(), optimalProduct);
			} else {
				Quartet<Integer, Integer, Matrix, Matrix> optimalSplitResult = computeOptimalSplit(Integer.MAX_VALUE);

				int minMultiplyCount = optimalSplitResult.getValue1().intValue();
				Matrix leftSubchainOptimalProduct = optimalSplitResult.getValue2();
				Matrix rightSubchainOptimalProduct = optimalSplitResult.getValue3();
				Matrix leftSubchainHead = matrixChain.get(0);
				Matrix rightSubchainTail = matrixChain.get(matrixCount - 1);

				optimalProduct = new Matrix(leftSubchainOptimalProduct, rightSubchainOptimalProduct, leftSubchainHead.getRowCount(),
						rightSubchainTail.getColumnCount(), minMultiplyCount);
				cachedOptimalProducts.put(optimalProduct.getSimpleProductName(), optimalProduct);
			}
		}
		return optimalProduct;
	}
}

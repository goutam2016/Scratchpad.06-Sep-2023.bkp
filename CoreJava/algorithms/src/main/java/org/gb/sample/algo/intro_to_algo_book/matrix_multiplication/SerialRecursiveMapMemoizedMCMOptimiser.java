package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.javatuples.Quartet;

class SerialRecursiveMapMemoizedMCMOptimiser extends AbstractMatrixChainMultiplicationOptimiser {

	private Quartet<Integer, Integer, Matrix, Matrix> computeOptimalSplit(final List<Matrix> matrixChain, final Map<String, Matrix> cachedOptimalProducts,
			int minMultiplyCount) {
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

			Matrix leftSubchainProduct = computeOptimalOrderOrGetFromCache(leftSubchain, cachedOptimalProducts);
			Matrix rightSubchainProduct = computeOptimalOrderOrGetFromCache(rightSubchain, cachedOptimalProducts);
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

	private Matrix computeOptimalOrderOrGetFromCache(final List<Matrix> matrixChain, final Map<String, Matrix> cachedOptimalProducts) {
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
				Quartet<Integer, Integer, Matrix, Matrix> optimalSplitResult = computeOptimalSplit(matrixChain, cachedOptimalProducts, Integer.MAX_VALUE);

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

	@Override
	Matrix computeOptimalOrderInternal(final List<Matrix> matrixChain) {
		return computeOptimalOrderOrGetFromCache(matrixChain, new HashMap<>());
	}

}

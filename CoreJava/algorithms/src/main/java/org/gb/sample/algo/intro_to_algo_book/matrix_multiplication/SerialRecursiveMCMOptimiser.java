package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.List;

class SerialRecursiveMCMOptimiser extends AbstractMatrixChainMultiplicationOptimiser {

	@Override
	Matrix computeOptimalOrderInternal(List<Matrix> matrixChain) {
		Matrix optimalProduct = null;
		if (matrixChain.size() == 1) {
			optimalProduct = matrixChain.get(0);
		} else if (matrixChain.size() == 2) {
			Matrix leftMatrix = matrixChain.get(0);
			Matrix rightMatrix = matrixChain.get(1);
			int multiplyCount = leftMatrix.getRowCount() * leftMatrix.getColumnCount() * rightMatrix.getColumnCount();
			optimalProduct = new Matrix(leftMatrix, rightMatrix, leftMatrix.getRowCount(), rightMatrix.getColumnCount(),
					multiplyCount);
		} else {
			int matrixCount = matrixChain.size();
			int minCumltvMultiplyCount = Integer.MAX_VALUE;

			for (int splitIdx = 0; splitIdx < (matrixCount - 1); splitIdx++) {
				List<Matrix> leftSubchain = matrixChain.subList(0, splitIdx + 1);
				List<Matrix> rightSubchain = matrixChain.subList(splitIdx + 1, matrixCount);
				Matrix leftSubchainHead = matrixChain.get(0);
				Matrix leftSubchainTail = matrixChain.get(splitIdx);
				Matrix rightSubchainTail = matrixChain.get(matrixCount - 1);
				Matrix leftSubchainOptimalProduct = computeOptimalOrderInternal(leftSubchain);
				Matrix rightSubchainOptimalProduct = computeOptimalOrderInternal(rightSubchain);
				int subchainsMultiplyCount = leftSubchainHead.getRowCount() * leftSubchainTail.getColumnCount()
						* rightSubchainTail.getColumnCount();
				int cumltvMultiplyCount = leftSubchainOptimalProduct.getCumulativeMultiplyCount()
						+ rightSubchainOptimalProduct.getCumulativeMultiplyCount() + subchainsMultiplyCount;

				if (cumltvMultiplyCount < minCumltvMultiplyCount) {
					minCumltvMultiplyCount = cumltvMultiplyCount;
					optimalProduct = new Matrix(leftSubchainOptimalProduct, rightSubchainOptimalProduct,
							leftSubchainHead.getRowCount(), rightSubchainTail.getColumnCount(), minCumltvMultiplyCount);
				}
			}
		}

		return optimalProduct;
	}
}

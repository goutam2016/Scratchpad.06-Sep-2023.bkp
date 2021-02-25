package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

class SerialTailRecursiveMatrixChainMultiplier extends AbstractMatrixChainMultiplier {

	private Matrix computeProduct(Matrix first, Matrix second) {
		int multiplyCount = first.getRowCount() * first.getColumnCount() * second.getColumnCount();
		int cumltvMultiplyCount = first.getCumulativeMultiplyCount() + second.getCumulativeMultiplyCount()
				+ multiplyCount;
		Matrix product = new Matrix(first, second, first.getRowCount(), second.getColumnCount(), cumltvMultiplyCount);
		return product;
	}

	private Matrix computeOptimalProduct(List<Pair<List<Matrix>, List<Matrix>>> subchainPairs) {
		/*for (Pair<List<Matrix>, List<Matrix>> subchainPair : subchainPairs) {
			
		}
		List<Matrix> leftSubchain = subchainPairs.getValue0();
		List<Matrix> rightSubchain = subchainPairs.getValue1();
		Matrix leftSubchainProduct = null, rightSubchainProduct = null;

		if (leftSubchain.size() == 1) {
			leftSubchainProduct = leftSubchain.get(0);
		} else if (leftSubchain.size() == 2) {
			leftSubchainProduct = computeProduct(leftSubchain.get(0), leftSubchain.get(1));
		}

		if (rightSubchain.size() == 1) {
			rightSubchainProduct = rightSubchain.get(0);
		} else if (rightSubchain.size() == 2) {
			rightSubchainProduct = computeProduct(rightSubchain.get(0), rightSubchain.get(1));
		}

		return computeProduct(leftSubchainProduct, rightSubchainProduct);*/
		return null;
	}

	@Override
	Matrix computeOptimalOrderInternal(List<Matrix> matrixChain) {
		int matrixCount = matrixChain.size();
		List<Matrix> leftSubchain = matrixChain.subList(0, 1);
		List<Matrix> rightSubchain = matrixChain.subList(1, matrixCount);
		List<Pair<List<Matrix>, List<Matrix>>> subchainPairs = new ArrayList<>();
		subchainPairs.add(Pair.with(leftSubchain, rightSubchain));
		//return computeOptimalProduct(subchainPairs);
		return leftSubchain.get(0);
	}

}

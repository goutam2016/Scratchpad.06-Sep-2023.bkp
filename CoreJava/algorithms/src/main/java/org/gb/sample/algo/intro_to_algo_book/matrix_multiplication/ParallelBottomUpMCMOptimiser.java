package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.javatuples.Pair;

class ParallelBottomUpMCMOptimiser extends AbstractMatrixChainMultiplicationOptimiser {

	private void initOptimalProductArr(final List<Matrix> matrixChain, final int matrixCount, final Matrix[][] optimalProductArr) {
		for (int chainIdx = 0; chainIdx < matrixCount; chainIdx++) {
			optimalProductArr[chainIdx][chainIdx] = matrixChain.get(chainIdx);
		}
	}

	private Pair<Integer, Integer> computeCumltvMultiplyCount(final List<Matrix> matrixChain, final int sgmtBeginIdx, final int sgmtEndIdx, final int splitIdx,
			final int[][] minMultiplyCountArr) {
		Matrix sgmtHead = matrixChain.get(sgmtBeginIdx);
		Matrix sgmtSplit = matrixChain.get(splitIdx);
		Matrix sgmtTail = matrixChain.get(sgmtEndIdx);

		int subsgmtsMultiplyCount = sgmtHead.getRowCount() * sgmtSplit.getColumnCount() * sgmtTail.getColumnCount();
		int cumltvMultiplyCount = minMultiplyCountArr[sgmtBeginIdx][splitIdx] + minMultiplyCountArr[splitIdx + 1][sgmtEndIdx] + subsgmtsMultiplyCount;

		return Pair.with(splitIdx, cumltvMultiplyCount);
	}

	private Pair<Integer, Integer> computeOptimalSplit(final List<Matrix> matrixChain, final int sgmtBeginIdx, final int sgmtEndIdx,
			final int[][] minMultiplyCountArr) {
		Optional<Pair<Integer, Integer>> optimalSplit = IntStream.range(sgmtBeginIdx, sgmtEndIdx).parallel()
				.mapToObj(splitIdx -> computeCumltvMultiplyCount(matrixChain, sgmtBeginIdx, sgmtEndIdx, splitIdx, minMultiplyCountArr))
				.min(Comparator.comparingInt(Pair::getValue1));

		return optimalSplit.orElse(Pair.with(sgmtBeginIdx, Integer.MAX_VALUE));
	}

	@Override
	Matrix computeOptimalOrderInternal(final List<Matrix> matrixChain) {
		final int matrixCount = matrixChain.size();
		final Matrix[][] optimalProductArr = new Matrix[matrixCount][matrixCount];
		initOptimalProductArr(matrixChain, matrixCount, optimalProductArr);

		final int[][] minMultiplyCountArr = new int[matrixCount][matrixCount];
		for (int segmentLength = 2; segmentLength <= matrixCount; segmentLength++) {
			for (int sgmtBeginIdx = 0; sgmtBeginIdx <= (matrixCount - segmentLength); sgmtBeginIdx++) {
				int sgmtEndIdx = sgmtBeginIdx + segmentLength - 1;
				Pair<Integer, Integer> optimalSplitResult = computeOptimalSplit(matrixChain, sgmtBeginIdx, sgmtEndIdx, minMultiplyCountArr);

				int optimalSplitIdx = optimalSplitResult.getValue0().intValue();
				int minMultiplyCount = optimalSplitResult.getValue1().intValue();
				Matrix sgmtHead = matrixChain.get(sgmtBeginIdx);
				Matrix sgmtTail = matrixChain.get(sgmtEndIdx);
				Matrix leftSubsgmtOptimalProduct = optimalProductArr[sgmtBeginIdx][optimalSplitIdx];
				Matrix rightSubsgmtOptimalProduct = optimalProductArr[optimalSplitIdx + 1][sgmtEndIdx];

				minMultiplyCountArr[sgmtBeginIdx][sgmtEndIdx] = minMultiplyCount;
				Matrix sgmtOptimalProduct = new Matrix(leftSubsgmtOptimalProduct, rightSubsgmtOptimalProduct, sgmtHead.getRowCount(), sgmtTail.getColumnCount(),
						minMultiplyCount);
				optimalProductArr[sgmtBeginIdx][sgmtEndIdx] = sgmtOptimalProduct;
			}
		}

		return optimalProductArr[0][matrixCount - 1];
	}

}

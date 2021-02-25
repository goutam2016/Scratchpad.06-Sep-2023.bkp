package org.gb.sample.scala.intro_to_algo_book.matrix_multiplication

class SerialRecursiveArrayMemoizedMatrixChainMultiplier extends AbstractMatrixChainMultiplier {

    private def computeOptimalSplit(matrixChain: Seq[Matrix], sgmtBeginIdx: Int, sgmtEndIdx: Int, cachedOptimalProducts: Array[Array[Matrix]])
    : (Int, Int, Matrix, Matrix) = {
        var optimalSplitIdx = 0;
        var minMultiplyCount = Int.MaxValue
        var leftSubchainOptimalProduct: Matrix = null;
        var rightSubchainOptimalProduct: Matrix = null;
        for (splitIdx <- sgmtBeginIdx until (sgmtEndIdx)) {
            val leftSubchainHead = matrixChain(sgmtBeginIdx);
            val leftSubchainTail = matrixChain(splitIdx);
            val rightSubchainTail = matrixChain(sgmtEndIdx);

            val leftSubchainProduct = computeOptimalOrderOrGetFromCache(matrixChain, sgmtBeginIdx, splitIdx, cachedOptimalProducts);
            val rightSubchainProduct = computeOptimalOrderOrGetFromCache(matrixChain, splitIdx + 1, sgmtEndIdx, cachedOptimalProducts);
            val subchainsMultiplyCount = leftSubchainHead.rowCount * leftSubchainTail.columnCount * rightSubchainTail.columnCount;
            val cumltvMultiplyCount = leftSubchainProduct.cumulativeMultiplyCount + rightSubchainProduct.cumulativeMultiplyCount + subchainsMultiplyCount;

            if(cumltvMultiplyCount < minMultiplyCount) {
                optimalSplitIdx = splitIdx;
                minMultiplyCount = cumltvMultiplyCount;
                leftSubchainOptimalProduct = leftSubchainProduct;
                rightSubchainOptimalProduct = rightSubchainProduct;
            }
        }
        return (optimalSplitIdx, minMultiplyCount, leftSubchainOptimalProduct, rightSubchainOptimalProduct)
    }

    private def computeOptimalOrderOrGetFromCache(matrixChain: Seq[Matrix], sgmtBeginIdx: Int, sgmtEndIdx: Int, cachedOptimalProducts: Array[Array[Matrix]]): Matrix = {
        var optimalProduct: Matrix = null
        if(sgmtBeginIdx == sgmtEndIdx) {
            optimalProduct = matrixChain(sgmtBeginIdx)
        }
        else {
            if(cachedOptimalProducts(sgmtBeginIdx)(sgmtEndIdx) != null) {
                optimalProduct = cachedOptimalProducts(sgmtBeginIdx)(sgmtEndIdx)
            }
            else {
                if(sgmtEndIdx - sgmtBeginIdx == 1) {
                    val leftMatrix: Matrix = matrixChain(sgmtBeginIdx)
                    val rightMatrix: Matrix = matrixChain(sgmtEndIdx)
                    val multiplyCount: Int = leftMatrix.rowCount * leftMatrix.columnCount * rightMatrix.columnCount
                    optimalProduct = Matrix(leftMatrix, rightMatrix, leftMatrix.rowCount, rightMatrix.columnCount, multiplyCount)
                    cachedOptimalProducts(sgmtBeginIdx)(sgmtEndIdx) = optimalProduct
                }
                else {
                    val (_, minMultiplyCount, leftSubchainOptimalProduct, rightSubchainOptimalProduct) = computeOptimalSplit(matrixChain, sgmtBeginIdx, sgmtEndIdx, cachedOptimalProducts)
                    val leftSubchainHead: Matrix = matrixChain(sgmtBeginIdx)
                    val rightSubchainTail: Matrix = matrixChain(sgmtEndIdx)
                    optimalProduct = Matrix(leftSubchainOptimalProduct, rightSubchainOptimalProduct, leftSubchainHead.rowCount, rightSubchainTail
                      .columnCount, minMultiplyCount)
                    cachedOptimalProducts(sgmtBeginIdx)(sgmtEndIdx) = optimalProduct
                }
            }
        }

        return optimalProduct
    }

    override def computeOptimalOrderInternal(matrixChain: Seq[Matrix]): Matrix = {
        val matrixCount = matrixChain.size
        val cachedOptimalProducts = Array.ofDim[Matrix](matrixCount, matrixCount)
        return computeOptimalOrderOrGetFromCache(matrixChain, 0, matrixCount - 1, cachedOptimalProducts)
    }
}

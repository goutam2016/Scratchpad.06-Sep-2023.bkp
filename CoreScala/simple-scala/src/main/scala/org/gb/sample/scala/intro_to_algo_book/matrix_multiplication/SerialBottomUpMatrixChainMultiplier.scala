package org.gb.sample.scala.intro_to_algo_book.matrix_multiplication

class SerialBottomUpMatrixChainMultiplier extends AbstractMatrixChainMultiplier {

    private def initOptimalProductArr(matrixChain: Seq[Matrix], matrixCount: Int, optimalProductArr: Array[Array[Matrix]]): Unit = {
        for (chainIdx <- 0 until matrixCount) {
            optimalProductArr(chainIdx)(chainIdx) = matrixChain(chainIdx)
        }
    }

    private def computeOptimalSplit(matrixChain: Seq[Matrix], sgmtBeginIdx: Int, sgmtEndIdx: Int, minMultiplyCountArr: Array[Array[Int]]): (Int, Int) = {
        var optimalSplitIdx = sgmtBeginIdx
        var minMultiplyCount = Int.MaxValue
        for (splitIdx <- sgmtBeginIdx until sgmtEndIdx) {
            val sgmtHead = matrixChain(sgmtBeginIdx)
            val sgmtSplit = matrixChain(splitIdx)
            val sgmtTail = matrixChain(sgmtEndIdx)
            val subsgmtsMultiplyCount = sgmtHead.rowCount * sgmtSplit.columnCount * sgmtTail.columnCount
            val cumltvMultiplyCount = minMultiplyCountArr(sgmtBeginIdx)(splitIdx) + minMultiplyCountArr(splitIdx + 1)(sgmtEndIdx) + subsgmtsMultiplyCount
            if(cumltvMultiplyCount < minMultiplyCount) {
                optimalSplitIdx = splitIdx
                minMultiplyCount = cumltvMultiplyCount
            }
        }
        return (optimalSplitIdx, minMultiplyCount)
    }

    override def computeOptimalOrderInternal(matrixChain: Seq[Matrix]): Matrix = {
        val matrixCount = matrixChain.size
        val optimalProductArr = Array.ofDim[Matrix](matrixCount, matrixCount)
        initOptimalProductArr(matrixChain, matrixCount, optimalProductArr)

        val minMultiplyCountArr = Array.ofDim[Int](matrixCount, matrixCount)
        for (segmentLength <- 2 to (matrixCount)) {
            for (sgmtBeginIdx <- 0 to (matrixCount - segmentLength)) {
                val sgmtEndIdx = sgmtBeginIdx + segmentLength - 1
                val (optimalSplitIdx, minMultiplyCount) = computeOptimalSplit(matrixChain, sgmtBeginIdx, sgmtEndIdx, minMultiplyCountArr)
                val sgmtHead = matrixChain(sgmtBeginIdx)
                val sgmtTail = matrixChain(sgmtEndIdx)
                val leftSubsgmtOptimalProduct = optimalProductArr(sgmtBeginIdx)(optimalSplitIdx)
                val rightSubsgmtOptimalProduct = optimalProductArr(optimalSplitIdx + 1)(sgmtEndIdx)

                minMultiplyCountArr(sgmtBeginIdx)(sgmtEndIdx) = minMultiplyCount
                val sgmtOptimalProduct = Matrix(leftSubsgmtOptimalProduct, rightSubsgmtOptimalProduct, sgmtHead.rowCount,
                    sgmtTail.columnCount, minMultiplyCount)
                optimalProductArr(sgmtBeginIdx)(sgmtEndIdx) = sgmtOptimalProduct
            }
        }

        return optimalProductArr(0)(matrixCount - 1)
    }
}

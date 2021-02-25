package org.gb.sample.scala.intro_to_algo_book.matrix_multiplication

abstract class AbstractMatrixChainMultiplier extends MatrixChainMultiplier {

    private def verifyCompatibility(matrixChain: Seq[Matrix]): Int = {
        val matrixCount = matrixChain.length
        for (idx <- 0 until (matrixCount - 1)) {
            val matrix = matrixChain(idx)
            val nextMatrix = matrixChain(idx + 1)
            if(matrix.columnCount != nextMatrix.rowCount) {
                return idx
            }
        }
        return -1
    }

    override def computeOptimalOrder(matrixChain: Seq[Matrix]): Matrix = {
        if(matrixChain.length == 1) {
            val matrix = matrixChain.head
            return Matrix(matrix.simpleProductName, matrix.rowCount, matrix.columnCount)
        }

        val incompatibleIdx = verifyCompatibility(matrixChain)
        if(incompatibleIdx != -1) {
            val incompatibleMatrix1 = matrixChain(incompatibleIdx)
            val incompatibleMatrix2 = matrixChain(incompatibleIdx + 1)
            val errorMsg = String
              .format("Matrices at index %d: %s and index: %d: %s are incompatible. Chain multiplication aborted.",
                  incompatibleIdx, incompatibleMatrix1
                    .simpleProductName, incompatibleIdx + 1, incompatibleMatrix2.simpleProductName)
            throw new IllegalArgumentException(errorMsg)
        }

        return computeOptimalOrderInternal(matrixChain)
    }

    def computeOptimalOrderInternal(matrixChain: Seq[Matrix]): Matrix
}

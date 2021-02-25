package org.gb.sample.scala.intro_to_algo_book.matrix_multiplication

class SerialRecursiveMatrixChainMultiplier extends AbstractMatrixChainMultiplier {

    override def computeOptimalOrderInternal(matrixChain: Seq[Matrix]): Matrix = {
        var optimalProduct: Matrix = null
        if(matrixChain.length == 1) {
            optimalProduct = matrixChain.head
        }
        else {
            if(matrixChain.size == 2) {
                val leftMatrix: Matrix = matrixChain.head
                val rightMatrix: Matrix = matrixChain(1)
                val multiplyCount: Int = leftMatrix.rowCount * leftMatrix.columnCount * rightMatrix.columnCount
                optimalProduct = Matrix(leftMatrix, rightMatrix, leftMatrix.rowCount, rightMatrix.columnCount, multiplyCount)
            }
            else {
                val matrixCount: Int = matrixChain.size
                var minCumltvMultiplyCount: Int = Int.MaxValue
                for (splitIdx <- 0 until (matrixCount - 1)) {
                    val leftSubchain: Seq[Matrix] = matrixChain.slice(0, splitIdx + 1)
                    val rightSubchain: Seq[Matrix] = matrixChain.slice(splitIdx + 1, matrixCount)
                    val leftSubchainHead: Matrix = matrixChain.head
                    val leftSubchainTail: Matrix = matrixChain(splitIdx)
                    val rightSubchainTail: Matrix = matrixChain.last
                    val leftSubchainOptimalProduct: Matrix = computeOptimalOrderInternal(leftSubchain)
                    val rightSubchainOptimalProduct: Matrix = computeOptimalOrderInternal(rightSubchain)
                    val subchainsMultiplyCount: Int = leftSubchainHead.rowCount * leftSubchainTail.columnCount * rightSubchainTail.columnCount
                    val cumltvMultiplyCount: Int = leftSubchainOptimalProduct.cumulativeMultiplyCount + rightSubchainOptimalProduct
                      .cumulativeMultiplyCount + subchainsMultiplyCount
                    if(cumltvMultiplyCount < minCumltvMultiplyCount) {
                        minCumltvMultiplyCount = cumltvMultiplyCount
                        optimalProduct = Matrix(leftSubchainOptimalProduct, rightSubchainOptimalProduct, leftSubchainHead.rowCount, rightSubchainTail
                          .columnCount, minCumltvMultiplyCount)
                    }
                }
            }
        }

        return optimalProduct
    }
}

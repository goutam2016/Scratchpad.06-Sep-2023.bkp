package org.gb.sample.scala.intro_to_algo_book.matrix_multiplication

import scala.collection.mutable

class SerialRecursiveMapMemoizedMatrixChainMultiplier extends AbstractMatrixChainMultiplier {

    private def computeOptimalSplit(matrixChain: Seq[Matrix], cachedOptimalProducts: mutable.Map[String, Matrix])
    : (Int, Int, Matrix, Matrix) = {
        val matrixCount = matrixChain.size
        var optimalSplitIdx = 0
        var minMultiplyCount = Int.MaxValue
        var leftSubchainOptimalProduct: Matrix = null
        var rightSubchainOptimalProduct: Matrix = null

        for (splitIdx <- 0 until (matrixCount - 1)) {
            val leftSubchain = matrixChain.slice(0, splitIdx + 1)
            val rightSubchain = matrixChain.slice(splitIdx + 1, matrixCount)
            val leftSubchainHead = matrixChain.head
            val leftSubchainTail = matrixChain(splitIdx)
            val rightSubchainTail = matrixChain(matrixCount - 1)

            val leftSubchainProduct = computeOptimalOrderOrGetFromCache(leftSubchain, cachedOptimalProducts)
            val rightSubchainProduct = computeOptimalOrderOrGetFromCache(rightSubchain, cachedOptimalProducts)
            val subchainsMultiplyCount = leftSubchainHead.rowCount * leftSubchainTail.columnCount * rightSubchainTail.columnCount
            val cumltvMultiplyCount = leftSubchainProduct.cumulativeMultiplyCount + rightSubchainProduct
              .cumulativeMultiplyCount + subchainsMultiplyCount

            if(cumltvMultiplyCount < minMultiplyCount) {
                optimalSplitIdx = splitIdx
                minMultiplyCount = cumltvMultiplyCount
                leftSubchainOptimalProduct = leftSubchainProduct
                rightSubchainOptimalProduct = rightSubchainProduct
            }
        }
        return (optimalSplitIdx, minMultiplyCount, leftSubchainOptimalProduct, rightSubchainOptimalProduct)
    }

    private def computeOptimalOrderOrGetFromCache(matrixChain: Seq[Matrix], cachedOptimalProducts: mutable.Map[String, Matrix]): Matrix = {
        val matrixCount = matrixChain.size
        var optimalProduct: Matrix = null
        if(matrixCount == 1) {
            optimalProduct = matrixChain.head
        } else {
            val chainProductName = matrixChain.map(_.simpleProductName).mkString

            if(cachedOptimalProducts.contains(chainProductName)) {
                optimalProduct = cachedOptimalProducts(chainProductName)
            } else if(matrixCount == 2) {
                val leftMatrix = matrixChain.head
                val rightMatrix = matrixChain(1)
                val multiplyCount = leftMatrix.rowCount * leftMatrix.columnCount * rightMatrix.columnCount

                optimalProduct = Matrix(leftMatrix, rightMatrix, leftMatrix.rowCount, rightMatrix.columnCount, multiplyCount)
                cachedOptimalProducts.put(optimalProduct.simpleProductName, optimalProduct)
            } else {
                val (_, minMultiplyCount, leftSubchainOptimalProduct, rightSubchainOptimalProduct) = computeOptimalSplit(matrixChain, cachedOptimalProducts)
                val leftSubchainHead = matrixChain.head
                val rightSubchainTail = matrixChain(matrixCount - 1)

                optimalProduct = Matrix(leftSubchainOptimalProduct, rightSubchainOptimalProduct, leftSubchainHead.rowCount, rightSubchainTail
                  .columnCount, minMultiplyCount)
                cachedOptimalProducts.put(optimalProduct.simpleProductName, optimalProduct)
            }
        }

        return optimalProduct
    }

    override def computeOptimalOrderInternal(matrixChain: Seq[Matrix]): Matrix = {
        return computeOptimalOrderOrGetFromCache(matrixChain, mutable.Map())
    }
}

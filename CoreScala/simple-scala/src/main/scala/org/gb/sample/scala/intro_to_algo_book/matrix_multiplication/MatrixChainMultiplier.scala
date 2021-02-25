package org.gb.sample.scala.intro_to_algo_book.matrix_multiplication

trait MatrixChainMultiplier {

    def computeOptimalOrder(matrixChain: Seq[Matrix]): Matrix
}

object MatrixChainMultiplier {

    def getSerialRecursiveMatrixChainMultiplier = new SerialRecursiveMatrixChainMultiplier

    def getSerialBottomUpMatrixChainMultiplier = new SerialBottomUpMatrixChainMultiplier

    def getSerialRecursiveMapMemoizedMatrixChainMultiplier = new SerialRecursiveMapMemoizedMatrixChainMultiplier

    def getSerialRecursiveArrayMemoizedMatrixChainMultiplier = new SerialRecursiveArrayMemoizedMatrixChainMultiplier
}
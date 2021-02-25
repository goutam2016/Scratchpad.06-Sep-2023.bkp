package org.gb.sample.scala.intro_to_algo_book.matrix_multiplication

import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

class MatrixChainMultiplierTest extends AnyFunSuite with BeforeAndAfterAll {

    private var threeMatricesChain: Seq[Matrix] = null
    private var sixMatricesChain: Seq[Matrix] = null
    private var tenMatricesChain: Seq[Matrix] = null

    private def initThreeMatricesChain(): Unit = {
        val a = Matrix("A", 10, 100)
        val b = Matrix("B", 100, 5)
        val c = Matrix("C", 5, 50)
        threeMatricesChain = Seq(a, b, c)
    }

    private def initSixMatricesChain(): Unit = {
        val a = Matrix("A", 30, 35)
        val b = Matrix("B", 35, 15)
        val c = Matrix("C", 15, 5)
        val d = Matrix("D", 5, 10)
        val e = Matrix("E", 10, 20)
        val f = Matrix("F", 20, 25)
        sixMatricesChain = Seq(a, b, c, d, e, f)
    }

    private def initTenMatricesChain(): Unit = {
        val a = Matrix("A", 40, 20)
        val b = Matrix("B", 20, 30)
        val c = Matrix("C", 30, 10)
        val d = Matrix("D", 10, 30)
        val e = Matrix("E", 30, 52)
        val f = Matrix("F", 52, 48)
        val g = Matrix("G", 48, 71)
        val h = Matrix("H", 71, 80)
        val i = Matrix("I", 80, 57)
        val j = Matrix("J", 57, 11)
        tenMatricesChain = Seq(a, b, c, d, e, f, g, h, i, j)
    }

    override protected def beforeAll(): Unit = {
        initThreeMatricesChain()
        initSixMatricesChain()
        initTenMatricesChain()
    }

    private def computeOptimalOrder(matricesChain: Seq[Matrix], matrixChainMultiplier: MatrixChainMultiplier,
                                    exptdMinMultiplyCount: Int, exptdOptimalParenthesization: String): Unit = {
        // Invoke test target
        val product = matrixChainMultiplier.computeOptimalOrder(matricesChain)
        // Verify results
        //LOGGER
        //.info(String.format("Optimal parenthesization: %s, no. of multiplications: %d.", product.getParenthesizedName, product.getCumulativeMultiplyCount))
        assertResult(exptdMinMultiplyCount)(product.cumulativeMultiplyCount)
        assertResult(exptdOptimalParenthesization)(product.parenthesizedName)
    }

    private def computeOptimalOrder_3Matrices(matrixChainMultiplier: MatrixChainMultiplier): Unit = {
        // Setup expectations
        val exptdMinMultiplyCount = 7500
        val exptdOptimalParenthesization = "((A.B).C)"
        computeOptimalOrder(threeMatricesChain, matrixChainMultiplier, exptdMinMultiplyCount, exptdOptimalParenthesization)
    }

    private def computeOptimalOrder_6Matrices(matrixChainMultiplier: MatrixChainMultiplier): Unit = {
        val exptdMinMultiplyCount = 15125
        val exptdOptimalParenthesization = "((A.(B.C)).((D.E).F))"
        computeOptimalOrder(sixMatricesChain, matrixChainMultiplier, exptdMinMultiplyCount, exptdOptimalParenthesization)
    }

    private def computeOptimalOrder_10Matrices(matrixChainMultiplier: MatrixChainMultiplier): Unit = {
        val exptdMinMultiplyCount = 200310
        val exptdOptimalParenthesization = "(A.((B.C).((((((D.E).F).G).H).I).J)))"
        computeOptimalOrder(tenMatricesChain, matrixChainMultiplier, exptdMinMultiplyCount, exptdOptimalParenthesization)
    }

    test("3 matrices chain multiplication with SerialRecursiveMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialRecursiveMatrixChainMultiplier
        computeOptimalOrder_3Matrices(matrixChainMultiplier)
    }

    test("3 matrices chain multiplication with SerialBottomUpMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialBottomUpMatrixChainMultiplier
        computeOptimalOrder_3Matrices(matrixChainMultiplier)
    }

    test("3 matrices chain multiplication with SerialRecursiveMapMemoizedMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialRecursiveMapMemoizedMatrixChainMultiplier
        computeOptimalOrder_3Matrices(matrixChainMultiplier)
    }

    test("3 matrices chain multiplication with SerialRecursiveArrayMemoizedMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialRecursiveArrayMemoizedMatrixChainMultiplier
        computeOptimalOrder_3Matrices(matrixChainMultiplier)
    }

    test("6 matrices chain multiplication with SerialRecursiveMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialRecursiveMatrixChainMultiplier
        computeOptimalOrder_6Matrices(matrixChainMultiplier)
    }

    test("6 matrices chain multiplication with SerialBottomUpMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialBottomUpMatrixChainMultiplier
        computeOptimalOrder_6Matrices(matrixChainMultiplier)
    }

    test("6 matrices chain multiplication with SerialRecursiveMapMemoizedMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialRecursiveMapMemoizedMatrixChainMultiplier
        computeOptimalOrder_6Matrices(matrixChainMultiplier)
    }

    test("6 matrices chain multiplication with SerialRecursiveArrayMemoizedMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialRecursiveArrayMemoizedMatrixChainMultiplier
        computeOptimalOrder_6Matrices(matrixChainMultiplier)
    }

    test("10 matrices chain multiplication with SerialRecursiveMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialRecursiveMatrixChainMultiplier
        computeOptimalOrder_10Matrices(matrixChainMultiplier)
    }

    test("10 matrices chain multiplication with SerialBottomUpMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialBottomUpMatrixChainMultiplier
        computeOptimalOrder_10Matrices(matrixChainMultiplier)
    }

    test("10 matrices chain multiplication with SerialRecursiveMapMemoizedMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialRecursiveMapMemoizedMatrixChainMultiplier
        computeOptimalOrder_10Matrices(matrixChainMultiplier)
    }

    test("10 matrices chain multiplication with SerialRecursiveArrayMemoizedMultiplier") {
        val matrixChainMultiplier = MatrixChainMultiplier.getSerialRecursiveArrayMemoizedMatrixChainMultiplier
        computeOptimalOrder_10Matrices(matrixChainMultiplier)
    }
}

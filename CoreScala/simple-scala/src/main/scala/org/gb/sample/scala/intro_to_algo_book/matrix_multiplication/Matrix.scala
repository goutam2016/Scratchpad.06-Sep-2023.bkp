package org.gb.sample.scala.intro_to_algo_book.matrix_multiplication

class Matrix private(val parenthesizedName: String, val simpleProductName: String, val rowCount: Int, val columnCount: Int, val cumulativeMultiplyCount: Int) {
    override def toString: String = {
        val summaryInfo = "Name: %s, dimensions: %d x %d."
        String.format(summaryInfo, simpleProductName, rowCount, columnCount)
    }
}

object Matrix {
    def apply(name: String, rowCount: Int, columnCount: Int): Matrix = {
        new Matrix(name, name, rowCount, columnCount, 0)
    }

    def apply(leftPredecessor: Matrix, rightPredecessor: Matrix, rowCount: Int, columnCount: Int, cumulativeMultiplyCount: Int): Matrix = {
        val parenthesizedNameBuilder = new StringBuilder("(")
        val simpleProductNameBuilder = new StringBuilder(leftPredecessor.simpleProductName)
        val parenthesizedName = parenthesizedNameBuilder.append(leftPredecessor.parenthesizedName)
          .append(".")
          .append(rightPredecessor.parenthesizedName).append(")").toString()
        val simpleProductName = simpleProductNameBuilder.append(rightPredecessor.simpleProductName).toString
        new Matrix(parenthesizedName, simpleProductName, rowCount, columnCount, cumulativeMultiplyCount)
    }
}

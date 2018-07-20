package org.gb.sample.scala.maze

import scala.util.Random

object DataGenerator {
    def main(args: Array[String]): Unit = {
        val rnd = new Random
        val totalRows = 100
        val totalColumns = 100
        for (y <- 0 to (totalRows - 1)) {
            val noOfClosedPositions = y % 45
            val closedPositions = (0 to noOfClosedPositions).map(_ => rnd.nextInt(totalColumns))
            print("closedPositions = closedPositions")
            closedPositions.foreach(closedPosition => print(s" :+ ($closedPosition,$y)"))
            println()
        }
    }
}
package org.gb.sample.scala.conc

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Random

object Main {
    /*def main(args: Array[String]): Unit = {
        val future = Future.apply({
            Thread.sleep(5000)
            "Hello"
        })
        println(future.isCompleted)
        val result = Await.result(future, Duration(10, "seconds"))
        println(result)
        println(future.isCompleted)
    }*/

    /*def main(args: Array[String]): Unit = {
        val items = List(1, 2, 3, 4, 5)
        val sum = addItems(items)
        println(sum)
    }*/

    def main(args: Array[String]): Unit = {
        val rnd = new Random
        val totalRows = 15
        val totalColumns = 15
        for (y <- 0 to (totalRows - 1)) {
            val noOfClosedPositions = y % 7
            val closedPositions = (0 to noOfClosedPositions).map(_ => rnd.nextInt(totalColumns))
            print("closedPositions = closedPositions")
            closedPositions.foreach(closedPosition => print(s" :+ ($closedPosition,$y)"))
            println()
        }
    }

    def arrMin(arr: Array[Int]): Int = arr match {
        case Array(0) => 0
        case Array(x, y) => 1
        case Array(0, rest @ _*) => rest.min
    }

    def addItems(items: List[Int]): Int = items match {
        case List(x) => x
        case head :: tail => head + addItems(tail)
    }
}
package org.gb.sample.scala.conc

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Random

object Main {
    def main(args: Array[String]): Unit = {
        val future = Future.apply(sayHello("hello world"))
        println(future.isCompleted)
        val result = Await.result(future, Duration(10, "seconds"))
        println(result)
        println(future.isCompleted)
    }
    
    def sayHello(msg: String): String = {
        Thread.sleep(8000)
        msg
    }

    /*def main(args: Array[String]): Unit = {
        val items = List(1, 2, 3, 4, 5)
        val sum = addItems(items)
        println(sum)
    }*/

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
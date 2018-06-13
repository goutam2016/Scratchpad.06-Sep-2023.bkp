package org.gb.sample.scala.conc

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import org.scalatest.time.Milliseconds

object Main {
    def main(args: Array[String]): Unit = {
        val future = Future.apply({
            Thread.sleep(5000)
            "Hello"
        })
        println(future.isCompleted)
        val result = Await.result(future, Duration(10, "seconds"))
        println(result)
        println(future.isCompleted)
    }
}
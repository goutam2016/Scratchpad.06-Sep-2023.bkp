package org.gb.sample.scala.conc.bq

import java.util.concurrent.BlockingQueue
import java.util.concurrent.ArrayBlockingQueue

object Main {
    def main(args: Array[String]): Unit = {
        val blQueue = new ArrayBlockingQueue[Box](4);
        
        val producer = new Producer(blQueue)
        val consumer = new Consumer(blQueue)
        
        new Thread(producer).start()
        new Thread(consumer).start()
    }
}
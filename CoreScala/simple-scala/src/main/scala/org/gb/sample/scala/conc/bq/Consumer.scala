package org.gb.sample.scala.conc.bq

import java.util.concurrent.BlockingQueue

class Consumer(blQueue: BlockingQueue[Box]) extends Runnable {
    
    def run(): Unit = {
        blQueue.take()
        println("got a box")
        blQueue.take()
        println("got a box")
        blQueue.take()
        println("got a box")
    }
}
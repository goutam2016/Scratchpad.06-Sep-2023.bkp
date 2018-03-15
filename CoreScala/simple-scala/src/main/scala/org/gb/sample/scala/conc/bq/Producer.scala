package org.gb.sample.scala.conc.bq

import java.util.concurrent.BlockingQueue

class Producer(blQueue: BlockingQueue[Box]) extends Runnable {

    def run(): Unit = {
        blQueue.put(new Box())
        println("put a box")
        Thread.sleep(3000)
        blQueue.put(new Box())
        println("put a box")
        Thread.sleep(3000)
        blQueue.put(new Box())
        println("put a box")
    }
}
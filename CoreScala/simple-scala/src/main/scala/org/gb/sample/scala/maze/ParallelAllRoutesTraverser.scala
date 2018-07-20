package org.gb.sample.scala.maze

import java.util.concurrent.ForkJoinPool
import scala.collection.concurrent.TrieMap
import java.util.concurrent.TimeUnit

class ParallelAllRoutesTraverser(val totalRows: Int, val totalColumns: Int, val maze: Map[Coordinate, Status]) extends Traverser {

    override def findShortestPath(startPos: Coordinate, destPos: Coordinate): Route = {
        val initialRoute = new Route
        initialRoute.addPosition(startPos)
        val forkJoinPool = new ForkJoinPool(8)
        val routeFinder = new RouteFinder(totalRows, totalColumns, maze, destPos, initialRoute)
        val shortestRoute = forkJoinPool.invoke(routeFinder)
        forkJoinPool.shutdown()
        forkJoinPool.awaitTermination(5, TimeUnit.SECONDS);
        return shortestRoute
    }
}
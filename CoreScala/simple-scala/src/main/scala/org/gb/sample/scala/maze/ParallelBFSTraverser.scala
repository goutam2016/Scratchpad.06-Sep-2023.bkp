package org.gb.sample.scala.maze

import java.util.concurrent.ForkJoinPool
import scala.collection.concurrent.TrieMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.CountDownLatch
import scala.annotation.tailrec
import scala.collection.parallel.ParSeq

class ParallelBFSTraverser(val totalRows: Int, val totalColumns: Int, val maze: Map[Coordinate, Status]) extends Traverser {

    val discvPositions: scala.collection.concurrent.Map[Coordinate, Boolean] = TrieMap()

    override def findShortestPath(startPos: Coordinate, destPos: Coordinate): Route = {
        val initialRoute = new Route
        initialRoute.addPosition(startPos)
        findRouteToDestination(destPos, Seq(initialRoute).par)
    }

    override protected def extend(currentRoute: Route, openNeighbour: Coordinate): Route = {
        val alreadyDiscovered = discvPositions.putIfAbsent(openNeighbour, true)

        if (alreadyDiscovered == None) {
            super.extend(currentRoute, openNeighbour)
        } else {
            null
        }
    }

    @tailrec
    private def findRouteToDestination(destPos: Coordinate, foundRoutes: ParSeq[Route]): Route = {
        val extndRoutes = foundRoutes.map(foundRoute => addNeighbour(foundRoute.traversedPositions.last, foundRoute)).flatten

        //println(s"No. of extended routes: ${extndRoutes.size}")

        if (extndRoutes.isEmpty) {
            println("No extended routes, returning null")
            return null
        }

        val firstCompleteRoute = extndRoutes.filter(extndRoute => extndRoute.traversedPositions.last.equals(destPos)).headOption

        if (firstCompleteRoute.nonEmpty) {
            return firstCompleteRoute.get
        } else {
            return findRouteToDestination(destPos, extndRoutes)
        }
    }
}

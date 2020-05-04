package org.gb.sample.scala.maze

import scala.collection.mutable.Buffer
import scala.annotation.tailrec

class BFSTraverser(val totalRows: Int, val totalColumns: Int, val maze: Map[Coordinate, Status]) extends Traverser {

    override def findShortestPath(startPos: Coordinate, destPos: Coordinate): Route = {
        val initialRoute = new Route
        setDiscovered(startPos)
        initialRoute.addPosition(startPos)
        findRouteToDestination(destPos, Seq(initialRoute))
    }

    private def setDiscovered(position: Coordinate): Unit = {
        if (maze.contains(position)) {
            val status = maze.get(position).get
            status.isDiscovered = true
        }
    }

    private def isDiscovered(position: Coordinate): Boolean = {
        if (maze.contains(position)) {
            val status = maze.get(position).get
            status.isDiscovered
        } else {
            false
        }
    }

    override protected def isValid(position: Coordinate, currentRoute: Route): Boolean = {
        if (!super.isValid(position, currentRoute)) {
            false
        } else {
            val discovered = isDiscovered(position)
            //println(s"inside isValid: (${position.x}, ${position.y}): ${discovered}")
            !discovered
        }
    }

    override protected def extend(currentRoute: Route, openNeighbour: Coordinate): Route = {
        setDiscovered(openNeighbour)
        super.extend(currentRoute, openNeighbour)
    }

    @tailrec
    private def findRouteToDestination(destPos: Coordinate, foundRoutes: Seq[Route]): Route = {
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
package org.gb.sample.scala.maze

import scala.collection.mutable.Buffer
import scala.annotation.tailrec

class AllRoutesTraverser(val totalRows: Int, val totalColumns: Int, val maze: Map[Coordinate, Status]) extends Traverser {

    override def findShortestPath(startPos: Coordinate, destPos: Coordinate): Route = {
        val initialRoute = new Route
        initialRoute.addPosition(startPos)
        val routes = findRoutes(startPos, destPos, initialRoute)
        println(s"Total routes found: ${routes.size}")
        val shortestRoute = routes.minBy(_.traversedPositions.size)
        shortestRoute
    }

    override protected def isValid(position: Coordinate, currentRoute: Route): Boolean = {
        if (!super.isValid(position, currentRoute)) {
            false
        } else {
            val hasCycle = currentRoute.traversedPositions.contains(position)
            //println(s"inside isValid: (${position.x}, ${position.y}): ${discovered}")
            !hasCycle
        }
    }

    def findRoutes(currentPosition: Coordinate, destPos: Coordinate, currentRoute: Route): Seq[Route] = {
        val extndRoutes = addNeighbour(currentPosition, currentRoute)

        println(s"No. of extended routes: ${extndRoutes.size}")

        var completedRoutes = extndRoutes.filter(extndRoute => extndRoute.traversedPositions.last.equals(destPos))

        if (completedRoutes.isEmpty) {
            completedRoutes = extndRoutes.map(extndRoute => findRoutes(extndRoute.traversedPositions.last, destPos, extndRoute)).flatten
        }

        completedRoutes.toSeq
    }
}

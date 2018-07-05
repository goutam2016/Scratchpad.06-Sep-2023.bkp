package org.gb.sample.scala.maze

import scala.collection.mutable.Buffer
import scala.annotation.tailrec

class AllRoutesTraverser(val totalRows: Int, val totalColumns: Int, val maze: Map[Coordinate, Boolean]) extends Traverser {

    override def findShortestPath(startPos: Coordinate, destPos: Coordinate): Route = {
        val initialRoute = new Route
        initialRoute.addPosition(startPos)
        val routes = findRoutes(startPos, destPos, initialRoute)
        println(s"Total routes found: ${routes.size}")
        val shortestRoute = routes.minBy(_.traversedPositions.size)
        shortestRoute
    }

    def findRoutes(currentPosition: Coordinate, destPos: Coordinate, currentRoute: Route): Seq[Route] = {
        val extndRoutes = addNeighbour(currentPosition, currentRoute)
        var completedRoutes = extndRoutes.filter(extndRoute => extndRoute.traversedPositions.last.equals(destPos))

        if (completedRoutes.isEmpty) {
            completedRoutes = extndRoutes.map(extndRoute => findRoutes(extndRoute.traversedPositions.last, destPos, extndRoute)).flatten
        }
        
        completedRoutes.toSeq
    }
}

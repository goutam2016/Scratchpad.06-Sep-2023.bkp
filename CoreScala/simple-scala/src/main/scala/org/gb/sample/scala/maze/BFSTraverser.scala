package org.gb.sample.scala.maze

import scala.collection.mutable.Buffer
import scala.annotation.tailrec

class BFSTraverser(val totalRows: Int, val totalColumns: Int, val maze: Map[Coordinate, Boolean]) extends Traverser {

    override def findShortestPath(startPos: Coordinate, destPos: Coordinate): Route = {
        val initialRoute = new Route
        initialRoute.addPosition(startPos)
        findRouteToDestination(destPos, Seq(initialRoute))
    }

    override protected def isOpen(position: Coordinate): Boolean = {
        if (position.isDiscovered) {
            false
        } else {
            super.isOpen(position)
        }
    }

    @tailrec
    private def findRouteToDestination(destPos: Coordinate, foundRoutes: Seq[Route]): Route = {
        val extndRoutes = foundRoutes.map(foundRoute => addNeighbour(foundRoute.traversedPositions.last, foundRoute)).flatten
        
        if(extndRoutes.isEmpty) {
            return null
        }
        
        val firstCompleteRoute = extndRoutes.filter(extndRoute => extndRoute.traversedPositions.last.equals(destPos)).headOption

        if (firstCompleteRoute.nonEmpty) {
            return firstCompleteRoute.get
        } else {
            val incompleteRoutes = extndRoutes.filterNot(extndRoute => extndRoute.traversedPositions.last.equals(destPos))
            return findRouteToDestination(destPos, incompleteRoutes)
        }
    }
}
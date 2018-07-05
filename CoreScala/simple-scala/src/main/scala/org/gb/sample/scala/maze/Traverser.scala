package org.gb.sample.scala.maze

trait Traverser {

    val totalRows: Int
    val totalColumns: Int
    val maze: Map[Coordinate, Boolean]

    def findShortestPath(startPos: Coordinate, destPos: Coordinate): Route

    private def findOpenNeighbours(position: Coordinate): Seq[Coordinate] = {
        val openHorizNeighbours = Seq(-1, 1).map(offset => Coordinate(position.x + offset, position.y)).filter(isOpen)
        val openVertNeighbours = Seq(-1, 1).map(offset => Coordinate(position.x, position.y + offset)).filter(isOpen)
        openHorizNeighbours ++ openVertNeighbours
    }

    protected def isOpen(position: Coordinate): Boolean = {
        if (position.x >= 0 && position.x < totalColumns && position.y >= 0 && position.y < totalRows) {
            maze.getOrElse(position, false)
        } else {
            false
        }
    }

    protected final def addNeighbour(currentPosition: Coordinate, currentRoute: Route): Seq[Route] = {
        val openNeighbours = findOpenNeighbours(currentPosition)
        val extndRoutes = for (
            openNeighbour <- openNeighbours if (!currentRoute.traversedPositions.contains(openNeighbour))
        ) yield {
            extend(currentRoute, openNeighbour)
        }
        extndRoutes
    }

    private def extend(currentRoute: Route, openNeighbour: Coordinate): Route = {
        val extndRoute = currentRoute.copy()
        openNeighbour.isDiscovered = true
        extndRoute.addPosition(openNeighbour)
        extndRoute
    }
}

object Traverser {
    def getAllRoutesTraverser(totalRows: Int, totalColumns: Int, maze: Map[Coordinate, Boolean]): Traverser = {
        new AllRoutesTraverser(totalRows, totalColumns, maze)
    }
    def getBFSTraverser(totalRows: Int, totalColumns: Int, maze: Map[Coordinate, Boolean]): Traverser = {
        new BFSTraverser(totalRows, totalColumns, maze)
    }
}
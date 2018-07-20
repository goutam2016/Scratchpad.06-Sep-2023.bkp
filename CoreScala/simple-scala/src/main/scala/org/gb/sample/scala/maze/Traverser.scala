package org.gb.sample.scala.maze

trait Traverser {

    val totalRows: Int
    val totalColumns: Int
    val maze: Map[Coordinate, Status]

    def findShortestPath(startPos: Coordinate, destPos: Coordinate): Route

    private def findValidNeighbours(position: Coordinate, currentRoute: Route): Seq[Coordinate] = {
        val openHorizNeighbours = Seq(-1, 1).map(offset => Coordinate(position.x + offset, position.y)).filter(isValid(_, currentRoute))
        val openVertNeighbours = Seq(-1, 1).map(offset => Coordinate(position.x, position.y + offset)).filter(isValid(_, currentRoute))
        openHorizNeighbours ++ openVertNeighbours
    }

    protected def isValid(position: Coordinate, currentRoute: Route): Boolean = {
        if (position.x >= 0 && position.x < totalColumns && position.y >= 0 && position.y < totalRows) {
            val status = maze.get(position)
            val isValid = if (status.isEmpty) false else status.get.isOpen
            isValid
        } else {
            false
        }
    }

    protected final def addNeighbour(currentPosition: Coordinate, currentRoute: Route): Seq[Route] = {
        val openNeighbours = findValidNeighbours(currentPosition, currentRoute)
        val extndRoutes = for (
            openNeighbour <- openNeighbours
        ) yield {
            extend(currentRoute, openNeighbour)
        }
        extndRoutes.filterNot(_ == null)
    }

    protected def extend(currentRoute: Route, openNeighbour: Coordinate): Route = {
        val extndRoute = currentRoute.copy()
        extndRoute.addPosition(openNeighbour)
        extndRoute
    }
}

object Traverser {
    def getAllRoutesTraverser(totalRows: Int, totalColumns: Int, maze: Map[Coordinate, Status]): Traverser = {
        new AllRoutesTraverser(totalRows, totalColumns, maze)
    }
    def getBFSTraverser(totalRows: Int, totalColumns: Int, maze: Map[Coordinate, Status]): Traverser = {
        new BFSTraverser(totalRows, totalColumns, maze)
    }
    def getParallelAllRoutesTraverser(totalRows: Int, totalColumns: Int, maze: Map[Coordinate, Status]): Traverser = {
        new ParallelAllRoutesTraverser(totalRows, totalColumns, maze)
    }
    def getParallelBFSTraverser(totalRows: Int, totalColumns: Int, maze: Map[Coordinate, Status]): Traverser = {
        new ParallelBFSTraverser(totalRows, totalColumns, maze)
    }
}
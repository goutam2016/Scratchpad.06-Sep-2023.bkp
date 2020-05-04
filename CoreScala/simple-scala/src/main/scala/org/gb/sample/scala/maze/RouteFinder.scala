package org.gb.sample.scala.maze

import java.util.concurrent.RecursiveTask
import java.util.concurrent.ForkJoinTask
import scala.collection.JavaConverters._

class RouteFinder(totalRows: Int, totalColumns: Int, maze: Map[Coordinate, Status], destPos: Coordinate, currentRoute: Route) extends RecursiveTask[Route] {

    private def findValidNeighbours(position: Coordinate, currentRoute: Route): Seq[Coordinate] = {
        val openHorizNeighbours = Seq(-1, 1).map(offset => Coordinate(position.x + offset, position.y)).filter(isValid(_, currentRoute))
        val openVertNeighbours = Seq(-1, 1).map(offset => Coordinate(position.x, position.y + offset)).filter(isValid(_, currentRoute))
        openHorizNeighbours ++ openVertNeighbours
    }

    protected def isValid(position: Coordinate, currentRoute: Route): Boolean = {
        if (position.x >= 0 && position.x < totalColumns && position.y >= 0 && position.y < totalRows) {
            val status = maze.get(position)
            val isValid = if (status.isEmpty) false else (status.get.isOpen && !currentRoute.traversedPositions.contains(position))
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
        extndRoutes
    }

    protected def extend(currentRoute: Route, openNeighbour: Coordinate): Route = {
        val extndRoute = currentRoute.copy()
        extndRoute.addPosition(openNeighbour)
        extndRoute
    }

    override protected def compute(): Route = {
        val extndRoutes = addNeighbour(currentRoute.traversedPositions.last, currentRoute)
        //println(s"No. of extended routes: ${extndRoutes.size}")
        if (extndRoutes.isEmpty) {
            //println("No extended routes, returning null")
            return null
        }

        val completedRoutes = extndRoutes.filter(extndRoute => extndRoute.traversedPositions.last.equals(destPos))

        if (completedRoutes.nonEmpty) {
            //println(s"Found ${completedRoutes.size} completed routes")
            //completedRoutes.foreach(cmplRt => println(s"completed route of length: ${cmplRt.traversedPositions.size}"))
            return completedRoutes.head
        }

        val subsequentRouteFinders = extndRoutes.map(new RouteFinder(totalRows, totalColumns, maze, destPos, _))
        val submittedRouteFinderTasks = ForkJoinTask.invokeAll(subsequentRouteFinders.asJava).asScala
        val subsequentCompletedRoutes = submittedRouteFinderTasks.map(_.join()).filterNot(_ == null)

        if (subsequentCompletedRoutes.isEmpty) {
            null
        } else {
            subsequentCompletedRoutes.minBy(_.traversedPositions.size)
        }
    }
}
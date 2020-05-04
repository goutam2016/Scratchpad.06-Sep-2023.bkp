package org.gb.sample.scala.maze

import org.scalatest.FunSuite
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import scala.concurrent.Await

class TraverserTest extends FunSuite {

    private def closedPositions(): Seq[(Int, Int)] = {
        var closedPositions = Seq.empty[(Int, Int)]
        closedPositions = closedPositions :+ (7, 0) :+ (8, 0)
        closedPositions = closedPositions :+ (0, 1) :+ (2, 1) :+ (3, 1) :+ (7, 1) :+ (8, 1) :+ (9, 1)
        closedPositions = closedPositions :+ (0, 2) :+ (2, 2) :+ (4, 2) :+ (6, 2)
        closedPositions = closedPositions :+ (4, 3)
        closedPositions = closedPositions :+ (1, 4) :+ (2, 4) :+ (5, 4) :+ (7, 4) :+ (8, 4)
        closedPositions = closedPositions :+ (1, 5) :+ (2, 5)
        closedPositions = closedPositions :+ (3, 6) :+ (4, 6) :+ (5, 6) :+ (8, 6)
        closedPositions = closedPositions :+ (0, 7) :+ (2, 7) :+ (5, 7) :+ (6, 7) :+ (8, 7) :+ (9, 7)
        closedPositions = closedPositions :+ (2, 8)
        closedPositions = closedPositions :+ (0, 9) :+ (6, 9) :+ (7, 9)
        closedPositions
    }

    private def buildMaze(totalRows: Int, totalColumns: Int): Map[Coordinate, Status] = {
        val closedCoords = closedPositions()

        val coordVsStatusPairs = for (rowIdx <- 0 to totalRows; colIdx <- 0 to totalColumns) yield {
            val position = Coordinate(rowIdx, colIdx)
            val isOpen = !closedCoords.contains((rowIdx, colIdx))
            val status = new Status(isOpen)
            (position, status)
        }

        coordVsStatusPairs.toMap
    }

    test("the shortest path between 2 positions in a maze - check all paths") {
        val totalRows = 10
        val totalColumns = 10
        val maze = buildMaze(totalRows, totalColumns)
        val traverser = Traverser.getAllRoutesTraverser(totalRows, totalColumns, maze)
        val shortestPath = traverser.findShortestPath(Coordinate(4, 3), Coordinate(9, 9))
        assertResult(12)(shortestPath.traversedPositions.size)
        //println(shortestPath.traversedPositions.size)
        //shortestPath.traversedPositions.foreach(pos => println(s"(${pos.x}, ${pos.y})"))
    }
    test("the shortest path between 2 positions in a maze - breadth first search") {
        val totalRows = 10
        val totalColumns = 10
        val maze = buildMaze(totalRows, totalColumns)
        val traverser = Traverser.getBFSTraverser(totalRows, totalColumns, maze)
        val shortestPath = traverser.findShortestPath(Coordinate(4, 3), Coordinate(9, 9))
        assertResult(12)(shortestPath.traversedPositions.size)
        //println(s"Length of shortest path: ${shortestPath.traversedPositions.size}")
        //shortestPath.traversedPositions.foreach(pos => println(s"(${pos.x}, ${pos.y})"))
    }
    test("the shortest path between 2 positions in a maze - parallel check all paths") {
        val totalRows = 10
        val totalColumns = 10
        val maze = buildMaze(totalRows, totalColumns)
        val traverser = Traverser.getParallelAllRoutesTraverser(totalRows, totalColumns, maze)
        val shortestPath = traverser.findShortestPath(Coordinate(4, 3), Coordinate(9, 9))
        assertResult(12)(shortestPath.traversedPositions.size)
        //println(s"Length of shortest path: ${shortestPath.traversedPositions.size}")
        //shortestPath.traversedPositions.foreach(pos => println(s"(${pos.x}, ${pos.y})"))
    }
    test("the shortest path between 2 positions in a maze - parallel breadth first search") {
        val totalRows = 10
        val totalColumns = 10
        val maze = buildMaze(totalRows, totalColumns)
        val traverser = Traverser.getParallelBFSTraverser(totalRows, totalColumns, maze)
        val shortestPath = traverser.findShortestPath(Coordinate(4, 3), Coordinate(9, 9))
        assertResult(12)(shortestPath.traversedPositions.size)
        //println(s"Length of shortest path: ${shortestPath.traversedPositions.size}")
        //shortestPath.traversedPositions.foreach(pos => println(s"(${pos.x}, ${pos.y})"))
    }
}
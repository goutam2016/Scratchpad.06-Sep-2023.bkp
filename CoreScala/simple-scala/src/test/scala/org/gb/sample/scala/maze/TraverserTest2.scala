package org.gb.sample.scala.maze

import org.scalatest.FunSuite
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import scala.concurrent.Await

class TraverserTest2 extends FunSuite {

    private def closedPositions(): Seq[(Int, Int)] = {
        var closedPositions = Seq.empty[(Int, Int)]
        closedPositions = closedPositions :+ (7, 0)
        closedPositions = closedPositions :+ (10, 1) :+ (14, 1)
        closedPositions = closedPositions :+ (11, 2) :+ (1, 2) :+ (2, 2)
        closedPositions = closedPositions :+ (4, 3) :+ (1, 3) :+ (4, 3) :+ (4, 3)
        closedPositions = closedPositions :+ (13, 4) :+ (14, 4) :+ (5, 4) :+ (5, 4) :+ (9, 4)
        closedPositions = closedPositions :+ (7, 5) :+ (11, 5) :+ (1, 5) :+ (10, 5) :+ (11, 5) :+ (5, 5)
        closedPositions = closedPositions :+ (13, 6) :+ (4, 6) :+ (7, 6) :+ (5, 6) :+ (2, 6) :+ (8, 6) :+ (14, 6)
        closedPositions = closedPositions :+ (3, 7)
        closedPositions = closedPositions :+ (3, 8) :+ (12, 8)
        closedPositions = closedPositions :+ (12, 9) :+ (12, 9) :+ (9, 9)
        closedPositions = closedPositions :+ (3, 10) :+ (0, 10) :+ (4, 10) :+ (8, 10)
        closedPositions = closedPositions :+ (1, 11) :+ (5, 11) :+ (1, 11) :+ (3, 11) :+ (4, 11)
        closedPositions = closedPositions :+ (0, 12) :+ (4, 12) :+ (0, 12) :+ (13, 12) :+ (3, 12) :+ (5, 12)
        closedPositions = closedPositions :+ (8, 13) :+ (4, 13) :+ (2, 13) :+ (6, 13) :+ (0, 13) :+ (12, 13) :+ (11, 13)
        closedPositions = closedPositions :+ (4, 14)
        closedPositions
    }

    private def buildMaze(totalRows: Int, totalColumns: Int): Map[Coordinate, Boolean] = {
        val closedCoords = closedPositions()

        val coordVsIsOpenPairs = for (rowIdx <- 0 to totalRows; colIdx <- 0 to totalColumns) yield {
            val position = Coordinate(rowIdx, colIdx)
            val isOpen = !closedCoords.contains((rowIdx, colIdx))
            (position, isOpen)
        }

        coordVsIsOpenPairs.toMap
    }

    test("the shortest path between 2 positions in a maze - check all paths") {
        val totalRows = 15
        val totalColumns = 15
        val maze = buildMaze(totalRows, totalColumns)
        val traverser = Traverser.getAllRoutesTraverser(totalRows, totalColumns, maze)
        val shortestPath = traverser.findShortestPath(Coordinate(0, 0), Coordinate(14, 14))
        println(shortestPath.traversedPositions.size)
        shortestPath.traversedPositions.foreach(pos => println(s"(${pos.x}, ${pos.y})"))
    }
    test("the shortest path between 2 positions in a maze - breadth first search") {
        val totalRows = 15
        val totalColumns = 15
        val maze = buildMaze(totalRows, totalColumns)
        val traverser = Traverser.getBFSTraverser(totalRows, totalColumns, maze)
        val shortestPath = traverser.findShortestPath(Coordinate(0, 0), Coordinate(14, 14))
        println(shortestPath.traversedPositions.size)
        shortestPath.traversedPositions.foreach(pos => println(s"(${pos.x}, ${pos.y})"))
    }
}
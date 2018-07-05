package org.gb.sample.scala.maze

import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Benchmark

@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
class TraverserBenchmark {

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

    private def buildMaze(totalRows: Int, totalColumns: Int): Map[Coordinate, Boolean] = {
        val closedCoords = closedPositions()

        val coordVsIsOpenPairs = for (rowIdx <- 0 to totalRows; colIdx <- 0 to totalColumns) yield {
            val position = Coordinate(rowIdx, colIdx)
            val isOpen = !closedCoords.contains((rowIdx, colIdx))
            (position, isOpen)
        }

        coordVsIsOpenPairs.toMap
    }

    @Benchmark
    def findShortestPath_AllRoutes(): Unit = {
        val totalRows = 10
        val totalColumns = 10
        val maze = buildMaze(totalRows, totalColumns)
        val traverser = Traverser.getAllRoutesTraverser(totalRows, totalColumns, maze)
        val shortestPath = traverser.findShortestPath(Coordinate(0, 0), Coordinate(9, 9))
        println(s"Length of shortest path: ${shortestPath.traversedPositions.size}")
    }

    @Benchmark
    def findShortestPath_BFS(): Unit = {
        val totalRows = 10
        val totalColumns = 10
        val maze = buildMaze(totalRows, totalColumns)
        val traverser = Traverser.getBFSTraverser(totalRows, totalColumns, maze)
        val shortestPath = traverser.findShortestPath(Coordinate(0, 0), Coordinate(9, 9))
        println(s"Length of shortest path: ${shortestPath.traversedPositions.size}")
    }
}
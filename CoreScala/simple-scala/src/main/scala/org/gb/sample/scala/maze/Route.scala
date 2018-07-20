package org.gb.sample.scala.maze

import scala.collection.mutable.Buffer

class Route {

    val traversedPositions = Buffer.empty[Coordinate]

    def addPosition(position: Coordinate): Unit = {
        traversedPositions.+=(position)
    }
    def copy(): Route = {
        val copiedRoute = new Route
        copiedRoute.traversedPositions ++= traversedPositions
        //copiedRoute.traversedPositions.foreach(pos => print(s"(${pos.x}, ${pos.y}):${pos.isDiscovered} "))
        //println()
        copiedRoute
    }
}
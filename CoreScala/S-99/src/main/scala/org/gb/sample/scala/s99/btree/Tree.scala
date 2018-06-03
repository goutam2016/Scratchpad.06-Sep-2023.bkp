package org.gb.sample.scala.s99.btree

import scala.math.pow

sealed abstract class Tree[+T] {
    def p56_isSymmetric: Boolean
    def p56_isMirrorOf[V](otherTree: Tree[V]): Boolean
    def p61_leafCount: Int = p61A_collectLeafValues.size
    def p61A_collectLeafValues: List[T]
}

case class Node[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T] {
    override def toString(): String = {
        "T(" + value.toString + " " + left.toString + " " + right.toString + ")"
    }
    def p56_isSymmetric: Boolean = {
        left.p56_isMirrorOf(right)
    }
    def p56_isMirrorOf[V](otherTree: Tree[V]): Boolean = otherTree match {
        case node: Node[V] => value.equals(node.value) && left.p56_isMirrorOf(node.right) && right.p56_isMirrorOf(node.left)
        case End => false
    }
    def p61A_collectLeafValues: List[T] = (left, right) match {
        case (End, End) => List(value)
        case _ => left.p61A_collectLeafValues ++ right.p61A_collectLeafValues
    }
}

case object End extends Tree[Nothing] {
    override def toString() = "."
    def p56_isSymmetric(): Boolean = true
    def p56_isMirrorOf[V](otherTree: Tree[V]): Boolean = otherTree == End
    def p61A_collectLeafValues: List[Nothing] = Nil
}

object Node {
    def apply[T](value: T): Node[T] = Node(value, End, End)
}

object Tree {
    def p55_balanced[T](totalNodes: Int, value: T): Tree[T] = {
        if (totalNodes == 0) {
            End
        } else if (totalNodes == 1) {
            Node(value)
        } else {
            val children = getNodesForLevel(totalNodes, value, 1, 1)
            val leftChild = children(0)
            val rightChild = if (children.size > 1) children(1) else End
            Node(value, leftChild, rightChild)
        }
    }

    private def getNodesForLevel[T](totalNodes: Int, value: T, nodeCount: Int, level: Int): List[Tree[T]] = {
        val avlNodesForLevel = pow(2, level).toInt

        if (nodeCount + avlNodesForLevel >= totalNodes) {
            val reqdLeaves = totalNodes - nodeCount
            (1 to reqdLeaves).map(_ => Node(value)).toList
        } else {
            val children = getNodesForLevel(totalNodes, value, nodeCount + avlNodesForLevel, level + 1)

            val reqdNodesWithBothChildren = children.size / 2
            val reqdNodesWithLeftChild = children.size % 2
            val reqdLeaves = avlNodesForLevel - reqdNodesWithBothChildren - reqdNodesWithLeftChild

            val nodesWithBothChildren = (0 until reqdNodesWithBothChildren).map(nodeIdx => (children(nodeIdx * 2), children(nodeIdx * 2 + 1))).
                map(leftRightChildrenPair => Node(value, leftRightChildrenPair._1, leftRightChildrenPair._2)).toList
            val nodesWithLeftChild = (0 until reqdNodesWithLeftChild).map(nodeIdx => children((nodeIdx + reqdNodesWithBothChildren) * 2)).
                map(leftChild => (Node(value, leftChild, End))).toList
            val leaves = (0 until reqdLeaves).map(nodeIdx => Node(value)).toList

            nodesWithBothChildren ++ nodesWithLeftChild ++ leaves
        }
    }

}
package org.gb.sample.scala.s99.btree

import scala.math.pow

sealed abstract class Tree[+T] {
    def p56_isSymmetric: Boolean
    def p56_isMirrorOf[V](otherTree: Tree[V]): Boolean
    def p61_leafCount: Int = p61A_collectLeafValues.size
    def p61A_collectLeafValues: List[T]
    def p67_stringify(): String
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
    def p67_stringify(): String = (left, right) match {
        case (End, End) => value.toString()
        case _ => value.toString() + "(" + left.p67_stringify() + "," + right.p67_stringify() + ")"
    }
}

case object End extends Tree[Nothing] {
    override def toString() = "."
    def p56_isSymmetric(): Boolean = true
    def p56_isMirrorOf[V](otherTree: Tree[V]): Boolean = otherTree == End
    def p61A_collectLeafValues: List[Nothing] = Nil
    def p67_stringify(): String = ""
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

    def p67_fromString(strReprs: String): Tree[String] = {
        val firstOpenParenIdx = strReprs.indexOf("(")
        val lastCloseParenIdx = strReprs.lastIndexOf(")")
        val nodeValue = strReprs.substring(0, firstOpenParenIdx)
        val childrenAsString = strReprs.substring(firstOpenParenIdx + 1, lastCloseParenIdx)
        val children = parseChildren(childrenAsString)
        Node(nodeValue, children._1, children._2)
    }

    private def parseChildren(childrenAsString: String): (Tree[String], Tree[String]) = {
        var foundParenthesises = 0
        var unbalancedParenthesises = 0
        val charSelector = (charWithIdx: (Char, Int)) => {
            if (charWithIdx._1 == '(') {
                foundParenthesises += 1
                unbalancedParenthesises += 1
            } else if (charWithIdx._1 == ')') {
                foundParenthesises += 1
                unbalancedParenthesises -= 1
            }
            if (foundParenthesises == 0) {
                true
            } else {
                if (unbalancedParenthesises != 0) {
                    true
                } else {
                    false
                }
            }
        }
        val firstOpenParenIdx = childrenAsString.indexOf("(")
        val firstCommaIdx = childrenAsString.indexOf(",")

        if (firstOpenParenIdx == -1) { //No grand-children
            if (firstCommaIdx == -1) { //Has left child, no right child
                val leftChildValue = childrenAsString
                return (Node(leftChildValue), End)
            } else { //Both left and right children present with no grand-children
                val leftChildValue = childrenAsString.substring(0, firstCommaIdx)
                val rightChildValue = childrenAsString.substring(firstCommaIdx + 1)
                val leftChild = if (leftChildValue.trim().length() == 0) End else Node(leftChildValue)
                val rightChild = if (rightChildValue.trim().length() == 0) End else Node(rightChildValue)
                return (leftChild, rightChild)
            }
        } else if (firstCommaIdx == -1) { //Has left child with left-grand-child, no right child
            val leftChildOpenParenIdx = firstOpenParenIdx
            val leftChildValue = childrenAsString.substring(0, leftChildOpenParenIdx)
            val leftChildCloseParenIdx = childrenAsString.toCharArray().zipWithIndex.takeWhile(charSelector).map(_._2).last + 1
            val leftGrandChildrenAsString = childrenAsString.substring(leftChildOpenParenIdx + 1, leftChildCloseParenIdx)
            val leftGrandChildren = parseChildren(leftGrandChildrenAsString)
            val leftChild = Node(leftChildValue, leftGrandChildren._1, leftGrandChildren._2)
            return (leftChild, End)
        } else if (firstCommaIdx < firstOpenParenIdx) { //Has left child with no grand-child, has right child
            val leftChildValue = childrenAsString.substring(0, firstCommaIdx)
            val leftChild = if (leftChildValue.trim().length() == 0) End else Node(leftChildValue)
            val rightChildOpenParenIdx = firstOpenParenIdx
            val rightChildValue = childrenAsString.substring(firstCommaIdx + 1, rightChildOpenParenIdx)
            val rightChildCloseParenIdx = childrenAsString.toCharArray().zipWithIndex.takeWhile(charSelector).map(_._2).last + 1
            val rightGrandChildrenAsString = childrenAsString.substring(rightChildOpenParenIdx + 1, rightChildCloseParenIdx)
            val rightGrandChildren = parseChildren(rightGrandChildrenAsString)
            val rightChild = Node(rightChildValue, rightGrandChildren._1, rightGrandChildren._2)
            return (leftChild, rightChild)
        } else { //Has left child with grand-children, has right child
            val leftChildOpenParenIdx = firstOpenParenIdx
            val leftChildCloseParenIdx = childrenAsString.toCharArray().zipWithIndex.takeWhile(charSelector).map(_._2).last + 1
            val leftChildValue = childrenAsString.substring(0, leftChildOpenParenIdx)
            val leftGrandChildrenAsString = childrenAsString.substring(leftChildOpenParenIdx + 1, leftChildCloseParenIdx)
            val leftGrandChildren = parseChildren(leftGrandChildrenAsString)
            val leftChild = Node(leftChildValue, leftGrandChildren._1, leftGrandChildren._2)

            val leftChildRightChildCommaSeparatorIdx = childrenAsString.indexOf(",", leftChildCloseParenIdx)
            foundParenthesises = 0
            unbalancedParenthesises = 0

            val rightChildOpenParenIdx = childrenAsString.indexOf("(", leftChildCloseParenIdx)

            if (rightChildOpenParenIdx == -1) { //Right child has no grand-children
                val rightChildValue = childrenAsString.substring(leftChildRightChildCommaSeparatorIdx + 1)
                val rightChild = Node(rightChildValue)
                return (leftChild, rightChild)
            } else { //Right child has grand-children
                val rightChildCloseParenIdx = childrenAsString.substring(leftChildRightChildCommaSeparatorIdx).toCharArray().zipWithIndex.takeWhile(charSelector).map(_._2).last + 1 + leftChildRightChildCommaSeparatorIdx
                val rightChildValue = childrenAsString.substring(leftChildRightChildCommaSeparatorIdx + 1, rightChildOpenParenIdx)
                val rightGrandChildrenAsString = childrenAsString.substring(rightChildOpenParenIdx + 1, rightChildCloseParenIdx)
                val rightGrandChildren = parseChildren(rightGrandChildrenAsString)
                val rightChild = Node(rightChildValue, rightGrandChildren._1, rightGrandChildren._2)
                return (leftChild, rightChild)
            }
        }
    }
}
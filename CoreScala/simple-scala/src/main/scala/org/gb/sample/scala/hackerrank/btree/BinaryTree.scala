package org.gb.sample.scala.hackerrank.btree

import scala.math.max

class Node[T](val value: T, val leftChild: Node[T], val rightChild: Node[T]) {
    
    override def toString(): String = {
        String.valueOf(value)
    }
}

case class BinaryTree[T](private val root: Node[T]) {

    private def getHeight(node: Node[T]): Int = {
        if (node == null) {
            return -1
        } else {
            val subtree = BinaryTree(node)
            return subtree.getHeight()
        }
    }

    def getHeight(): Int = {
        val leftSubtreeHeight = getHeight(root.leftChild)
        val rightSubtreeHeight = getHeight(root.rightChild)

        val maxHeightOfChildren = max(leftSubtreeHeight, rightSubtreeHeight)
        return maxHeightOfChildren + 1
    }

    def getRoot(): Node[T] = {
        return root
    }

    def getLeftSubtree(): BinaryTree[T] = {
        if (root.leftChild == null) {
            null
        } else {
            BinaryTree(root.leftChild)
        }
    }

    def getRightSubtree(): BinaryTree[T] = {
        if (root.rightChild == null) {
            null
        } else {
            BinaryTree(root.rightChild)
        }
    }

    override def toString(): String = {
        "root: ".concat(String.valueOf(root.value))
    }

}
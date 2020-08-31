package org.gb.sample.scala.hackerrank.btree.bst

import org.gb.sample.scala.hackerrank.btree.BinaryTree

object Solution {
    
    def whatisthis(): Unit = {
        println("what a worthless method!")
    }

    def isBST(btree: BinaryTree[Int]): Boolean = {
        val leftSubtreeBST = isLeftSubtreeBST(btree.getLeftSubtree(), -1, btree.getRoot().value)

        if (!leftSubtreeBST) {
            return false
        }

        val rightSubtreeBST = isRightSubtreeBST(btree.getRightSubtree(), btree.getRoot().value, -1)

        if (!rightSubtreeBST) {
            return false
        }

        return true
    }

    private def areChildSubtreesBST(btree: BinaryTree[Int], leftSubtreeLwLmt: Int, leftSubtreeUpLmt: Int, rightSubtreeLwLmt: Int, rightSubtreeUpLmt: Int): Boolean = {
        val leftSubtreeBST = isLeftSubtreeBST(btree.getLeftSubtree(), leftSubtreeLwLmt, leftSubtreeUpLmt)

        if (!leftSubtreeBST) {
            return false
        }

        val rightSubtreeBST = isRightSubtreeBST(btree.getRightSubtree(), rightSubtreeLwLmt, rightSubtreeUpLmt)

        if (!rightSubtreeBST) {
            return false
        }

        return true
    }

    private def isLeftSubtreeBST(btree: BinaryTree[Int], lowerLimit: Int, upperLimit: Int): Boolean = {
        if (btree == null) {
            return true
        }

        val currentValue = btree.getRoot().value

        if (lowerLimit != -1) {
            if (currentValue < lowerLimit) {
                return false
            }
        }

        if (currentValue > upperLimit) {
            return false
        }

        val leftSubtreeLwLmt = lowerLimit
        val leftSubtreeUpLmt = currentValue
        val rightSubtreeLwLmt = currentValue
        val rightSubtreeUpLmt = upperLimit

        return areChildSubtreesBST(btree, leftSubtreeLwLmt, leftSubtreeUpLmt, rightSubtreeLwLmt, rightSubtreeUpLmt)
    }

    private def isRightSubtreeBST(btree: BinaryTree[Int], lowerLimit: Int, upperLimit: Int): Boolean = {
        if (btree == null) {
            return true
        }

        val currentValue = btree.getRoot().value

        if (currentValue < lowerLimit) {
            return false
        }

        if (upperLimit != -1) {
            if (currentValue > upperLimit) {
                return false
            }
        }

        val leftSubtreeLwLmt = lowerLimit
        val leftSubtreeUpLmt = currentValue
        val rightSubtreeLwLmt = currentValue
        val rightSubtreeUpLmt = upperLimit

        return areChildSubtreesBST(btree, leftSubtreeLwLmt, leftSubtreeUpLmt, rightSubtreeLwLmt, rightSubtreeUpLmt)
    }
}
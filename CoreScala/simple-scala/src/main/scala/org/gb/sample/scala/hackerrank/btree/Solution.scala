package org.gb.sample.scala.hackerrank.btree

object Solution {

    def getHeight[T](btree: BinaryTree[T]): Int = {
        return btree.getHeight()
    }

    case class Status[T](targetNodesCount: Int, commonAncestor: Node[T] = null) {

    }

    def findLCA[T](btree: BinaryTree[T], v1: T, v2: T): Node[T] = {
        val status = findTargetNodes(btree, v1, v2)
        return status.commonAncestor
    }

    private def findTargetNodes[T](btree: BinaryTree[T], v1: T, v2: T): Status[T] = {
        if (btree == null) {
            return Status(0)
        }

        val leftSubtree = btree.getLeftSubtree()
        val leftSubtreeStatus = findTargetNodes(leftSubtree, v1, v2)

        if (leftSubtreeStatus.targetNodesCount == 2) {
            return leftSubtreeStatus
        }

        val rightSubtree = btree.getRightSubtree()
        val rightSubtreeStatus = findTargetNodes(rightSubtree, v1, v2)

        if (rightSubtreeStatus.targetNodesCount == 2) {
            return rightSubtreeStatus
        }

        val currentRoot = btree.getRoot()
        val nodeCountIfCurrentRootIsATarget =
            if (currentRoot.value == v1 || currentRoot.value == v2) {
                1
            } else {
                0
            }

        val combinedTgtNodesCount = leftSubtreeStatus.targetNodesCount + rightSubtreeStatus.targetNodesCount + nodeCountIfCurrentRootIsATarget

        if (combinedTgtNodesCount == 2) {
            //Both target nodes are found, hence currentRoot is the LCA.
            return Status(2, currentRoot)
        } else {
            //Not all target nodes are found, yet to find the LCA.
            return Status(combinedTgtNodesCount)
        }
    }
}
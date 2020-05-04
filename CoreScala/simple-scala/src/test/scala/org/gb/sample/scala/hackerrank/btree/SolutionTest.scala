package org.gb.sample.scala.hackerrank.btree

import org.scalatest.FunSuite

class SolutionTest extends FunSuite {

    private val binaryTree = buildBinaryTree()

    private def buildBinaryTree(): BinaryTree[Int] = {
        val node19 = new Node(19, null, null)
        val node20 = new Node(20, null, null)
        val node15 = new Node(15, node19, null)
        val node16 = new Node(16, node20, null)
        val node17 = new Node(17, null, null)
        val node18 = new Node(18, null, null)
        val node7 = new Node(7, null, null)
        val node8 = new Node(8, null, null)
        val node9 = new Node(9, null, null)
        val node10 = new Node(10, null, null)
        val node11 = new Node(11, node15, node16)
        val node12 = new Node(12, node17, node18)
        val node13 = new Node(13, null, null)
        val node14 = new Node(14, null, null)
        val node3 = new Node(3, node7, node8)
        val node4 = new Node(4, node9, node10)
        val node5 = new Node(5, node11, node12)
        val node6 = new Node(6, node13, node14)
        val node1 = new Node(1, node3, node4)
        val node2 = new Node(2, node5, node6)
        val node0 = new Node(0, node1, node2)

        val binaryTree = BinaryTree(node0)
        return binaryTree
    }

    test("height of a binary tree") {
        val height = Solution.getHeight(binaryTree)

        assertResult(5)(height)
    }

    test("lowest common ancestor - 1") {
        val lca = Solution.findLCA(binaryTree, 15, 13)

        assertResult(2)(lca.value)
    }

    test("lowest common ancestor - 2") {
        val lca = Solution.findLCA(binaryTree, 19, 20)

        assertResult(11)(lca.value)
    }

    test("lowest common ancestor - 3") {
        val lca = Solution.findLCA(binaryTree, 8, 6)

        assertResult(0)(lca.value)
    }

}
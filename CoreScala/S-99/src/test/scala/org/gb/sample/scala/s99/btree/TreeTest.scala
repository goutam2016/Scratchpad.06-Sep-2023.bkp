package org.gb.sample.scala.s99.btree

import org.scalatest.FunSuite

class TreeTest extends FunSuite {
    def buildTree(): Tree[String] = {
        val level3_1 = Node("3.1")
        val level3_2 = Node("3.2")
        val level3_3 = Node("3.3")
        val level3_4 = Node("3.4")
        val level3_5 = Node("3.5")
        val level3_6 = Node("3.6")
        val level3_7 = Node("3.7")
        val level3_8 = Node("3.8")
        val level2_1 = Node("2.1", level3_1, level3_2)
        val level2_2 = Node("2.2", level3_3, level3_4)
        val level2_3 = Node("2.3", level3_5, level3_6)
        val level2_4 = Node("2.4", level3_7, level3_8)
        val level1_1 = Node("1.1", level2_1, level2_2)
        val level1_2 = Node("1.2", level2_3, level2_4)
        val root = Node("0", level1_1, level1_2)
        root
    }

    test("P61: count leaves of a tree") {
        val root = buildTree()
        val leafCount = root.p61_leafCount
        assertResult(8)(leafCount)
    }

    test("P61A: collect values from leaves into a list") {
        val root = buildTree()
        val leafValues = root.p61A_collectLeafValues
        assertResult(List("3.1", "3.2", "3.3", "3.4", "3.5", "3.6", "3.7", "3.8"))(leafValues)
    }

    test("P56: check if symmetric tree") {
        val root = Node("a", Node("b"), Node("b"))
        val isSymmetric = root.p56_isSymmetric
        assertResult(true)(isSymmetric)
    }
}
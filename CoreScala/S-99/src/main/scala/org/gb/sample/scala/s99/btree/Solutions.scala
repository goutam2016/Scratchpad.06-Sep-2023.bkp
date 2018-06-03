package org.gb.sample.scala.s99.btree

object Solutions {
    def main(args: Array[String]): Unit = {
        val treeRoot = Tree.p55_balanced(19, "a")
        println(treeRoot)
        println(treeRoot.p61_leafCount)
    }
}
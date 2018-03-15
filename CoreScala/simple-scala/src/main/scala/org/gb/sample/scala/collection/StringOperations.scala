package org.gb.sample.scala.collection

import scala.collection.immutable.IndexedSeq
import scala.Enumeration
import scala.collection.immutable.Map

object StringOperations {

    object SortOrder extends Enumeration {
        type SortOrder = Value
        val ASCENDING, DESCENDING = Value
    }

    def capitalizeAllItems(items: IndexedSeq[String]): IndexedSeq[String] = {
        items.map(_.toUpperCase)
    }

    def findMaxLengthItem(items: IndexedSeq[String]): String = {
        items.maxBy(_.length)
    }

    def sort(items: IndexedSeq[String], order: SortOrder.Value): IndexedSeq[String] = {
        items.sortWith((item1, item2) => order match {
            case SortOrder.ASCENDING  => item1 > item2
            case SortOrder.DESCENDING => item2 > item1
        })
    }

    def groupByItemLength(items: IndexedSeq[String]): Map[Int, IndexedSeq[String]] = {
        items.groupBy(_.length())
    }

    def groupCountByWord(words: IndexedSeq[String]): Map[String, Int] = {
        words.groupBy(word => word).mapValues(_.size)
    }

    def concatenateWithSeparator(items: IndexedSeq[String]): String = {
        items.reduce((itemsConcatenated, nextItem) => itemsConcatenated.concat(",").concat(nextItem))
    }
    
    def getTotalLengthOfAllItems(items: IndexedSeq[String]): Int = {
        items.map(_.length()).sum
    }
}
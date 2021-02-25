package org.gb.sample.scala.collection

import scala.collection.immutable.IndexedSeq
import org.scalatest.funsuite.AnyFunSuite
import scala.collection.immutable.Map

class StringOperationsTest extends AnyFunSuite {
    test("all items are capitalized") {
        val names = IndexedSeq("Gulai", "ppo", "durBa", "Mumun", "SwapanKaku")
        val expectedNames = IndexedSeq("GULAI", "PPO", "DURBA", "MUMUN", "SWAPANKAKU");
        val capitalizedNames = StringOperations.capitalizeAllItems(names)
        assert(capitalizedNames == expectedNames)
    }

    test("the longest item is returned") {
        val names = IndexedSeq("Gulai", "ppo", "durBa", "Mumun", "SwapanKaku")
        val expectedName = "SwapanKaku"
        val longestName = StringOperations.findMaxLengthItem(names)
        assert(longestName == expectedName)
    }

    test("items are sorted") {
        val names = IndexedSeq("Gulai", "ppo", "durBa", "Mumun", "SwapanKaku")
        val expectedNames = IndexedSeq("Gulai", "Mumun", "SwapanKaku", "durBa", "ppo")
        val sortedNames = StringOperations.sort(names, StringOperations.SortOrder.DESCENDING)
        assert(sortedNames == expectedNames)
    }

    test("items are grouped by length") {
        val names = IndexedSeq("Gulai", "ppo", "durBa", "Mumun", "SwapanKaku")
        val expectedNamesByLength = Map(3 -> IndexedSeq("ppo"), 5 -> IndexedSeq("Gulai", "durBa", "Mumun"), 10 -> IndexedSeq("SwapanKaku"))
        val namesByLength = StringOperations.groupByItemLength(names)
        assert(namesByLength == expectedNamesByLength)
    }

    test("count per word") {
        val names = IndexedSeq("John", "Paul", "George", "John", "Paul", "John");
        val expectedCountPerWord = Map("John" -> 3, "Paul" -> 2, "George" -> 1)
        val countPerWord = StringOperations.groupCountByWord(names)
        assert(countPerWord == expectedCountPerWord)
    }
    
    test("items are concatenated with a separator") {
        val names = IndexedSeq("Gulai", "ppo", "durBa", "Mumun", "SwapanKaku")
        val expectedNamesConcatenated = "Gulai,ppo,durBa,Mumun,SwapanKaku"
        val namesConcatenated = StringOperations.concatenateWithSeparator(names)
        assert(namesConcatenated == expectedNamesConcatenated)
    }
    
    test("total length of all items") {
        val names = IndexedSeq("Gulai", "ppo", "durBa", "Mumun", "SwapanKaku")
        val expectedTotalLength = 28
        val totalLength = StringOperations.getTotalLengthOfAllItems(names)
        assert(totalLength == expectedTotalLength)
    }
}

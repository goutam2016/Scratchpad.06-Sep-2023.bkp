package org.gb.sample.scala.hackerrank.maxarraysum

import scala.collection.mutable.Map
import scala.annotation.tailrec

object Solution {
    def maxSubsetSum(numbers: Array[Int]): Int = {
        val nonAdjMaxSumPerIndex = Map.empty[Int, Int]
        val nonAdjMaxSumVsIndexArr = numbers.zipWithIndex.reverse.map(numVsIdx => (numVsIdx._2, findNonAdjMaxSum(numVsIdx._2, numbers.size - 1, numbers, nonAdjMaxSumPerIndex)))
        return nonAdjMaxSumVsIndexArr.map(_._2).max
    }

    private def findNonAdjMaxSum(currIdx: Int, maxIdx: Int, numbers: Array[Int], nonAdjMaxSumPerIndex: Map[Int, Int]): Int = {
        val number = numbers(currIdx)
        var nonAdjMaxSum = 0

        if (currIdx > maxIdx - 2) {
            nonAdjMaxSum = number
        } else {
            nonAdjMaxSum = nonAdjMaxSumPerIndex.filter(_._1 > currIdx + 1).map(_._2).max

            nonAdjMaxSum =
                if (number < 0 || nonAdjMaxSum < 0) {
                    Integer.max(number, nonAdjMaxSum)
                } else {
                    number + nonAdjMaxSum
                }
        }

        nonAdjMaxSumPerIndex.put(currIdx, nonAdjMaxSum)

        if (nonAdjMaxSumPerIndex.size > 3) {
            nonAdjMaxSumPerIndex.remove(currIdx + 3)
        }

        return nonAdjMaxSum
    }
}
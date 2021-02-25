package org.gb.sample.scala.hackerrank.candies

import org.scalatest.funsuite.AnyFunSuite

class SolutionTest extends AnyFunSuite {

    test("calculate minimum candies - 1") {
        val ratings = Array[Int](2, 4, 2, 6, 1, 7, 8, 9, 2, 1, 3, 2)
        val minTotalCandies = Solution.distributeCandies(ratings)
        assertResult(22)(minTotalCandies)
    }

    test("calculate minimum candies - 2") {
        val ratings = Array[Int](4, 5, 6, 1, 7, 8, 9, 2, 1, 3, 4)
        val minTotalCandies = Solution.distributeCandies(ratings)
        assertResult(24)(minTotalCandies)
    }

    test("calculate minimum candies - 3") {
        val ratings = Array[Int](3, 4, 8, 7, 6, 1, 6, 7, 8, 3, 2, 1)
        val minTotalCandies = Solution.distributeCandies(ratings)
        assertResult(28)(minTotalCandies)
    }
    
    test("calculate minimum candies - 4") {
        val ratings = Range(1, 100001).toArray
        val minTotalCandies = Solution.distributeCandies(ratings)
        assertResult(5000050000L)(minTotalCandies)
    }
    
}
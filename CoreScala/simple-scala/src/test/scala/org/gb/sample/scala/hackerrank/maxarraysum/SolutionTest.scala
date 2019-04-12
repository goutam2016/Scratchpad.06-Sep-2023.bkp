package org.gb.sample.scala.hackerrank.maxarraysum

import org.scalatest.FunSuite

class SolutionTest extends FunSuite {

    test("maximum sum of non adjacent numbers - 1") {
        val numbers = Array[Int](3, 5, -7, 8, 10)
        val nonAdjMaxSum = Solution.maxSubsetSum(numbers)
        assertResult(15)(nonAdjMaxSum)
    }

    test("maximum sum of non adjacent numbers - 2") {
        val numbers = Array[Int](0, 1, -2, 3, 4, 6, 5)
        val nonAdjMaxSum = Solution.maxSubsetSum(numbers)
        assertResult(10)(nonAdjMaxSum)
    }

    test("maximum sum of non adjacent numbers - 3") {
        val numbers = Range(0, 50000).toArray
        val nonAdjMaxSum = Solution.maxSubsetSum(numbers)
        assertResult(625000000)(nonAdjMaxSum)
    }
}

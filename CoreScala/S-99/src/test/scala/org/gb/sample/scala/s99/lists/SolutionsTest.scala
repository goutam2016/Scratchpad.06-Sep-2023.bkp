package org.gb.sample.scala.s99.lists

import org.scalatest.FunSuite

class SolutionsTest extends FunSuite {
    test("P06: Check if list is palindrome") {
        val isPalindromeOddList = Solutions.p06_isPalindrome(List(1, 2, 3, 2, 1))
        val isPalindromeEvenList = Solutions.p06_isPalindrome(List(1, 2, 3, 4))
        assertResult(true)(isPalindromeOddList)
        assertResult(false)(isPalindromeEvenList)
    }

    test("P07: flatten nested lists into a single list") {
        val listOfLists = List(List('a', 'b'), List('c', 'c'), List('d', 'e'))
        val fltndList = Solutions.p07_flatten(listOfLists)
        println(fltndList)
    }

    test("P08: remove consecutive duplicate elements from the list - iterative style") {
        val cmprsdList = Solutions.p08_compress_iterative(List('a', 'b', 'b', 'c', 'd', 'd', 'd', 'd'))
        assertResult(List('a', 'b', 'c', 'd'))(cmprsdList)
    }

    test("P08: remove consecutive duplicate elements from the list - functional style") {
        val cmprsdList = Solutions.p08_compress_functional(List('a', 'b', 'b', 'c', 'd', 'd', 'd', 'd'))
        assertResult(List('a', 'b', 'c', 'd'))(cmprsdList)
    }

    test("P10: encode consecutive duplicates of elements as tuples (Count, Element)") {
        val encdList = Solutions.p10_encode(List('a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e'))
        assertResult(List((4, 'a'), (1, 'b'), (2, 'c'), (2, 'a'), (1, 'd'), (4, 'e')))(encdList)
    }

    test("P12: decode list of tuples (Count, Element) into a list of individual elemts") {
        val decdList = Solutions.p12_decode(List((1, 'a'), (2, 'b'), (3, 'c'), (2, 'd'), (1, 'e')))
        assertResult(List('a', 'b', 'b', 'c', 'c', 'c', 'd', 'd', 'e'))(decdList)
    }

    test("P16: drop elements at a fixed interval from a list") {
        val elemDroppedList = Solutions.p16_dropAtIntervals(4, List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'))
        assertResult(List('a', 'b', 'c', 'e', 'f', 'g', 'i', 'j'))(elemDroppedList)
    }

    test("P19: rotate a list N places to the left") {
        val leftRotatedList = Solutions.p19_rotateLeft(3, List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'))
        val rightRotatedList = Solutions.p19_rotateLeft(-3, List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'))
        assertResult(List('d', 'e', 'f', 'g', 'h', 'a', 'b', 'c'))(leftRotatedList)
        assertResult(List('f', 'g', 'h', 'a', 'b', 'c', 'd', 'e'))(rightRotatedList)
    }
}
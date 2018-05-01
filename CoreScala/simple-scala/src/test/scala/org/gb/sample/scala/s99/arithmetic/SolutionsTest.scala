package org.gb.sample.scala.s99.arithmetic

import org.scalatest.FunSuite

class SolutionsTest extends FunSuite {

    test("P31: is it a prime number? - functional style") {
        val isPrime43 = Solutions.p31_isPrime_functional(43)
        val isPrime45 = Solutions.p31_isPrime_functional(45)
        assertResult(true)(isPrime43)
        assertResult(false)(isPrime45)
    }
    
    test("P32: greatest common divisor of 2 positive integers") {
        val gcd = Solutions.p32_gcd(45, 195)
        assertResult(15)(gcd)
    }
    
    test("P39: list of prime numbers within a given range - iterative style") {
        val primesInRange = Solutions.p39_listPrimesInRange_iterative(20 to 100)
        assertResult(List(23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97))(primesInRange)
    }

    test("P39: list of prime numbers within a given range - functional style") {
        val primesInRange = Solutions.p39_listPrimesInRange_functional(20 to 100)
        assertResult(List(23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97))(primesInRange)
    }
}
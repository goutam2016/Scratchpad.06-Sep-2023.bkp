package org.gb.sample.scala.s99.arithmetic

import scala.math.sqrt
import scala.math.max
import scala.math.min

object Solutions {

    def p31_isPrime(number: Int): Boolean = {
        if (number == 1 || number == 2) {
            true
        }

        var divisor = 2
        var quotient = number

        while (quotient > divisor) {
            quotient = number / divisor
            val remainder = number % divisor

            if (remainder == 0) {
                return false
            }

            divisor += 1
        }

        true
    }

    def p31_isPrime_functional(number: Int): Boolean = {
        val sqRoot = sqrt(number).toInt
        val zeroRemainders = List.iterate(2, sqRoot)(_ + 1).map(number % _).filter(_ == 0)
        zeroRemainders.isEmpty
    }

    def p32_gcd(number1: Int, number2: Int): Int = {
        val larger = max(number1, number2)
        val smaller = min(number1, number2)
        tryDivision(larger, smaller, smaller)
    }

    private def tryDivision(larger: Int, smaller: Int, divisor: Int): Int = {
        if (larger % divisor == 0 && smaller % divisor == 0) {
            divisor
        } else {
            tryDivision(larger, smaller, divisor - 1)
        }
    }

    def p33_areCoprime(number1: Int, number2: Int): Boolean = {
        p32_gcd(number1, number2) == 1
    }

    def p39_listPrimesInRange_iterative(range: Range): List[Int] = {
        var primesInRange = List[Int]()
        for (number <- range.start until (range.end)) {
            if (p31_isPrime_functional(number)) {
                primesInRange = primesInRange.:+(number)
            }
        }
        primesInRange
    }

    def p39_listPrimesInRange_functional(range: Range): List[Int] = {
        val primePicker = (primesInRange: List[Int], nextNumber: Int) => {
            if (p31_isPrime_functional(nextNumber)) {
                primesInRange.:+(nextNumber)
            } else {
                primesInRange
            }
        }
        List.range(range.start, range.end).foldLeft(List[Int]())(primePicker)
    }

    def p40_goldbach(number: Int): (Int, Int) = {
        for (part <- number / 2 until 0 by -1) {
            if (p31_isPrime_functional(part) && p31_isPrime_functional(number - part)) {
                return (part, number - part)
            }
        }
        null
    }

    def add(a: Int)(b: Int) = {
        a + b
    }

    def sum(a: Int, b: Int) = {
        a + b
    }

    def main(args: Array[String]): Unit = {
        val x = add(1)(_)
        println(x(2))
        println(x(3))
        println(x(4))
        val y = sum(1, _:Int)
        println(y(2))
        println(y(3))
        println(y(4))        
    }
}
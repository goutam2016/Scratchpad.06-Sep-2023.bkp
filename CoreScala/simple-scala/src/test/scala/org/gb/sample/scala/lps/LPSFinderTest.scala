package org.gb.sample.scala.lps

import org.scalatest.funsuite.AnyFunSuite

class LPSFinderTest extends AnyFunSuite {

    test("longest palindrome sequence - 1") {
        val lps = LPSFinder.findLPS("racecar")
        assertResult("racecar")(String.valueOf(lps))
    }

    test("longest palindrome sequence - 2") {
        val lps = LPSFinder.findLPS("character")
        assertResult("carac")(String.valueOf(lps))
    }

    test("longest palindrome sequence - 3") {
        val lps = LPSFinder.findLPS("aibohphobia")
        assertResult("aibohphobia")(String.valueOf(lps))
    }

    test("longest palindrome sequence - 4") {
        val lps = LPSFinder.findLPS("axyywyyzyyxb")
        assertResult("xyyyyyyx")(String.valueOf(lps))
    }
}
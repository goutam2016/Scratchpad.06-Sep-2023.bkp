package org.gb.sample.scala.lps

import scala.collection.SortedSet

object Main {
    def main(args: Array[String]): Unit = {
        val longestPdm = findLPS("character")
        println(longestPdm)
    }

    def findLPS(origStr: String): String = {
        println(s"searching LPS in string: ${origStr}")
        var letters = Seq[Char]()
        val pdmLengthCmpr = Ordering.by[PalindromeOld, Int](_.letters.length()).reverse
        var discvdPdms = SortedSet[PalindromeOld]()(pdmLengthCmpr)

        for (currIdx <- 0 to origStr.size - 1) {
            val letter = origStr.charAt(currIdx);
            println(s"reached letter: ${letter}, currIdx: ${currIdx}")
            if (letters.contains(letter)) {
                val idxOf1stInstance = letters.indexOf(letter);
                println(s"found an eariler occurence for this letter at index: ${idxOf1stInstance}")
                val lpsBtwn1stAndCurrIdx = findLPS(origStr, idxOf1stInstance, currIdx, discvdPdms)
                val extdLPS = letter + lpsBtwn1stAndCurrIdx + letter
                println(s"new palindrome sequence, letter: ${letter}, idxOf1stInstance: ${idxOf1stInstance}, currIdx: ${currIdx}, lps: ${extdLPS}")
                val pdm = new PalindromeOld(extdLPS, idxOf1stInstance, currIdx)
                discvdPdms = discvdPdms + (pdm)
            }
            letters = letters :+ letter
            println("---------------------------------------------------------------------------------------------------------------------")
        }

        if (discvdPdms.isEmpty) {
            ""
        } else {
            discvdPdms.head.letters;
        }

    }

    def findLPS(origStr: String, startIdx: Int, endIdx: Int, discvdPdms: SortedSet[PalindromeOld]): String = {
        var lps = ""
        if (endIdx - startIdx <= 2) {
            lps = origStr.substring(startIdx + 1, endIdx)
        } else {
            val lpsBtwnStartAndEndIdx = discvdPdms.filter(p => p.startIdx > startIdx).filter(p => p.endIdx < endIdx)
            if (lpsBtwnStartAndEndIdx.isEmpty) {
                val strBtwnStartAndEndIdx = origStr.substring(startIdx + 1, endIdx)
                lps = findLPS(strBtwnStartAndEndIdx)
            } else {
                lps = lpsBtwnStartAndEndIdx.head.letters
            }
        }

        //println(s"startIdx: ${startIdx}, endIdx: ${endIdx}, lps: ${lps}")
        return lps
    }

}

class PalindromeOld(val letters: String, val startIdx: Int, val endIdx: Int) {
    
}
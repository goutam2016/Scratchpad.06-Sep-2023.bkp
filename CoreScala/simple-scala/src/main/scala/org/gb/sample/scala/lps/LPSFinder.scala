package org.gb.sample.scala.lps

import scala.collection.SortedSet

object LPSFinder {
    
    def findLPS(text: String): Array[Char] = {
        var letters = Seq[Char]()
        val pdmLengthCmpr = Ordering.by[Palindrome, Int](_.letters.length).reverse
        var discvdPdms = SortedSet[Palindrome]()(pdmLengthCmpr)

        for (currIdx <- 0 to text.size - 1) {
            val letter = text.charAt(currIdx);

            if (letters.contains(letter)) {
                val idxOf1stInstance = letters.indexOf(letter)
                val lpsBtwn1stAndCurrIdx = findLPS(text, idxOf1stInstance, currIdx, discvdPdms)
                val extdLPS = lpsBtwn1stAndCurrIdx.+:(letter).:+(letter)
                val pdm = new Palindrome(extdLPS, idxOf1stInstance, currIdx)
                discvdPdms = discvdPdms + (pdm)
            }
            letters = letters :+ letter
        }

        if (discvdPdms.isEmpty) {
            Array.emptyCharArray
        } else {
            discvdPdms.head.letters;
        }
    }

    private def findLPS(text: String, startIdx: Int, endIdx: Int, discvdPdms: SortedSet[Palindrome]): Array[Char] = {
        var lps = Array[Char]()
        if (endIdx - startIdx <= 2) {
            lps = text.substring(startIdx + 1, endIdx).toCharArray()
        } else {
            val lpsBtwnStartAndEndIdx = discvdPdms.filter(p => p.startIdx > startIdx).filter(p => p.endIdx < endIdx)
            if (lpsBtwnStartAndEndIdx.isEmpty) {
                val subText = text.substring(startIdx + 1, endIdx)
                lps = findLPS(subText)
            } else {
                lps = lpsBtwnStartAndEndIdx.head.letters
            }
        }

        return lps
    }

}
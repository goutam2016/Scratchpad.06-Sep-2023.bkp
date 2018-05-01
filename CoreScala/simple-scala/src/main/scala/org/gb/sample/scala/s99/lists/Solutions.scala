package org.gb.sample.scala.s99.lists

import scala.collection.mutable.IndexedSeq
import scala.collection.mutable.MutableList

object Solutions {

    def p06_isPalindrome(list: List[Int]): Boolean = {
        val revsdList = list.reverse
        list.equals(revsdList)
    }

    def p07_flatten(list: List[Any]): List[Any] = {
        list.flatMap(elem => elem match {
            case subList: List[_] => p07_flatten(subList)
            case singleVal        => List(singleVal)
        })
    }

    def p08_compress_iterative(list: List[Char]): List[Char] = {
        var cmprsdSeq = IndexedSeq[Char]()

        for (charIdx <- 0 until (list.size)) {
            val ch = list.apply(charIdx)

            if (cmprsdSeq.isEmpty) {
                cmprsdSeq = cmprsdSeq.:+(ch)
            } else {
                val lastChar = cmprsdSeq.last

                if (ch != lastChar) {
                    cmprsdSeq = cmprsdSeq.:+(ch)
                }
            }
        }

        cmprsdSeq.toList
    }

    def p08_compress_functional(list: List[Char]): List[Char] = {
        val consecutiveDuplRemover = (cmprsdList: List[Char], nextChar: Char) => {
            if (cmprsdList.isEmpty) {
                cmprsdList.:+(nextChar)
            } else if (cmprsdList.last == nextChar) {
                cmprsdList
            } else {
                cmprsdList.:+(nextChar)
            }
        }
        list.foldLeft(List[Char]())(consecutiveDuplRemover)
    }

    def p10_encode(list: List[Char]): List[(Int, Char)] = {
        val consecutiveDuplEncoder = (encdList: List[(Int, Char)], nextChar: Char) => {
            if (encdList.isEmpty) {
                encdList.:+((1, nextChar))
            } else if (encdList.last._2 == nextChar) {
                val consDuplCount = encdList.last._1 + 1
                val updEncdList = encdList.take(encdList.size - 1)
                updEncdList.:+((consDuplCount, nextChar))
            } else {
                encdList.:+((1, nextChar))
            }
        }
        list.foldLeft(List[(Int, Char)]())(consecutiveDuplEncoder)
    }

    def p12_decode(countVsElemPairs: List[(Int, Char)]): List[Char] = {
        val elemDecoder = (countVsElemPair: (Int, Char)) => {
            List.fill(countVsElemPair._1)(countVsElemPair._2)
        }
        countVsElemPairs.flatMap(elemDecoder)
    }

    def p14_duplicate(list: List[Char]): List[Char] = {
        val duplicator = (duplList: List[Char], nextChar: Char) => {
            duplList.:+(nextChar).:+(nextChar)
        }
        list.foldLeft(List[Char]())(duplicator)
    }

    def p16_dropAtIntervals(intervalLength: Int, list: List[Char]): List[Char] = {
        val elementFilterer = (charVsIdx: (Char, Int)) => {
            (charVsIdx._2 + 1) % intervalLength != 0
        }
        list.zipWithIndex.filter(elementFilterer).map(_._1)
    }

    def p19_rotateLeft(rotateBy: Int, elements: List[Char]): List[Char] = {
        if (rotateBy >= 0) {
            val leftSubList = elements.take(rotateBy)
            val rightSubList = elements.drop(rotateBy)
            rightSubList.++(leftSubList)
        } else {
            p19_rotateLeft(elements.size + rotateBy, elements)
        }
    }

    def main(args: Array[String]): Unit = {
        val elements = List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
        val rotatedList = Solutions.p19_rotateLeft(2, elements)
        println(rotatedList)
    }
}
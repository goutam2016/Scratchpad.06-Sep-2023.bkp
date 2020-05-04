package org.gb.sample.scala.hackerrank.decibinary

import scala.math.pow
import scala.collection.mutable.Map

object Solution5 {

    private def overlappedDecibinaryNumbers(currentDecimalNumber: Int, prevDeciBinNumbers: Seq[Int], cachedDecVsDeciBinNumbers: Map[Int, Seq[Int]]): Seq[Int] = {
        if (currentDecimalNumber == 0) {
            return Seq(0)
        } else if (currentDecimalNumber % 2 == 1) {
            val ovrlpdDeciBinNumbers = prevDeciBinNumbers.map(_ + 1)
            cachedDecVsDeciBinNumbers.put(currentDecimalNumber, ovrlpdDeciBinNumbers)
            return ovrlpdDeciBinNumbers
        } else {
            val incrementedDeciBinNumbers = prevDeciBinNumbers.filterNot(_ % 10 == 9).map(_ + 1)
            val deciBinsForHalfOfCurrDecNumber = cachedDecVsDeciBinNumbers.remove(currentDecimalNumber / 2).get
            val leftShiftedDeciBinNumbers = deciBinsForHalfOfCurrDecNumber.map(_ * 10)
            val ovrlpdDeciBinNumbers = leftShiftedDeciBinNumbers ++ incrementedDeciBinNumbers
            cachedDecVsDeciBinNumbers.put(currentDecimalNumber, ovrlpdDeciBinNumbers)
            return ovrlpdDeciBinNumbers
        }
    }

    def computeDecibinaryNumber(index: Long): Long = {
        var currentDecimalNumber = 0
        var totalDeciBinNumCount = 0
        var ovrlpdDeciBinNumbers = Seq.empty[Int]
        val cachedDecVsDeciBinNumbers = Map.empty[Int, Seq[Int]]

        while (totalDeciBinNumCount < index) {
            ovrlpdDeciBinNumbers = overlappedDecibinaryNumbers(currentDecimalNumber, ovrlpdDeciBinNumbers, cachedDecVsDeciBinNumbers)
            totalDeciBinNumCount += ovrlpdDeciBinNumbers.size
            //println(s"currentDecimalNumber: ${currentDecimalNumber}, ovrlpdDeciBinNumbers.size: ${ovrlpdDeciBinNumbers.size}, totalDeciBinNumCount: ${totalDeciBinNumCount}")
            //println(s"currentDecimalNumber: ${currentDecimalNumber}, ovrlpdDeciBinNumbers.size: ${ovrlpdDeciBinNumbers.size}, ovrlpdDeciBinNumbers: ${ovrlpdDeciBinNumbers}, totalDeciBinNumCount: ${totalDeciBinNumCount}")
            currentDecimalNumber += 1
        }

        val reqdDecimalValue = currentDecimalNumber - 1
        //return reqdDecimalValue
        val reqdDeciBinNumberOffset = if (totalDeciBinNumCount == index) ovrlpdDeciBinNumbers.size else ovrlpdDeciBinNumbers.size - (totalDeciBinNumCount - index)
        val srtdOvrlpdDeciBinNumbers = ovrlpdDeciBinNumbers.sorted
        //println(s"reqdDeciBinNumberOffset: ${reqdDeciBinNumberOffset}, srtdOvrlpdDeciBinNumbers: ${srtdOvrlpdDeciBinNumbers}")
        println(s"reqdDecimalValue: ${reqdDecimalValue}, reqdDeciBinNumberOffset: ${reqdDeciBinNumberOffset}, "
            + s"srtdOvrlpdDeciBinNumbers.size: ${srtdOvrlpdDeciBinNumbers.size}, "
            + s"srtdOvrlpdDeciBinNumbers: ${srtdOvrlpdDeciBinNumbers}")

        val deciBinNumberAtIdx = srtdOvrlpdDeciBinNumbers(reqdDeciBinNumberOffset.intValue() - 1)
        return deciBinNumberAtIdx
    }

    def main(args: Array[String]): Unit = {
        /*var ovrlpdDeciBinNumberCount = 0
        var totalDeciBinNumCount = 0
        val cachedDecVsDeciBinNumberCount = Map.empty[Int, Int]
        for(decimalNumber <- 0 to 11) {
            ovrlpdDeciBinNumberCount = overlappedDecibinaryNumberCount(decimalNumber, ovrlpdDeciBinNumberCount, cachedDecVsDeciBinNumberCount)
            totalDeciBinNumCount += ovrlpdDeciBinNumberCount
            println(s"currentDecimalNumber: ${decimalNumber}, ovrlpdDeciBinNumberCount: ${ovrlpdDeciBinNumberCount}, totalDeciBinNumCount: ${totalDeciBinNumCount}")
        }*/
        /*var ovrlpdDeciBinNumbers = Seq.empty[Int]
        var totalDeciBinNumCount = 0
        val cachedDecVsDeciBinNumbers = Map.empty[Int, Seq[Int]]
        for(decimalNumber <- 0 to 10) {
            ovrlpdDeciBinNumbers = overlappedDecibinaryNumbers(decimalNumber, ovrlpdDeciBinNumbers, cachedDecVsDeciBinNumbers)
            totalDeciBinNumCount += ovrlpdDeciBinNumbers.size
            println(s"decimalNumber: ${decimalNumber}, ovrlpdDeciBinNumbers.size: ${ovrlpdDeciBinNumbers.size}, totalDeciBinNumCount: ${totalDeciBinNumCount}, ovrlpdDeciBinNumbers: ${ovrlpdDeciBinNumbers}")
        }*/
        /*val startTime = System.currentTimeMillis();
        val index = 10000000
        val deciBinValue = computeDecibinaryNumber(index)
        println(s"index: ${index}, deciBinValue: ${deciBinValue}")
        val endTime = System.currentTimeMillis();
        val elapsedTime = (endTime - startTime)/1000.00
        println(s"Total time: ${elapsedTime} sec.")*/
        val startTime = System.currentTimeMillis();
        val index = 40000000
        val reqdDecimalValue = computeDecibinaryNumber(index)
        println(s"index: ${index}, reqdDecimalValue: ${reqdDecimalValue}")
        val endTime = System.currentTimeMillis();
        val elapsedTime = (endTime - startTime)/1000.00
        println(s"Total time: ${elapsedTime} sec.")


        /*for (index <- 1 to 19) {
            val deciBinValue = computeDecibinaryNumber(index)
            //println(s"index: ${index}, deciBinValue: ${deciBinValue}")
        }*/
    }
}
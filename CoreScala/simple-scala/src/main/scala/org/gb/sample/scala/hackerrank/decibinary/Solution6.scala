package org.gb.sample.scala.hackerrank.decibinary

import scala.math.pow
import scala.collection.mutable.Map

object Solution6 {

    private def append(decimalValue: Int, residualDecimalValue: Int, position: Int): Seq[Int] = {
        val placeValue = pow(2, position).toInt
        val deciBinValAtPosition = decimalValue / placeValue

        if (residualDecimalValue == 0) {
            return Seq(deciBinValAtPosition)
        }

        val higherPositionDeciBinNumbers = overlappedDecibinaryNumbers(residualDecimalValue, position + 1)
        val concatdDeciBinNumbers = higherPositionDeciBinNumbers.map(_ * 10 + deciBinValAtPosition)
        concatdDeciBinNumbers
    }

    private def overlappedDecibinaryNumbers(decimalValue: Int, position: Int): Seq[Int] = {
        val placeValue = pow(2, position).toInt
        val nextPlaceValue = 2 * placeValue
        val minDecValAtPosition = 0
        val maxDecValAtPosition = 9 * placeValue
        
        if (decimalValue <= maxDecValAtPosition) {
            val validDecValuesAtPosition = Range.inclusive(decimalValue, 0, -nextPlaceValue).seq
            validDecValuesAtPosition.map(decValAtPos => append(decValAtPos, decimalValue - decValAtPos, position)).flatten
        } else {
            val highestValidDecValAtPosition = Range(nextPlaceValue, decimalValue, nextPlaceValue).
                map(residualDecimalValue => decimalValue - residualDecimalValue).find(_ <= maxDecValAtPosition).get
            val validDecValuesAtPosition = Range.inclusive(highestValidDecValAtPosition, 0, -nextPlaceValue).seq
            validDecValuesAtPosition.map(decValAtPos => append(decValAtPos, decimalValue - decValAtPos, position)).flatten
        }
    }

    private def overlappedDecibinaryNumberCount(currentDecimalNumber: Int, prevDeciBinNumberCount: Int, cachedDecVsDeciBinNumberCount: Map[Int, Int]): Int = {
        if (currentDecimalNumber == 0) {
            return 1
        } else if (currentDecimalNumber % 2 == 1) {
            val ovrlpdDeciBinNumberCount = prevDeciBinNumberCount
            cachedDecVsDeciBinNumberCount.put(currentDecimalNumber, ovrlpdDeciBinNumberCount)
            return ovrlpdDeciBinNumberCount
        } else {
            val incrementedDeciBinNumberCount = prevDeciBinNumberCount - prevDeciBinNumberCount / 9
            val leftShiftedDeciBinNumberCount = cachedDecVsDeciBinNumberCount.remove(currentDecimalNumber / 2).get
            val ovrlpdDeciBinNumberCount = incrementedDeciBinNumberCount + leftShiftedDeciBinNumberCount
            cachedDecVsDeciBinNumberCount.put(currentDecimalNumber, ovrlpdDeciBinNumberCount)
            return ovrlpdDeciBinNumberCount
        }
    }

    def computeDecibinaryNumber(index: Long): Long = {
        var currentDecimalNumber = 0
        var totalDeciBinNumCount = 0
        var ovrlpdDeciBinNumberCount = 0
        val cachedDecVsDeciBinNumberCount = Map.empty[Int, Int]

        while (totalDeciBinNumCount < index) {
            ovrlpdDeciBinNumberCount = overlappedDecibinaryNumberCount(currentDecimalNumber, ovrlpdDeciBinNumberCount, cachedDecVsDeciBinNumberCount)
            totalDeciBinNumCount += ovrlpdDeciBinNumberCount
            //println(s"currentDecimalNumber: ${currentDecimalNumber}, ovrlpdDeciBinNumbers.size: ${ovrlpdDeciBinNumbers.size}, totalDeciBinNumCount: ${totalDeciBinNumCount}")
            //println(s"currentDecimalNumber: ${currentDecimalNumber}, ovrlpdDeciBinNumbers.size: ${ovrlpdDeciBinNumbers.size}, ovrlpdDeciBinNumbers: ${ovrlpdDeciBinNumbers}, totalDeciBinNumCount: ${totalDeciBinNumCount}")
            currentDecimalNumber += 1
        }

        val reqdDecimalValue = 840 //currentDecimalNumber - 1
        //return reqdDecimalValue
        val reqdDeciBinNumberOffset = 99756 //if (totalDeciBinNumCount == index) ovrlpdDeciBinNumberCount else ovrlpdDeciBinNumberCount - (totalDeciBinNumCount - index)
        val srtdOvrlpdDeciBinNumbers = overlappedDecibinaryNumbers(reqdDecimalValue, 0).sorted
        //println(s"reqdDeciBinNumberOffset: ${reqdDeciBinNumberOffset}, srtdOvrlpdDeciBinNumbers: ${srtdOvrlpdDeciBinNumbers}")
        println(s"reqdDecimalValue: ${reqdDecimalValue}, reqdDeciBinNumberOffset: ${reqdDeciBinNumberOffset}, "
            + s"srtdOvrlpdDeciBinNumbers.size: ${srtdOvrlpdDeciBinNumbers.size}, "
            + s"srtdOvrlpdDeciBinNumbers: ${srtdOvrlpdDeciBinNumbers}")

        val deciBinNumberAtIdx = srtdOvrlpdDeciBinNumbers(reqdDeciBinNumberOffset.intValue() - 1)
        deciBinNumberAtIdx
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
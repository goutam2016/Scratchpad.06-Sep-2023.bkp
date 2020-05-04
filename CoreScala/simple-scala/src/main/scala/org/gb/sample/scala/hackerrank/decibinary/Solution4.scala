package org.gb.sample.scala.hackerrank.decibinary

import scala.math.pow

object Solution4 {

    /**
     * The index will be determined by sorting all decibinary numbers according to it's corresponding decimal value and then sorting decibinary values within
     * the same decimal value.
     *
     * So, if we wish to find the decibinary number at 20th index, we can adopt the following approach.
     *
     * When decimal value = 0, how many decibinary numbers overlap? Answer: 1
     * When decimal value = 1, how many decibinary numbers overlap? Answer: 1
     * When decimal value = 2, how many decibinary numbers overlap? Answer: 2
     * When decimal value = 3, how many decibinary numbers overlap? Answer: 2
     * When decimal value = 4, how many decibinary numbers overlap? Answer: 4
     * When decimal value = 5, how many decibinary numbers overlap? Answer: 4
     * When decimal value = 6, how many decibinary numbers overlap? Answer: 6
     *
     * Up to decimal value 6, total number of decibinary numbers = (1 + 1 + 2 + 2 + 4 + 4 + 6) = 20
     * Which means, the 20th index will have decimal value = 6 and one of the 6 possible decibinary numbers that would evaluate to decimal value = 6.
     *
     * Therefore, we can write an algorithm in the following way:
     *
     * Starting from 0, increment the decimal value by 1 in a loop.
     * For each decimal value, derive the distinct decibinary numbers that would evaluate to the decimal value.
     * For each decimal value, add the count of overlapping decibinary numbers to a cumulative count for all decimal values so far.
     * When the cumulative count of decibinary numbers >= index, exit the loop. Note the decimal value when we reach/cross the index.
     * Sort the decibinary numbers that overlap the decimal value at the index.
     * Pick the decibinary number at the index.
     *
     */

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
    
    private def overlappedDecibinaryNumberCount(currentDecimalNumber: Int, prevDeciBinNumberCount: Int): Int = {
        if (currentDecimalNumber == 0) {
            return 1
        } else if (currentDecimalNumber % 2 == 1) {
            return prevDeciBinNumberCount
        } else {
            val residualAfterMinus10 = currentDecimalNumber - 10
            //val residualDeciBinNumberCount = overlappedDecibinaryNumbers(residualAfterMinus10, 1).size
            val residualDeciBinNumberCount = if (residualAfterMinus10 < 0) 0 else overlappedDecibinaryNumbers(residualAfterMinus10, 1).size
            val leftShiftedDeciBinNumberCount = overlappedDecibinaryNumbers(currentDecimalNumber, 1).size
            //println(s"currentDecimalNumber: ${currentDecimalNumber}, residualAfterMinus9: ${residualAfterMinus9}, residualDeciBinNumberCount: ${residualDeciBinNumberCount}, leftShiftedDeciBinNumberCount: ${leftShiftedDeciBinNumberCount}")
            return prevDeciBinNumberCount - residualDeciBinNumberCount + leftShiftedDeciBinNumberCount
        }
    }

    def computeDecibinaryNumber(index: Long): Long = {
        var currentDecimalNumber = 0
        var totalDeciBinNumCount = 0
        var ovrlpdDeciBinNumberCount = 0

        while (totalDeciBinNumCount < index) {
            ovrlpdDeciBinNumberCount = overlappedDecibinaryNumberCount(currentDecimalNumber, ovrlpdDeciBinNumberCount)
            totalDeciBinNumCount += ovrlpdDeciBinNumberCount
            println(s"currentDecimalNumber: ${currentDecimalNumber}, ovrlpdDeciBinNumberCount: ${ovrlpdDeciBinNumberCount}, totalDeciBinNumCount: ${totalDeciBinNumCount}")
            currentDecimalNumber += 1
        }

        val reqdDecimalValue = currentDecimalNumber - 1
        val reqdDeciBinNumberOffset = if (totalDeciBinNumCount == index) ovrlpdDeciBinNumberCount else ovrlpdDeciBinNumberCount - (totalDeciBinNumCount - index)
        val srtdOvrlpdDeciBinNumbers = overlappedDecibinaryNumbers(reqdDecimalValue, 0).sorted
        //println(s"reqdDeciBinNumberOffset: ${reqdDeciBinNumberOffset}, srtdOvrlpdDeciBinNumbers: ${srtdOvrlpdDeciBinNumbers}")
        println(s"reqdDecimalValue: ${reqdDecimalValue}, reqdDeciBinNumberOffset: ${reqdDeciBinNumberOffset}, "
            + s"srtdOvrlpdDeciBinNumbers.size: ${srtdOvrlpdDeciBinNumbers.size}, "
            + s"srtdOvrlpdDeciBinNumbers: ${srtdOvrlpdDeciBinNumbers}")

        val deciBinNumberAtIdx = srtdOvrlpdDeciBinNumbers(reqdDeciBinNumberOffset.intValue() - 1)
        deciBinNumberAtIdx
    }

    def main(args: Array[String]): Unit = {
        /*for(decimalNumber <- 0 to 16) {
            val ovrlpdDeciBinNumbers = overlappedDecibinaryNumbers(decimalNumber, 0)
            println(s"decimalNumber: ${decimalNumber}, ovrlpdDeciBinNumbers.size: ${ovrlpdDeciBinNumbers.size}, ovrlpdDeciBinNumbers: ${ovrlpdDeciBinNumbers}")
        }*/
        val startTime = System.currentTimeMillis();
        val index = 10000
        val deciBinValue = computeDecibinaryNumber(index)
        println(s"index: ${index}, deciBinValue: ${deciBinValue}")
        val endTime = System.currentTimeMillis();
        val elapsedTime = (endTime - startTime)/1000.00
        println(s"Total time: ${elapsedTime} sec.")

        /*for (index <- 1 to 19) {
            val deciBinValue = computeDecibinaryNumber(index)
            //println(s"index: ${index}, deciBinValue: ${deciBinValue}")
        }*/
    }
}
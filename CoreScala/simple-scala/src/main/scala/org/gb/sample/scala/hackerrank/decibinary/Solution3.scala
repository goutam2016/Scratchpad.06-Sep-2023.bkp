package org.gb.sample.scala.hackerrank.decibinary

import scala.math.pow

object Solution3 {

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
        /*val concatdDeciBinNumbers = higherPositionDeciBinNumbers
            .map(higherPosDeciBinVal => higherPosDeciBinVal.toString().concat(deciBinValAtPosition.toString()))*/
        val concatdDeciBinNumbers = higherPositionDeciBinNumbers.map(higherPosDeciBinVal => higherPosDeciBinVal * 10 + deciBinValAtPosition)
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
    
    private def overlappedDecibinaryNumbers(currentDecimalNumber: Int, prevDeciBinNumbers: Seq[Int]): Seq[Int] = {
        if (currentDecimalNumber == 0) {
            return Seq(0)
        } else if (currentDecimalNumber % 2 == 1) {
            return prevDeciBinNumbers.map(_ + 1)
        } else {
            val incrementedDeciBinNumbers = prevDeciBinNumbers.filterNot(_ % 10 == 9).map(_ + 1)
            /*
             * The recursive call to overlappedDecibinaryNumbers may not be necessary here, as the leftShiftedDeciBinNumbers can be derived
             * from the decibinary numbers corresponding to (currentDecimalNumber/2) and then multiplying them by 10. So, we need to hold the previous 
             * decimal vs decibinary numbers in a cache. Once the currentDecimalNumber becomes more than double of a decimal number hold in the cache,
             * the entry can be removed from the cache.
             */
            val leftShiftedDeciBinNumbers = overlappedDecibinaryNumbers(currentDecimalNumber, 1).map(_ * 10)
            return incrementedDeciBinNumbers ++ leftShiftedDeciBinNumbers
        }
    }
    
    def computeDecibinaryNumber(index: Long): Long = {
        var currentDecimalNumber = 0
        var totalDeciBinNumCount = 0
        var ovrlpdDeciBinNumbers = Seq.empty[Int]

        while (totalDeciBinNumCount < index) {
            ovrlpdDeciBinNumbers = overlappedDecibinaryNumbers(currentDecimalNumber, ovrlpdDeciBinNumbers)
            totalDeciBinNumCount += ovrlpdDeciBinNumbers.size
            //println(s"currentDecimalNumber: ${currentDecimalNumber}, ovrlpdDeciBinNumbers.size: ${ovrlpdDeciBinNumbers.size}, totalDeciBinNumCount: ${totalDeciBinNumCount}")
            //println(s"currentDecimalNumber: ${currentDecimalNumber}, ovrlpdDeciBinNumbers.size: ${ovrlpdDeciBinNumbers.size}, ovrlpdDeciBinNumbers: ${ovrlpdDeciBinNumbers}, totalDeciBinNumCount: ${totalDeciBinNumCount}")
            currentDecimalNumber += 1
        }

        val reqdDecimalValue = currentDecimalNumber - 1
        val reqdDeciBinNumberOffset = if (totalDeciBinNumCount == index) ovrlpdDeciBinNumbers.size else ovrlpdDeciBinNumbers.size - (totalDeciBinNumCount - index)
        val srtdOvrlpdDeciBinNumbers = ovrlpdDeciBinNumbers.sorted
        //println(s"reqdDeciBinNumberOffset: ${reqdDeciBinNumberOffset}, srtdOvrlpdDeciBinNumbers: ${srtdOvrlpdDeciBinNumbers}")
        println(s"reqdDecimalValue: ${reqdDecimalValue}, reqdDeciBinNumberOffset: ${reqdDeciBinNumberOffset}, "
            + s"srtdOvrlpdDeciBinNumbers.size: ${srtdOvrlpdDeciBinNumbers.size}, "
            + s"srtdOvrlpdDeciBinNumbers: ${srtdOvrlpdDeciBinNumbers}")

        val deciBinNumberAtIdx = srtdOvrlpdDeciBinNumbers(reqdDeciBinNumberOffset.intValue() - 1)
        deciBinNumberAtIdx
    }

    def main(args: Array[String]): Unit = {
        /*for(decimalNumber <- 0 to 24) {
            val ovrlpdDeciBinNumbers = overlappedDecibinaryNumbers(decimalNumber, 0)
            println(s"decimalNumber: ${decimalNumber}, ovrlpdDeciBinNumbers.size: ${ovrlpdDeciBinNumbers.size}, ovrlpdDeciBinNumbers: ${ovrlpdDeciBinNumbers}")
        }*/
        val startTime = System.currentTimeMillis();
        val index = 10000000
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
package org.gb.sample.scala.hackerrank.decibinary

import scala.math.pow

object Solution2 {

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

    private def append(decimalValue: Int, residualDecimalValue: Int, position: Int): Seq[Long] = {
        val placeValue = pow(2, position).toInt
        val deciBinValAtPosition = decimalValue / placeValue

        if (residualDecimalValue == 0) {
            return Seq(deciBinValAtPosition)
        }

        val higherPositionDeciBinNumbers = overlappedDecibinaryNumbers(residualDecimalValue, position + 1)
        val concatdDeciBinNumbers = higherPositionDeciBinNumbers
            .map(higherPosDeciBinVal => higherPosDeciBinVal.toString().concat(deciBinValAtPosition.toString()))
        concatdDeciBinNumbers.map(_.toLong)
    }

    private def overlappedDecibinaryNumbers(decimalValue: Int, position: Int): Seq[Long] = {
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

    private def overlappedDecibinaryNumbers(currentDecimalNumber: Int): Seq[Long] = {
        val ovrlpdDeciBinNumbers = overlappedDecibinaryNumbers(currentDecimalNumber, 0)
        //println(s"currentDecimalNumber: ${currentDecimalNumber}, ovrlpdDeciBinNumbers.size: ${ovrlpdDeciBinNumbers.size}, ovrlpdDeciBinNumbers: ${ovrlpdDeciBinNumbers}")
        ovrlpdDeciBinNumbers
    }

    def computeDecibinaryNumber(index: Long): Long = {
        val deciBinNumbersCountMapper = (decimalValue: Int) => (decimalValue, overlappedDecibinaryNumbers(decimalValue).size)
        val deciValVsTotalDeciBinNumCountTuples = Stream.from(0).map(deciBinNumbersCountMapper)
            .scanLeft((0, 0))((deciValVsTotalDeciBinNumCountTuple, deciValVsDeciBinNumberCountTuple) => (deciValVsDeciBinNumberCountTuple._1, deciValVsTotalDeciBinNumCountTuple._2 + deciValVsDeciBinNumberCountTuple._2))
            .takeWhile(_._2 <= index)
        val reqdDecimalValue = if (deciValVsTotalDeciBinNumCountTuples.last._2 == index) deciValVsTotalDeciBinNumCountTuples.last._1 else deciValVsTotalDeciBinNumCountTuples.last._1 + 1
        val deciBinNumbersForReqdDecValue = overlappedDecibinaryNumbers(reqdDecimalValue)
        val sortedDeciBinNumbersForReqdDecValue = deciBinNumbersForReqdDecValue.sorted

        val reqdDeciBinNumberOffset = if (deciValVsTotalDeciBinNumCountTuples.last._2 == index) sortedDeciBinNumbersForReqdDecValue.size else index - deciValVsTotalDeciBinNumCountTuples.last._2
        /*println(s"reqdDecimalValue: ${reqdDecimalValue}, reqdDeciBinNumberOffset: ${reqdDeciBinNumberOffset}, "  
                + s"sortedDeciBinNumbersForReqdDecValue.size: ${sortedDeciBinNumbersForReqdDecValue.size}, "
                + s"sortedDeciBinNumbersForReqdDecValue: ${sortedDeciBinNumbersForReqdDecValue}")*/
        val deciBinNumberAtIdx = sortedDeciBinNumbersForReqdDecValue(reqdDeciBinNumberOffset.intValue() - 1)
        deciBinNumberAtIdx
    }

    private def computeDecibinaryNumber_bkp(index: Int): Int = {
        val deciBinNumbersCountMapper = (decimalValue: Int) => (decimalValue, overlappedDecibinaryNumbers(decimalValue).size)
        val deciValVsTotalDeciBinNumCountTuples = Stream.from(0).map(deciBinNumbersCountMapper)
            .scanLeft((0, 0))((deciValVsTotalDeciBinNumCountTuple, deciValVsDeciBinNumberCountTuple) => (deciValVsDeciBinNumberCountTuple._1, deciValVsTotalDeciBinNumCountTuple._2 + deciValVsDeciBinNumberCountTuple._2))
            .takeWhile(_._2 <= index)
        //println(s"decimal value: ${deciValVsTotalDeciBinNumCountTuples.last._1}, total decibinary numbers count: ${deciValVsTotalDeciBinNumCountTuples.last._2}")
        val reqdDecimalValue = if (deciValVsTotalDeciBinNumCountTuples.last._2 == index) deciValVsTotalDeciBinNumCountTuples.last._1 else deciValVsTotalDeciBinNumCountTuples.last._1 + 1
        val deciBinNumbersForReqdDecValue = overlappedDecibinaryNumbers(reqdDecimalValue)
        println(s"No. of decibinary numbers for required decimal value: ${deciBinNumbersForReqdDecValue.size}")
        val sortingStartTime = System.currentTimeMillis();
        val sortedDeciBinNumbersForReqdDecValue = deciBinNumbersForReqdDecValue.sorted
        val sortingEndTime = System.currentTimeMillis();
        val sortingElapsedTime = (sortingEndTime - sortingStartTime)/1000.00
        println(s"Sorting time: ${sortingElapsedTime}")
        
        val reqdDeciBinNumberOffset = if (deciValVsTotalDeciBinNumCountTuples.last._2 == index) sortedDeciBinNumbersForReqdDecValue.size else index - deciValVsTotalDeciBinNumCountTuples.last._2
       //println(s"decimal value: ${reqdDecimalValue}, deciBinNumbersForReqdDecValue: ${deciBinNumbersForReqdDecValue}")
        val deciBinNumberAtIdx = sortedDeciBinNumbersForReqdDecValue(reqdDeciBinNumberOffset - 1)
        println(s"index: ${index}, decibinary: ${deciBinNumberAtIdx}, decimal value: ${reqdDecimalValue}")
        deciBinNumberAtIdx.toInt
    }

    def main(args: Array[String]): Unit = {
        //val placeValue = pow(2, 2).toInt
        //Range.inclusive(10, 0, -placeValue).foreach(println)
        //overlappedDecibinaryNumbers(17, 0).foreach(println)
        val startTime = System.currentTimeMillis();
        val index = 200
        val deciBinValue = computeDecibinaryNumber(index)
        println(s"index: ${index}, deciBinValue: ${deciBinValue}")
        val endTime = System.currentTimeMillis();
        val elapsedTime = (endTime - startTime)/1000.00
        println(s"Total time: ${elapsedTime}")

        /*for (index <- 1 to 19) {
            val deciBinValue = computeDecibinaryNumber(index)
            //println(s"index: ${index}, deciBinValue: ${deciBinValue}")
        }*/
    }
}
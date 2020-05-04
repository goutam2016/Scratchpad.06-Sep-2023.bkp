package org.gb.sample.scala.hackerrank.decibinary

import scala.math.pow

object Solution1 {
    
    private def computeDecimalValue(deciBinValue: Int): Int = {
        val startTime = System.currentTimeMillis()
        val noOfDigits = deciBinValue.toString().size
        val digits = deciBinValue.toString().map(_.asDigit)
        val decimalConverter = (sum: Int, digitVsPositionTuple: (Int, Int)) => {
            val digit = digitVsPositionTuple._1
            val position = digitVsPositionTuple._2
            sum + (digit * pow(2, noOfDigits - 1 - position).toInt)
        }
        val decimalValue = digits.zipWithIndex.foldLeft(0)(decimalConverter)
        val endTime = System.currentTimeMillis()
        val durationInSec = (startTime - endTime)
        //println(s"duration: ${durationInSec}")
        return decimalValue
    }

    private def mapToDecVsDeciBinVal(deciBinValue: Int): (Int, Int) = {
        val decimalValue = computeDecimalValue(deciBinValue)
        (decimalValue, deciBinValue)
    }
    
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

    def computeDecibinaryNumber_bkp(index: Int): Int = {    
        val decValueSorter = Ordering[(Int, Int)].on((decValVsDeciBinValTuple: (Int, Int)) => (decValVsDeciBinValTuple._1, decValVsDeciBinValTuple._2))
        /*
         * powersLessThanIndex: A list of powers of 10 (in increasing order), that produce decibinary numbers with decimal equivalents less than the passed index.
         * The last element is the highest power of 10, that produce a decibinary number with decimal equivalent less than the passed index.
         */
        /*val powersLessThanIndex = Stream.from(0).map(n => (n, pow(10, n).toInt))
                                                .map(powerVsDeciBinVal => (powerVsDeciBinVal._1, computeDecimalValue(powerVsDeciBinVal._2)))
                                                .takeWhile(powerVsDecVal => powerVsDecVal._2 < index)*/
        /*
         * powersLessThanIndex: A list of powers of 2 (in increasing order), that compute to less than the passed index.
         * The last element is the highest power of 2, that is less than the passed index.
         */                  
        val powersLessThanIndex = Stream.from(0).map(n => (n, pow(2, n).toInt)).takeWhile(powerVsVal => powerVsVal._2 < index)
        val highestPowerLessThanIndex = if(powersLessThanIndex.isEmpty) -1 else powersLessThanIndex.last._1
        println(s"highestPowerLessThanIndex: ${highestPowerLessThanIndex}")

        val deciBinValVsDecValTuples = Range(0, pow(10, highestPowerLessThanIndex + 1).toInt).map(mapToDecVsDeciBinVal)
        println(s"deciBinValVsDecValTuples.size: ${deciBinValVsDecValTuples.size}")
        val startTime = System.currentTimeMillis()
        val sortedDeciBinValVsDecValTuples = deciBinValVsDecValTuples.sorted(decValueSorter).map(_.swap)
        val endTime = System.currentTimeMillis()
        val durationInSec = (endTime - startTime)/1000.00
        println(s"duration: ${durationInSec} seconds")
        /*deciBinValVsDecValTuples.zipWithIndex
            .filter(_._2 < index)
            .foreach(deciBinValVsDecValVsIdxTuple => 
                println(s"index: ${deciBinValVsDecValVsIdxTuple._2 + 1}, decibinary: ${deciBinValVsDecValVsIdxTuple._1._1}, decimal: ${deciBinValVsDecValVsIdxTuple._1._2}"))*/
        //val deciBinValVsDecValTuples = Range(0, pow(10, highestPowerLessThanIndex + 1).toInt).map(mapToDecVsDeciBinVal).map(_.swap)
        //deciBinValVsDecValTuples.foreach(deciBinValVsDecValTuple => println(s"decibinary: ${deciBinValVsDecValTuple._1}, decimal: ${deciBinValVsDecValTuple._2}"))
        //deciBinValVsDecValTuples.groupBy(_._2).mapValues(_.size).toSeq.sortBy(_._1).foreach(decValVsCountTuple => println(s"decimal value: ${decValVsCountTuple._1}, count: ${decValVsCountTuple._2}"))
        println("----------------------------------------------")
        sortedDeciBinValVsDecValTuples(index - 1)._1
    }
    
    private def append(decimalValue: Int, residualDecimalValue:Int, position: Int): Seq[Int] = {
        val placeValue = pow(2, position).toInt
        val deciBinValAtPosition = decimalValue / placeValue
        
        if(residualDecimalValue == 0) {
            return Seq(deciBinValAtPosition)
        }
        
        val higherPositionDeciBinNumbers = overlappedDecibinaryNumbers(residualDecimalValue, position + 1)
        val concatdDeciBinNumbers = higherPositionDeciBinNumbers
                                    .map(higherPosDeciBinVal => higherPosDeciBinVal.toString().concat(deciBinValAtPosition.toString()))
        concatdDeciBinNumbers.map(_.toInt)
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
    
    private def overlappedDecibinaryNumbers(decimalValue: Int): Seq[Int] = {
        overlappedDecibinaryNumbers(decimalValue, 0)
        /*if(decimalValue == 0) {
            Seq(0)
        } else if (decimalValue == 1) {
            Seq(1)
        } else if (decimalValue == 2) {
            Seq(2, 10)
        } else if (decimalValue == 3) {
            Seq(3, 11)
        } else if (decimalValue == 4) {
            Seq(4, 12, 20, 100)
        } else {
            Seq()
        }*/
    }

    private def computeDecibinaryNumber(index: Int): Int = {
        val deciBinNumbersCountMapper = (decimalValue: Int) => (decimalValue, overlappedDecibinaryNumbers(decimalValue).size)
        val deciValVsTotalDeciBinNumCountTuples = Stream.from(0).map(deciBinNumbersCountMapper)
            .scanLeft((0, 0))((deciValVsTotalDeciBinNumCountTuple, deciValVsDeciBinNumberCountTuple) 
                    => (deciValVsDeciBinNumberCountTuple._1, deciValVsTotalDeciBinNumCountTuple._2 + deciValVsDeciBinNumberCountTuple._2))
            .takeWhile(_._2 < index)
        println(s"decimal value: ${deciValVsTotalDeciBinNumCountTuples.last._1}, total decibinary numbers count: ${deciValVsTotalDeciBinNumCountTuples.last._2}")
        val reqdDecimalValue = deciValVsTotalDeciBinNumCountTuples.last._1+1
        val reqdDeciBinNumberOffset = index-deciValVsTotalDeciBinNumCountTuples.last._2
        val deciBinNumbersForReqdDecValue = overlappedDecibinaryNumbers(reqdDecimalValue)
        deciBinNumbersForReqdDecValue(reqdDeciBinNumberOffset-1)
    }
    
    def main(args: Array[String]): Unit = {
        //val placeValue = pow(2, 2).toInt
        //Range.inclusive(10, 0, -placeValue).foreach(println)
        //overlappedDecibinaryNumbers(17, 0).foreach(println)
        val index = 20
        val deciBinValue = computeDecibinaryNumber(index)
        println(s"index: ${index}, deciBinValue: ${deciBinValue}")

        /*for (index <- 1 to 20) {
            val deciBinValue = computeDecibinaryNumber(10)
            println(s"index: ${index}, deciBinValue: ${deciBinValue}")
        }*/
    }
}
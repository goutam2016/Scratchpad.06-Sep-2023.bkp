package org.gb.sample.spark.income

import org.apache.spark.rdd.RDD
import scala.util.control.Breaks._

class IncomeBandMapper(nameVsIncomeLines: RDD[String], incomeSlabs: List[Int]) extends Serializable {
    def getCountPerIncomeBand(): Map[Band, Long] = {
        val countPerIncomeBand = nameVsIncomeLines.map(_.split(",")).filter(_.length == 3).map(_(2)).map(mapToBand).filter(_ != null).countByValue()
        countPerIncomeBand.toMap
    }

    private def mapToBand(incomeStr: String): Band = {
        var income = 0
        try {
            income = incomeStr.toInt
        } catch {
            case nfe: NumberFormatException => return null
        }

        var band: Band = null;
        breakable {
            for (i <- 0 to incomeSlabs.size) {
                val currentSlab = incomeSlabs(i)
                val nextSlab = if ((i + 1) < incomeSlabs.size) incomeSlabs(i + 1) else -1

                if (nextSlab == -1) {
                    if (income > currentSlab) {
                        band = Band(currentSlab, nextSlab)
                        break;
                    }
                } else if (income > currentSlab && income <= nextSlab) {
                    band = Band(currentSlab, nextSlab)
                    break;
                }
            }
        }
        println(s"Income: $income falls in band: $band.")
        return band
    }
}
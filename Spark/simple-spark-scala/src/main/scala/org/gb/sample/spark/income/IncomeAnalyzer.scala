package org.gb.sample.spark.income

import org.apache.spark.rdd.RDD
import org.apache.commons.csv.CSVRecord
import scala.collection.immutable.SortedMap

class IncomeAnalyzer(nameVsIncomeLines: RDD[String], personProfileLines: RDD[String]) extends Serializable {

    val nameVsIncomePairs = nameVsIncomeLines.map(Converter.convertToCSVRecord).filter(_ != null).map(tuplizePersonNameVsIncome)
    val nameVsProfilePairs = personProfileLines.map(Converter.convertToPersProfile).filter(_ != null).map(tuplizePersonNameVsPersonProfile)

    def getTopIncomePersonProfiles(topNum: Int): Map[Int, List[PersonProfile]] = {
        val incomeComparator = Ordering.by[(Int, PersonProfile), Int](_._1).reverse
        val incomeVsPersProfilePairs = nameVsIncomePairs.join(nameVsProfilePairs).values.takeOrdered(topNum)(incomeComparator).toList
        val incomeVsPersProfiles = incomeVsPersProfilePairs.groupBy(_._1).mapValues(_.map(_._2))
        SortedMap(incomeVsPersProfiles.toSeq: _*)(Ordering.by[Int, Int](income => income).reverse)
    }

    private def tuplizePersonNameVsPersonProfile(persProfile: PersonProfile): (PersonName, PersonProfile) = {
        val persName = PersonName(persProfile.firstName, persProfile.lastName)
        return (persName, persProfile)
    }

    private def tuplizePersonNameVsIncome(csvRecord: CSVRecord): (PersonName, Int) = {
        val persName = PersonName(csvRecord.get(0), csvRecord.get(1))

        var income = 0
        try {
            income = csvRecord.get(2).toInt
        } catch {
            case nfe: NumberFormatException => println(s"Could not convert income-str: ${csvRecord.get(2)} to an Int.")
        }

        return (persName, income)
    }
}
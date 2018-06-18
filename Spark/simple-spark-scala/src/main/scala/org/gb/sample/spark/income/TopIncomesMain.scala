package org.gb.sample.spark.income

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object TopIncomesMain {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Person profiles with top incomes")
        val sc = new SparkContext(conf)
        val nameVsIncomeFile = args(0)
        val personProfileFile = args(1)
        val nameVsIncomeLines = sc.textFile(nameVsIncomeFile)
        val personProfileLines = sc.textFile(personProfileFile)
        val incomeAnalyzer = new IncomeAnalyzer(nameVsIncomeLines, personProfileLines)
        val topIncomesWithPersonProfiles = incomeAnalyzer.getTopIncomePersonProfiles(10)
        topIncomesWithPersonProfiles.foreach(printIncomeWithPersonProfiles)
    }

    def printIncomeWithPersonProfiles(incomeWithPersProfiles: (Int, List[PersonProfile])): Unit = {
        incomeWithPersProfiles._2.foreach(persProfile => {
            val fullName = String.join(" ", persProfile.firstName, persProfile.lastName)
            val persProfileAsString = String.join(",", fullName, persProfile.companyName, persProfile.emailAddress)
            println(s"${incomeWithPersProfiles._1} <--> $persProfileAsString")
        })
    }
}
package org.gb.sample.spark.income

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object IncomeBandMain {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Person count per income band")
        val sc = new SparkContext(conf)
        val nameVsIncomeFile = args(0)
        val incomeSlabs = args(1).split(",").map(_.toInt).toList
        val nameVsIncomeLines = sc.textFile(nameVsIncomeFile)
        val incomeBandMapper = new IncomeBandMapper(nameVsIncomeLines, incomeSlabs)
        val countPerIncomeBand = incomeBandMapper.getCountPerIncomeBand()
        countPerIncomeBand.foreach(bandVsCountPair => println(bandVsCountPair._1 + ":" + bandVsCountPair._2))
    }
}

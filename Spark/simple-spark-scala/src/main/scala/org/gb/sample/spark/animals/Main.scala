package org.gb.sample.spark.animals

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Main {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Animals")
        val sc = new SparkContext(conf)

        val mammalsFile = args(0)
        val primatesFile = args(1);

        val mammals = sc.textFile(mammalsFile)
        val primates = sc.textFile(primatesFile)

        val unionResult = mammals.union(primates)
        val intersectionResult = mammals.intersection(primates)
        val subtractResult = mammals.subtract(primates)

        println("Union result:");
        println("---------------------------------------------------------------------\n");
        unionResult.foreach(println);
        println();

        println("Intersection result:");
        println("---------------------------------------------------------------------\n");
        intersectionResult.foreach(println);
        println();

        println("Subtract result:");
        println("---------------------------------------------------------------------\n");
        subtractResult.foreach(println);
        println();
    }
}
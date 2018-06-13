package org.gb.sample.spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object ContextInfoMain {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Spark Context Information")
        val sc = new SparkContext(conf)
        val appName = sc.appName
        val local = sc.isLocal
        val sparkVersion = sc.version
        val user = sc.sparkUser
        val appId = sc.getConf.getAppId
        val parameterPairs = sc.getConf.getAll

        println("App Name: " + appName);
        println("Is local? " + local);
        println("Spark Version: " + sparkVersion);
        println("User: " + user);
        println("App Id: " + appId);

        for (parameterPair <- parameterPairs) {
            val paramName = parameterPair._1;
            val paramValue = parameterPair._2;
            println("Param Name: " + paramName + ", Param Value: " + paramValue);
        }
    }
}
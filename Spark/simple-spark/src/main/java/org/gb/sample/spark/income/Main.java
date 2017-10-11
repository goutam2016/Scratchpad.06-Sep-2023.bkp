package org.gb.sample.spark.income;

import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Main {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Person profiles with top incomes");
		JavaSparkContext sc = new JavaSparkContext(conf);
		String nameVsIncomeFile = args[0];
		JavaRDD<String> nameVsIncomeLines = sc.textFile(nameVsIncomeFile);
		showTopIncomes(nameVsIncomeLines);
		sc.close();
	}

	private static void showTopIncomes(JavaRDD<String> nameVsIncomeLines) {
		List<Tuple2<Integer, PersonName>> topIncomesWithPersonNames = nameVsIncomeLines
				.map(nameVsIncomeLine -> nameVsIncomeLine.split(",")).mapToPair(Main::tuplizeIncomeVsPersonName)
				.sortByKey(false).take(10);
		topIncomesWithPersonNames.forEach(Main::printIncomeWithPersonNames);
	}

	private static Tuple2<Integer, PersonName> tuplizeIncomeVsPersonName(String[] tokenizedNameVsIncomeLine) {
		PersonName personName = new PersonName(tokenizedNameVsIncomeLine[0], tokenizedNameVsIncomeLine[1]);
		Integer income = Integer.parseInt(tokenizedNameVsIncomeLine[2]);
		return new Tuple2<Integer, PersonName>(income, personName);
	}

	private static void printIncomeWithPersonNames(Tuple2<Integer, PersonName> incomeVsPersonName) {
		System.out.println(incomeVsPersonName._1() + " <--> " + incomeVsPersonName._2());
	}

}

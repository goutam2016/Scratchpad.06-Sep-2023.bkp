package org.gb.sample.spark.income;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Main {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local").setAppName("Person profiles with top incomes");
		JavaSparkContext sc = new JavaSparkContext(conf);
		String nameVsIncomeFile = "data/income/name-vs-income_50000.txt";
		JavaRDD<String> nameVsIncomeLines = sc.textFile(nameVsIncomeFile);
		findTopIncomes(nameVsIncomeLines);
		sc.close();
	}
	
	private static void findTopIncomes(JavaRDD<String> nameVsIncomeLines) {
		JavaRDD<String[]> tokenizedNameVsIncomeLines = nameVsIncomeLines.map(nameVsIncomeLine -> nameVsIncomeLine.split(","));
		List<Tuple2<Integer, PersonName>> topIncomesWithPersonNames = tokenizedNameVsIncomeLines.mapToPair(Main::tuplizeIncomeVsPersonName).sortByKey(false).take(10);
		//List<Tuple2<Integer, PersonName>> topIncomesWithPersonNames = tokenizedNameVsIncomeLines.mapToPair(Main::tuplizeIncomeVsPersonName).top(10, Comparator.comparing(Tuple2::_1));
		Consumer<Tuple2<Integer, PersonName>> incomeVsPersonNamePrinter = incomeVsPersonName -> System.out.println(incomeVsPersonName._1() + " <--> " + incomeVsPersonName._2());
		topIncomesWithPersonNames.forEach(incomeVsPersonNamePrinter);	
	}
	
	private static Tuple2<Integer, PersonName> tuplizeIncomeVsPersonName(String[] tokenizedNameVsIncomeLine) {
		PersonName personName = new PersonName(tokenizedNameVsIncomeLine[0], tokenizedNameVsIncomeLine[1]);
		Integer income = Integer.parseInt(tokenizedNameVsIncomeLine[2]);
		return new Tuple2<Integer, PersonName>(income, personName);
	}

}

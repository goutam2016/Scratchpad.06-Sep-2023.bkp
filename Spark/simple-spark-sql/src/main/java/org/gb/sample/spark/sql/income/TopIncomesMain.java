package org.gb.sample.spark.sql.income;

import java.util.List;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.gb.sample.spark.sql.income.PersonProfile;

public class TopIncomesMain {

	public static void main(String[] args) {
        String nameVsIncomeFile = args[0];
        String personProfileFile = args[1];
        SparkSession session = SparkSession
                .builder()
                .appName("Person profiles with top incomes - using Spark SQL")
                .getOrCreate();
        Dataset<Row> nameVsIncomeRows = session.read().csv(nameVsIncomeFile);
        Dataset<Row> personProfileRows = session.read().csv(personProfileFile);
        IncomeAnalyzer incomeAnalyzer = new IncomeAnalyzer(nameVsIncomeRows, personProfileRows);
        Map<Integer, List<PersonProfile>> topIncomesWithPersonProfiles = incomeAnalyzer.getTopIncomePersonProfiles(10);
        topIncomesWithPersonProfiles.forEach(TopIncomesMain::printIncomeWithPersonProfiles);
        session.close();
    }
	
	private static void printIncomeWithPersonProfiles(Integer income, List<PersonProfile> personProfiles) {
		personProfiles.forEach(profile -> {
			String fullName = String.join(" ", profile.getFirstName(), profile.getLastName());
			String incomeWithPersProfileAsString = String.join(", ", fullName, profile.getCompanyName(),
					profile.getEmailAddress());
			System.out.println(income + " <--> " + incomeWithPersProfileAsString);
		});
	}
}

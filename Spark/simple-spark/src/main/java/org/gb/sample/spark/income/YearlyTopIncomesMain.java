package org.gb.sample.spark.income;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class YearlyTopIncomesMain {

	public static void main(String[] args) {
		try {
			SparkConf conf = new SparkConf().setAppName("Person profiles with top incomes per year");
			JavaSparkContext sc = new JavaSparkContext(conf);
			String yearlyNameVsIncomeDir = args[0];
			String personProfileFile = args[1];

			Path yearlyNameVsIncomePath = Paths.get(yearlyNameVsIncomeDir);
			Map<Integer, String> nameVsIncomePerYear = Files.list(yearlyNameVsIncomePath).map(Path::toFile)
					.filter(File::isFile)
					.collect(Collectors.toMap(YearlyTopIncomesMain::getNameVsIncomeYear, File::getPath));

			JavaRDD<String> personProfileLines = sc.textFile(personProfileFile);
			IncomeAnalyzer incomeAnalyzer = new DefaultIncomeAnalyzer(personProfileLines);

			Set<Entry<Integer, String>> nameVsIncomeEntries = nameVsIncomePerYear.entrySet();
			for (Entry<Integer, String> nameVsIncomeEntry : nameVsIncomeEntries) {
				JavaRDD<String> nameVsIncomeLines = sc.textFile(nameVsIncomeEntry.getValue());
				Map<Integer, List<PersonProfile>> topIncomesWithPersonProfiles = incomeAnalyzer
						.getTopIncomePersonProfiles(nameVsIncomeLines, 10);
				System.out.println("Year: " + nameVsIncomeEntry.getKey());
				System.out.println("-----------------------------------------------------------------");
				topIncomesWithPersonProfiles.forEach(YearlyTopIncomesMain::printIncomeWithPersonProfiles);
				System.out.println("-----------------------------------------------------------------");
			}
			incomeAnalyzer.cleanup();
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Integer getNameVsIncomeYear(File nameVsIncomeFile) {
		String nameVsIncomePath = nameVsIncomeFile.getPath();
		int idxOfLastUnderscore = nameVsIncomePath.lastIndexOf('_');
		int idxOfLastDot = nameVsIncomePath.lastIndexOf('.');
		String yearAsStr = nameVsIncomePath.substring(idxOfLastUnderscore + 1, idxOfLastDot);
		return Integer.parseInt(yearAsStr);
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

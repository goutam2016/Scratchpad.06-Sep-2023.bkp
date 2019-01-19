package org.gb.sample.spark.income;

import java.io.Console;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class YearlyTopIncomesMain {

	public static void main(String[] args) {
		try {
			Console console = System.console();
			console.printf("Choose income analysis strategy:\n");
			console.printf(
					"1 - Directly join name vs profile and name vs income dataset for each year, then find the top incomes.\n");
			console.printf(
					"2 - Partition and cache the name vs profile dataset then join with name vs income dataset for each year, then find the top incomes.\n");
			console.printf(
					"3 - Partition and cache the name vs profile dataset, find the top incomes from name vs income for each year, then do a broadcast hash join.\n");
			String consInput = console.readLine("Select one of the above numbers: ");
			int incomeAnalysisStgy = 0;

			try {
				incomeAnalysisStgy = Integer.parseInt(consInput);

				if (incomeAnalysisStgy < 1 || incomeAnalysisStgy > 3) {
					throw new IllegalArgumentException("Invalid entry for income analysis strategy, exiting.");
				}
			} catch (NumberFormatException nfe) {
				throw new IllegalArgumentException("Invalid entry for income analysis strategy, exiting.");
			}

			SparkConf conf = new SparkConf().setAppName("Person profiles with top incomes per year");
			JavaSparkContext sc = new JavaSparkContext(conf);
			String yearlyNameVsIncomeDir = args[0];
			String personProfileFile = args[1];

			Path yearlyNameVsIncomePath = Paths.get(yearlyNameVsIncomeDir);
			Map<Integer, String> filePathPerYear = Files.list(yearlyNameVsIncomePath).map(Path::toFile)
					.filter(File::isFile)
					.collect(Collectors.toMap(YearlyTopIncomesMain::getNameVsIncomeYear, File::getPath));

			JavaRDD<String> personProfileLines = sc.textFile(personProfileFile);
			IncomeAnalyzer incomeAnalyzer = deriveIncomeAnalysisImplementation(incomeAnalysisStgy, personProfileLines);

			Set<Entry<Integer, String>> yearVsFilePathEntries = filePathPerYear.entrySet();
			Comparator<Entry<Integer, String>> yearCmptr = Comparator.comparing(Entry::getKey);
			SortedSet<Entry<Integer, String>> sortedYearVsFilePathEntries = new TreeSet<>(yearCmptr);
			sortedYearVsFilePathEntries.addAll(yearVsFilePathEntries);
			
			for (Entry<Integer, String> yearVsFilePathEntry : sortedYearVsFilePathEntries) {
				JavaRDD<String> nameVsIncomeLines = sc.textFile(yearVsFilePathEntry.getValue());
				Map<Integer, List<PersonProfile>> topIncomesWithPersonProfiles = incomeAnalyzer
						.getTopIncomePersonProfiles(nameVsIncomeLines, 10);
				System.out.println("Year: " + yearVsFilePathEntry.getKey());
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

	private static IncomeAnalyzer deriveIncomeAnalysisImplementation(int incomeAnalysisStgy,
			JavaRDD<String> personProfileLines) {
		IncomeAnalyzer incomeAnalyzer = null;
		switch (incomeAnalysisStgy) {
		case 1:
			incomeAnalyzer = new DefaultIncomeAnalyzer(personProfileLines);
			break;
		case 2:
			incomeAnalyzer = new PartioningIncomeAnalyzer(personProfileLines);
			break;
		case 3:
			incomeAnalyzer = new BroadcastHashJoinIncomeAnalyzer(personProfileLines);
			break;
		}

		if (incomeAnalyzer == null) {
			throw new IllegalArgumentException(
					"No suitable implementation for income analysis strategy: " + incomeAnalysisStgy);
		}

		return incomeAnalyzer;
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

package org.gb.sample.spark.income;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Main {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("Person profiles with top incomes");
		JavaSparkContext sc = new JavaSparkContext(conf);
		String nameVsIncomeFile = args[0];
		String personProfileFile = args[1];
		JavaRDD<String> nameVsIncomeLines = sc.textFile(nameVsIncomeFile);
		JavaRDD<String> personProfileLines = sc.textFile(personProfileFile);
		showTopIncomePersonProfiles(nameVsIncomeLines, personProfileLines);
		sc.close();
	}

	private static void showTopIncomePersonProfiles(JavaRDD<String> nameVsIncomeLines,
			JavaRDD<String> personProfileLines) {
		JavaPairRDD<PersonName, PersonProfile> nameVsProfilePairs = personProfileLines.map(Main::convertToCSVRecord)
				.filter(csvRecord -> csvRecord != null).mapToPair(Main::tuplizePersonNameVsPersonProfile);
		JavaPairRDD<PersonName, Integer> nameVsIncomePairs = nameVsIncomeLines.map(Main::convertToCSVRecord)
				.filter(csvRecord -> csvRecord != null).mapToPair(Main::tuplizePersonNameVsIncome);
		/*List<Tuple2<Integer, PersonProfile>> topIncomesWithPersonProfiles = nameVsIncomePairs.join(nameVsProfilePairs)
				.values().mapToPair(incomeVsProfile -> incomeVsProfile).sortByKey(false).take(10);*/
		Comparator<Tuple2<Integer, PersonProfile>> incomeComparator = new IncomeComparator();
		List<Tuple2<Integer, PersonProfile>> topIncomesWithPersonProfiles = nameVsIncomePairs.join(nameVsProfilePairs)
				.values().mapToPair(incomeVsProfile -> incomeVsProfile).takeOrdered(10, incomeComparator);
		topIncomesWithPersonProfiles.forEach(Main::printIncomeWithPersonProfiles);
	}

	private static CSVRecord convertToCSVRecord(String lineOfText) {
		CSVRecord csvRecord = null;
		try {
			List<CSVRecord> csvRecords = CSVParser.parse(lineOfText, CSVFormat.DEFAULT).getRecords();
			csvRecord = csvRecords.get(0);
		} catch (IOException e) {
			System.err.println(String.format("Could not convert lineOfText: %s to a CSVRecord.", lineOfText));
		}
		return csvRecord;
	}

	private static PersonProfile convertCSVRecordToPersProfile(CSVRecord csvRecord) {
		PersonProfile profile = new PersonProfile();

		profile.setFirstName(csvRecord.get(0));
		profile.setLastName(csvRecord.get(1));
		profile.setCompanyName(csvRecord.get(2));
		profile.setAddress(csvRecord.get(3));
		profile.setCity(csvRecord.get(4));
		profile.setCounty(csvRecord.get(5));
		profile.setPostCode(csvRecord.get(6));
		profile.setPhoneNumber1(csvRecord.get(7));
		profile.setPhoneNumber2(csvRecord.get(8));
		profile.setEmailAddress(csvRecord.get(9));
		profile.setWebsite(csvRecord.get(10));
		return profile;
	}

	private static Tuple2<PersonName, PersonProfile> tuplizePersonNameVsPersonProfile(CSVRecord csvRecord) {
		PersonName personName = new PersonName(csvRecord.get(0), csvRecord.get(1));
		PersonProfile personProfile = convertCSVRecordToPersProfile(csvRecord);
		return new Tuple2<PersonName, PersonProfile>(personName, personProfile);
	}

	private static Tuple2<PersonName, Integer> tuplizePersonNameVsIncome(CSVRecord csvRecord) {
		PersonName personName = new PersonName(csvRecord.get(0), csvRecord.get(1));
		Integer income = Integer.parseInt(csvRecord.get(2));
		return new Tuple2<PersonName, Integer>(personName, income);
	}

	private static void printIncomeWithPersonProfiles(Tuple2<Integer, PersonProfile> incomeVsPersonProfile) {
		PersonProfile personProfile = incomeVsPersonProfile._2();
		String fullName = String.join(" ", personProfile.getFirstName(), personProfile.getLastName());
		String personProfileAsString = String.join(", ", fullName, personProfile.getCompanyName(),
				personProfile.getEmailAddress());
		System.out.println(personProfileAsString + " <--> " + incomeVsPersonProfile._1());
	}
	
	private static class IncomeComparator implements Comparator<Tuple2<Integer, PersonProfile>>, Serializable {

		private static final long serialVersionUID = 5362417276584332919L;

		@Override
		public int compare(Tuple2<Integer, PersonProfile> incomeVsProfile1,
				Tuple2<Integer, PersonProfile> incomeVsProfile2) {
			return incomeVsProfile2._1().intValue() - incomeVsProfile1._1().intValue();
		}

	}

}

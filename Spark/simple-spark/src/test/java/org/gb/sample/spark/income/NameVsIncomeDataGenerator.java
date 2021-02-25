package org.gb.sample.spark.income;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class NameVsIncomeDataGenerator {

	private static final String DEFAULT_PERSON_PROFILE_FILENAME = "data/income/person-profile_50000.txt";
	private static final String DEFAULT_NAME_VS_INCOME_FILENAME = "data/income/name-vs-income_50000_2018.txt";

	public static void main(String[] args) {
		String personProfileFileName = DEFAULT_PERSON_PROFILE_FILENAME;
		String nameVsIncomeFileName = DEFAULT_NAME_VS_INCOME_FILENAME;

		if (args.length == 2) {
			personProfileFileName = args[0];
			nameVsIncomeFileName = args[1];
		}

		try {
			List<PersonProfile> personProfiles = loadPersonProfiles(personProfileFileName);
			boolean repeatedPersProfile = checkRepeatedPersProfile(personProfiles);

			if (repeatedPersProfile) {
				System.err.println("Some person profiles are repeated, resolve these and retry.");
				return;
			}

			generateNameVsIncomeRecords(personProfiles, nameVsIncomeFileName);
			System.out.printf("Created name-vs-income records for %d persons.", personProfiles.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateNameVsIncomeRecords(List<PersonProfile> personProfiles, String nameVsIncomeFileName)
			throws IOException {
		final int baseIncome = 20000;
		final int maxVarIncome = 280000;

		Path nameVsIncomeFilePath = Paths.get(nameVsIncomeFileName);
		PrintWriter writer = new PrintWriter(Files.newBufferedWriter(nameVsIncomeFilePath), true);

		Random randomIncomeGenerator = new Random();
		for (PersonProfile personProfile : personProfiles) {
			BigDecimal randomIncome = BigDecimal.valueOf(baseIncome + randomIncomeGenerator.nextInt(maxVarIncome));
			String nameVsIncomeRecordLine = String.join(",", personProfile.getFirstName(), personProfile.getLastName(),
					randomIncome.toString());
			writer.println(nameVsIncomeRecordLine);
		}

		writer.close();
	}

	private static CSVRecord convertToCSVRecord(String lineOfText) {
		CSVRecord csvRecord = null;
		try {
			List<CSVRecord> csvRecords = CSVParser.parse(lineOfText, CSVFormat.DEFAULT).getRecords();
			csvRecord = csvRecords.get(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return csvRecord;
	}

	private static PersonProfile convertLineToPersProfile(String persProfileLine) {
		CSVRecord persProfileRecord = convertToCSVRecord(persProfileLine);

		if (persProfileRecord == null) {
			return null;
		}

		PersonProfile profile = new PersonProfile();

		profile.setFirstName(persProfileRecord.get(0));
		profile.setLastName(persProfileRecord.get(1));
		profile.setCompanyName(persProfileRecord.get(2));
		profile.setAddress(persProfileRecord.get(3));
		profile.setCity(persProfileRecord.get(4));
		profile.setCounty(persProfileRecord.get(5));
		profile.setPostCode(persProfileRecord.get(6));
		profile.setPhoneNumber1(persProfileRecord.get(7));
		profile.setPhoneNumber2(persProfileRecord.get(8));
		profile.setEmailAddress(persProfileRecord.get(9));
		profile.setWebsite(persProfileRecord.get(10));
		/*
		 * System.out.println(String.format(
		 * "Parsed with commons-csv: first-name: %s, last-name: %s, company: %s, address: %s, city: %s, post-code: %s."
		 * , profile.getFirstName(), profile.getLastName(),
		 * profile.getCompanyName(), profile.getAddress(), profile.getCity(),
		 * profile.getPostCode()));
		 */
		return profile;
	}

	private static List<PersonProfile> loadPersonProfiles(String personProfileFileName) throws IOException {
		Path personProfileFilePath = Paths.get(personProfileFileName);
		return Files.lines(personProfileFilePath).map(NameVsIncomeDataGenerator::convertLineToPersProfile)
				.collect(Collectors.toList());
	}

	private static boolean checkRepeatedPersProfile(List<PersonProfile> personProfiles) {
		Collector<PersonName, ?, Map<PersonName, Long>> personNameVsCountCollector = Collectors
				.groupingBy(Function.identity(), Collectors.counting());
		Map<PersonName, Long> personNameVsCount = personProfiles.stream()
				.map(persProfile -> new PersonName(persProfile.getFirstName(), persProfile.getLastName()))
				.collect(personNameVsCountCollector);

		boolean repeatedPersProfile = false;
		int totalRepeatedRecords = 0;
		Set<Entry<PersonName, Long>> personNameVsCountEntries = personNameVsCount.entrySet();

		for (Entry<PersonName, Long> entry : personNameVsCountEntries) {
			PersonName personName = entry.getKey();
			Long count = entry.getValue();
			if (count.intValue() > 1) {
				totalRepeatedRecords = totalRepeatedRecords + count.intValue();
				repeatedPersProfile = true;
				System.out.println(String.format("%s %s appear %d times.", personName.getFirstPart(),
						personName.getLastPart(), count.intValue()));
			}
		}
		System.err.println(String.format("%d records need correction.", totalRepeatedRecords));
		return repeatedPersProfile;
	}
}

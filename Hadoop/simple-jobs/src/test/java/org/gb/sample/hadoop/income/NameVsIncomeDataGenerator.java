package org.gb.sample.hadoop.income;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class NameVsIncomeDataGenerator {

	public static void main(String[] args) {
		final String personProfileFileName = "input/income/person-profile.txt";
		final String nameVsIncomeFileName = "input/income/name-vs-income.txt";
		try {
			List<PersonProfile> personProfiles = loadPersonProfiles(personProfileFileName);
			generateNameVsIncomeRecords(personProfiles, nameVsIncomeFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateNameVsIncomeRecords(List<PersonProfile> personProfiles, String nameVsIncomeFileName)
			throws IOException {
		final int baseIncome = 20000;
		final int maxVarIncome = 60000;

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

	private static PersonProfile convertLineToPersProfile(String persProfileLine) {
		String[] profileValues = persProfileLine.split(",");

		PersonProfile profile = new PersonProfile();
		profile.setFirstName(profileValues[0].replace("\"", ""));
		profile.setLastName(profileValues[1].replace("\"", ""));
		profile.setCompanyName(profileValues[2].replace("\"", ""));
		profile.setAddress(profileValues[3].replace("\"", ""));
		profile.setCity(profileValues[4].replace("\"", ""));
		profile.setCounty(profileValues[5].replace("\"", ""));
		profile.setPostCode(profileValues[6].replace("\"", ""));
		profile.setPhoneNumber1(profileValues[7].replace("\"", ""));
		profile.setPhoneNumber2(profileValues[8].replace("\"", ""));
		profile.setEmailAddress(profileValues[9].replace("\"", ""));
		profile.setWebsite(profileValues[10].replace("\"", ""));

		return profile;
	}

	private static List<PersonProfile> loadPersonProfiles(String personProfileFileName) throws IOException {
		Path personProfileFilePath = Paths.get(personProfileFileName);
		return Files.lines(personProfileFilePath).map(NameVsIncomeDataGenerator::convertLineToPersProfile)
				.collect(Collectors.toList());
	}

}

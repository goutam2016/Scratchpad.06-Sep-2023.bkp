package org.gb.sample.hadoop.income;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class PersonProfileDataCleaner {

	private static final String DEFAULT_PERSON_PROFILE_FILENAME = "input/income/person-profile.txt";
	private static final String DEFAULT_PERSON_PROFILE_CLEANED_FILENAME = "input/income/person-profile-cleaned.txt";

	public static void main(String[] args) {
		String personProfileFileName = DEFAULT_PERSON_PROFILE_FILENAME;
		String personProfileCleanedFileName = DEFAULT_PERSON_PROFILE_CLEANED_FILENAME;

		if (args.length == 2) {
			personProfileFileName = args[0];
			personProfileCleanedFileName = args[1];
		}

		try {
			List<PersonProfile> personProfiles = loadPersonProfiles(personProfileFileName);
			Map<PersonName, Integer> repeatedPersonNames = findRepeatedPersonNames(personProfiles);

			if (repeatedPersonNames.isEmpty()) {
				System.out.println("None of the person names has been repeated, no need to generate cleaned file.");
				return;
			}

			createCleanedPersonProfileRecords(personProfiles, repeatedPersonNames, personProfileCleanedFileName);
			System.out.println(String.format("Created %d cleaned records.", personProfiles.size()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createCleanedPersonProfileRecords(List<PersonProfile> personProfiles,
			Map<PersonName, Integer> repeatedPersonNames, String personProfileCleanedFileName) throws IOException {
		Map<PersonName, Integer> repeatedPersonNamesVsCounter = new HashMap<>(repeatedPersonNames);
		Path personProfileCleanedFilePath = Paths.get(personProfileCleanedFileName);
		PrintWriter writer = new PrintWriter(Files.newBufferedWriter(personProfileCleanedFilePath), true);
		CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

		for (PersonProfile personProfile : personProfiles) {
			String firstName = personProfile.getFirstName();
			String lastName = personProfile.getLastName();
			PersonName personName = new PersonName(personProfile.getFirstName(), personProfile.getLastName());

			if (repeatedPersonNames.containsKey(personName)) {
				int noOfRepetitions = repeatedPersonNames.get(personName).intValue();
				int counter = repeatedPersonNamesVsCounter.get(personName).intValue();
				int repeatedPersonNameIdx = noOfRepetitions - counter + 1;
				repeatedPersonNamesVsCounter.put(personName, counter - 1);
				firstName = firstName.concat("-").concat(String.valueOf(repeatedPersonNameIdx));
				lastName = lastName.concat("-").concat(String.valueOf(repeatedPersonNameIdx));

				personProfile.setFirstName(firstName);
				personProfile.setLastName(lastName);
			}

			csvPrinter.printRecord(personProfile.getFirstName(), personProfile.getLastName(),
					personProfile.getCompanyName(), personProfile.getAddress(), personProfile.getCity(),
					personProfile.getCounty(), personProfile.getPostCode(), personProfile.getPhoneNumber1(),
					personProfile.getPhoneNumber2(), personProfile.getEmailAddress(), personProfile.getWebsite());
		}

		csvPrinter.close();
		writer.close();
	}

	private static List<PersonProfile> loadPersonProfiles(String personProfileFileName) throws IOException {
		Path personProfileFilePath = Paths.get(personProfileFileName);
		return Files.lines(personProfileFilePath).map(PersonProfileDataCleaner::convertLineToPersProfile)
				.collect(Collectors.toList());
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

	private static Map<PersonName, Integer> findRepeatedPersonNames(List<PersonProfile> personProfiles) {
		Collector<PersonName, ?, Map<PersonName, Long>> personNameVsCountCollector = Collectors
				.groupingBy(Function.identity(), Collectors.counting());
		Map<PersonName, Long> personNameVsCount = personProfiles.stream()
				.map(persProfile -> new PersonName(persProfile.getFirstName(), persProfile.getLastName()))
				.collect(personNameVsCountCollector);

		int repeatedRecordCount = 0;
		Set<Entry<PersonName, Long>> personNameVsCountEntries = personNameVsCount.entrySet();
		Map<PersonName, Integer> repeatedPersonNameVsCount = new HashMap<>();

		for (Entry<PersonName, Long> entry : personNameVsCountEntries) {
			PersonName personName = entry.getKey();
			Long count = entry.getValue();
			if (count.intValue() > 1) {
				repeatedPersonNameVsCount.put(personName, count.intValue());
				repeatedRecordCount++;
				System.out.println(String.format("%s %s appear %d times.", personName.getFirstPart(),
						personName.getLastPart(), count.intValue()));
			}
		}
		System.err.println(String.format("%d records have 1 or more repetition.", repeatedRecordCount));
		return repeatedPersonNameVsCount;
	}

}

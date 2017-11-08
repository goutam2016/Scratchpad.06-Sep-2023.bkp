package org.gb.sample.pig.income;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.util.UDFContext;

public class PersonProfileResolver extends EvalFunc<String> {

	private String personProfileFileName;
	private Map<PersonName, PersonProfile> personNameVsProfile;

	public PersonProfileResolver(String personProfileFileName) {
		super();
		this.personProfileFileName = personProfileFileName;
		this.personNameVsProfile = null;
	}

	@Override
	public String exec(Tuple tuple) throws IOException {
		System.out.println("Inside PersonProfileResolver, tuple: " + tuple + ", tuple.size: " + tuple.size());

		if (personNameVsProfile == null) {
			populatePersonNameVsProfile();
		}

		String firstName = (String) tuple.get(0);
		String lastName = (String) tuple.get(1);
		PersonName personName = new PersonName(firstName, lastName);
		PersonProfile personProfile = personNameVsProfile.get(personName);
		StringBuilder sb = new StringBuilder(personProfile.getFirstName()).append(" ")
				.append(personProfile.getLastName()).append(", ").append(personProfile.getCompanyName()).append(", ")
				.append(personProfile.getCity()).append(", ").append(personProfile.getPostCode()).append(", ")
				.append(personProfile.getEmailAddress());
		System.out.println("Inside PersonProfileResolver, person-profile as string: " + sb.toString());
		return sb.toString();
	}

	private void populatePersonNameVsProfile() throws IOException {
		personNameVsProfile = new HashMap<>();
		FileSystem fs = FileSystem.get(UDFContext.getUDFContext().getJobConf());
		FSDataInputStream ips = fs.open(new Path(personProfileFileName));
		BufferedReader bfrd = new BufferedReader(new InputStreamReader(ips));

		while (true) {
			String line = bfrd.readLine();

			if (line == null) {
				break;
			}

			PersonProfile personProfile = convertLineToPersProfile(line);

			if (personProfile == null) {
				continue;
			}

			PersonName personName = new PersonName(personProfile.getFirstName(), personProfile.getLastName());
			personNameVsProfile.put(personName, personProfile);
		}

		bfrd.close();
		ips.close();
	}

	private PersonProfile convertLineToPersProfile(String persProfileLine) {
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
		 * "Inside PersonProfileResolver, Parsed with commons-csv: first-name: %s, company: %s, address: %s, city: %s, post-code: %s."
		 * , profile.getFirstName(), profile.getCompanyName(), profile.getAddress(),
		 * profile.getCity(), profile.getPostCode()));
		 */
		return profile;
	}

	private CSVRecord convertToCSVRecord(String lineOfText) {
		CSVRecord csvRecord = null;
		try {
			List<CSVRecord> csvRecords = CSVParser.parse(lineOfText, CSVFormat.DEFAULT).getRecords();
			csvRecord = csvRecords.get(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return csvRecord;
	}
}

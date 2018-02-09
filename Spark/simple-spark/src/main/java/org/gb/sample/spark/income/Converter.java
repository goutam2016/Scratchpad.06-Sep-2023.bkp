package org.gb.sample.spark.income;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public final class Converter implements Serializable {

	private static final long serialVersionUID = 7302045686512511581L;
	private static final Converter singleton = new Converter();
	
	private Converter() {
		
	}
	
	static Converter getInstance() {
		return singleton;
	}
		
	PersonProfile convertToPersProfile(String lineOfText) {
		CSVRecord csvRecord = convertToCSVRecord(lineOfText);
		
		if(csvRecord == null) {
			return null;
		}
		
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
	
	CSVRecord convertToCSVRecord(String lineOfText) {
		CSVRecord csvRecord = null;
		try {
			List<CSVRecord> csvRecords = CSVParser.parse(lineOfText, CSVFormat.DEFAULT).getRecords();
			csvRecord = csvRecords.get(0);
		} catch (IOException e) {
			System.err.println(String.format("Could not convert lineOfText: %s to a CSVRecord.", lineOfText));
		}
		return csvRecord;
	}
}

package org.gb.sample.hadoop.income;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PersonIncomeMapper2 extends Mapper<LongWritable, Text, IncomeWritable, PersonIncome> {

	private Map<PersonName, PersonProfile> personNameVsProfile;

	@Override
	protected void setup(Mapper<LongWritable, Text, IncomeWritable, PersonIncome>.Context context)
			throws IOException, InterruptedException {
		String personProfileFileName = context.getConfiguration().get("PERSON_PROFILE_METADATA");
		Path personProfilePath = FileSystems.getDefault().getPath(personProfileFileName);
		Collector<PersonProfile, ?, Map<PersonName, PersonProfile>> personNameVsProfileCollector = Collectors
				.toMap(profile -> new PersonName(profile.getFirstName(), profile.getLastName()), Function.identity());
		personNameVsProfile = Files.lines(personProfilePath).map(this::convertLineToPersProfile)
				.collect(personNameVsProfileCollector);
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
		/*System.out.println(String.format(
				"Parsed with commons-csv: first-name: %s, company: %s, address: %s, city: %s, post-code: %s.",
				profile.getFirstName(), profile.getCompanyName(), profile.getAddress(), profile.getCity(),
				profile.getPostCode()));*/
		return profile;
	}

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, IncomeWritable, PersonIncome>.Context context)
			throws IOException, InterruptedException {
		CSVRecord nameIncomeRecord = convertToCSVRecord(value.toString());

		if (nameIncomeRecord == null) {
			return;
		}

		PersonName name = new PersonName(nameIncomeRecord.get(0), nameIncomeRecord.get(1));
		PersonProfile profile = personNameVsProfile.get(name);

		PersonIncome personIncome = new PersonIncome();
		personIncome.setFirstName(name.getFirstPart());
		personIncome.setLastName(name.getLastPart());
		personIncome.setIncome(new BigDecimal(nameIncomeRecord.get(2)));
		personIncome.setCompanyName(profile.getCompanyName());
		personIncome.setEmailAddress(profile.getEmailAddress());

		IncomeWritable incomeWritable = new IncomeWritable(personIncome.getIncome());
		context.write(incomeWritable, personIncome);
	}
}

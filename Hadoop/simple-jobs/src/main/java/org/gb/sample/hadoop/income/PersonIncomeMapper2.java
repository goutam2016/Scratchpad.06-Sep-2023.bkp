package org.gb.sample.hadoop.income;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
		System.out.println("completed setup.");
	}

	private PersonProfile convertLineToPersProfile(String persProfileLine) {
		String[] profileValues = persProfileLine.replace("\"", "").split(",");

		PersonProfile profile = new PersonProfile();
		profile.setFirstName(profileValues[0]);
		profile.setLastName(profileValues[1]);
		profile.setCompanyName(profileValues[2]);
		profile.setAddress(profileValues[3]);
		profile.setCity(profileValues[4]);
		profile.setCounty(profileValues[5]);
		profile.setPostCode(profileValues[6]);
		profile.setPhoneNumber1(profileValues[7]);
		profile.setPhoneNumber2(profileValues[8]);
		profile.setEmailAddress(profileValues[9]);
		profile.setWebsite(profileValues[10]);

		return profile;
	}

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, IncomeWritable, PersonIncome>.Context context)
			throws IOException, InterruptedException {
		String incomeWithNameLine = value.toString().replace("\"", "");
		String[] parts = incomeWithNameLine.split(",");
		
		PersonName name = new PersonName(parts[0], parts[1]);
		PersonProfile profile = personNameVsProfile.get(name);

		PersonIncome personIncome = new PersonIncome();
		personIncome.setFirstName(name.getFirstPart());
		personIncome.setLastName(name.getLastPart());
		personIncome.setIncome(new BigDecimal(parts[2]));
		personIncome.setCompanyName(profile.getCompanyName());
		personIncome.setEmailAddress(profile.getEmailAddress());
		
		System.out.println("From map task, FirstName: " + personIncome.getFirstName() + ", Income: " + personIncome.getIncome());

		IncomeWritable incomeWritable = new IncomeWritable(personIncome.getIncome());
		context.write(incomeWritable, personIncome);
	}
}

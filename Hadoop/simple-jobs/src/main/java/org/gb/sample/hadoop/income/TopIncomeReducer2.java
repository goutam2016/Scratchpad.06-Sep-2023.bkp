package org.gb.sample.hadoop.income;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.mapreduce.Reducer;

public class TopIncomeReducer2 extends Reducer<IncomeWritable, PersonIncome, BigDecimal, BriefPersonProfile> {

	private Map<BigDecimal, List<BriefPersonProfile>> incomeVsPersProfiles;

	@Override
	protected void setup(Reducer<IncomeWritable, PersonIncome, BigDecimal, BriefPersonProfile>.Context context)
			throws IOException, InterruptedException {
		incomeVsPersProfiles = new LinkedHashMap<>();
	}

	private BriefPersonProfile createBriefPersProfile(PersonIncome personIncome) {
		BriefPersonProfile briefPersonProfile = new BriefPersonProfile();
		briefPersonProfile.setFullName(String.join(" ", personIncome.getFirstName(), personIncome.getLastName()));
		briefPersonProfile.setCompanyName(personIncome.getCompanyName());
		briefPersonProfile.setEmailAddress(personIncome.getEmailAddress());
		return briefPersonProfile;
	}

	@Override
	protected void reduce(IncomeWritable key, Iterable<PersonIncome> values,
			Reducer<IncomeWritable, PersonIncome, BigDecimal, BriefPersonProfile>.Context context)
			throws IOException, InterruptedException {

		if (incomeVsPersProfiles.size() > 10) {
			return;
		}

		List<BriefPersonProfile> briefPersonProfiles = new ArrayList<>();
		values.forEach(persIncome -> briefPersonProfiles.add(createBriefPersProfile(persIncome)));

		incomeVsPersProfiles.put(key.getIncome(), briefPersonProfiles);
	}

	private void writeIncomeWithPersProfile(BigDecimal income, List<BriefPersonProfile> briefPersonProfiles,
			Reducer<IncomeWritable, PersonIncome, BigDecimal, BriefPersonProfile>.Context context) {

		for (BriefPersonProfile briefPersonProfile : briefPersonProfiles) {
			try {
				context.write(income, briefPersonProfile);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void cleanup(Reducer<IncomeWritable, PersonIncome, BigDecimal, BriefPersonProfile>.Context context)
			throws IOException, InterruptedException {

		incomeVsPersProfiles.forEach(
				(income, briefPersonProfiles) -> writeIncomeWithPersProfile(income, briefPersonProfiles, context));
	}
}

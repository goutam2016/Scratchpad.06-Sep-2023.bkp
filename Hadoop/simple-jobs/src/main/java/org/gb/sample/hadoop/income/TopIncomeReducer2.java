package org.gb.sample.hadoop.income;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import org.apache.hadoop.mapreduce.Reducer;

public class TopIncomeReducer2 extends Reducer<IncomeWritable, PersonIncome, BigDecimal, BriefPersonProfile> {

	private PersonIncome copyPersonIncome(PersonIncome origPersIncome) {
		PersonIncome copiedPersIncome = new PersonIncome();
		copiedPersIncome.setFirstName(origPersIncome.getFirstName());
		copiedPersIncome.setLastName(origPersIncome.getLastName());
		copiedPersIncome.setIncome(origPersIncome.getIncome());
		copiedPersIncome.setCompanyName(origPersIncome.getCompanyName());
		copiedPersIncome.setEmailAddress(origPersIncome.getEmailAddress());
		return copiedPersIncome;
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

		Iterator<PersonIncome> itr = values.iterator();
		PersonIncome personIncome = null;

		if (itr.hasNext()) {
			personIncome = itr.next();
		}

		context.write(key.getIncome(), createBriefPersProfile(personIncome));
	}
}

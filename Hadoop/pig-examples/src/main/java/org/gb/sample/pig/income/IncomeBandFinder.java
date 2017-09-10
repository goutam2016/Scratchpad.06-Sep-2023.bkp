package org.gb.sample.pig.income;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class IncomeBandFinder extends EvalFunc<String> {

	@Override
	public String exec(Tuple tuple) throws IOException {
		String band = null;
		Integer income = (Integer) tuple.get(0);
		
		if(income.intValue() <= 30000) {
			band = "Below 30000";
		} else if (income.intValue() > 30000 && income.intValue() <= 50000) {
			band = "Over 30000 and upto 50000";
		} else {
			band = "Over 50000";
		}
		
		System.out.printf("Inside IncomeBandFinder, income: %d, band: %s.\n", income, band);
		return band;
	}

}

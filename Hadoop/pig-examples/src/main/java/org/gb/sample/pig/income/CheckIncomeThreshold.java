package org.gb.sample.pig.income;

import java.io.IOException;

import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;

public class CheckIncomeThreshold extends FilterFunc {

	@Override
	public Boolean exec(Tuple tuple) throws IOException {
		System.out.println("Inside CheckIncomeThreshold, tuple: " + tuple);
		if (tuple == null || tuple.size() != 2) {
			return false;
		} else if (tuple.get(0) == null) {
			return false;
		} else if (tuple.get(1) == null) {
			return false;
		} else {
			int incomeThreshold = 0;
			int income = 0;

			if (tuple.get(0) instanceof Integer) {
				incomeThreshold = ((Integer) tuple.get(0)).intValue();
			} else {
				return false;
			}
			if (tuple.get(1) instanceof Integer) {
				income = ((Integer) tuple.get(1)).intValue();
			} else {
				return false;
			}
			System.out.printf("Inside CheckIncomeThreshold, incomeThreshold: %d, income: %d.\n", incomeThreshold,
					income);
			return (income > incomeThreshold);
		}
	}
}

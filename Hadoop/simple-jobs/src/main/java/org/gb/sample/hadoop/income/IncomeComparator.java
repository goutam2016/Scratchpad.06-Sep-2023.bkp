package org.gb.sample.hadoop.income;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class IncomeComparator extends WritableComparator {

	public IncomeComparator() {
		super(IncomeWritable.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable firstIncome, WritableComparable secondIncome) {
		if(firstIncome instanceof IncomeWritable && secondIncome instanceof IncomeWritable) {
			return ((IncomeWritable)firstIncome).compareTo((IncomeWritable) secondIncome);
		}
		
		return super.compare(firstIncome, secondIncome);
	}
}

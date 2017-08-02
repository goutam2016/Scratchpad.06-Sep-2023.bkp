package org.gb.sample.hadoop.income;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.hadoop.io.WritableComparable;

public class IncomeWritable implements WritableComparable<IncomeWritable> {
	
	private BigDecimal income;
	
	IncomeWritable() {
		super();
	}
	IncomeWritable(BigDecimal income) {
		super();
		this.income = income;
	}
	
	BigDecimal getIncome() {
		return income;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(income.toString());
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		income = new BigDecimal(in.readUTF());
	}

	@Override
	public int compareTo(IncomeWritable other) {
		return -income.compareTo(other.income);
	}

}

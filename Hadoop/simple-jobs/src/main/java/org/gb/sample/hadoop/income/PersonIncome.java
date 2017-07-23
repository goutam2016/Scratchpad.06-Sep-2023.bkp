package org.gb.sample.hadoop.income;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.hadoop.io.Writable;

public class PersonIncome implements Writable {
	
	private String firstName;
	private String lastName;
	private BigDecimal income;
	private String companyName;
	private String emailAddress;

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub

	}

}

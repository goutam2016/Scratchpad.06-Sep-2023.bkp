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
	
	String getFirstName() {
		return firstName;
	}
	void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	String getLastName() {
		return lastName;
	}
	void setLastName(String lastName) {
		this.lastName = lastName;
	}

	BigDecimal getIncome() {
		return income;
	}
	void setIncome(BigDecimal income) {
		this.income = income;
	}

	String getCompanyName() {
		return companyName;
	}
	void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	String getEmailAddress() {
		return emailAddress;
	}
	void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(firstName);
		out.writeUTF(lastName);
		out.writeUTF(income.toString());
		out.writeUTF(companyName);
		out.writeUTF(emailAddress);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		firstName = in.readUTF();
		lastName = in.readUTF();
		income = new BigDecimal(in.readUTF());
		companyName = in.readUTF();
		emailAddress = in.readUTF();
	}

}

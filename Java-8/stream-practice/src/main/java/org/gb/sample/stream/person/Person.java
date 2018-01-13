package org.gb.sample.stream.person;

import java.util.Date;

public abstract class Person {

	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String residenceAddress;
	private String motherTongue;
	
	Person(String firstName, String lastName, String motherTongue) {
		this(firstName, lastName, null, null, motherTongue);
	}
	
	Person(String firstName, String lastName, Date dateOfBirth, String residenceAddress, String motherTongue) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.residenceAddress = residenceAddress;
		this.motherTongue = motherTongue;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getResidenceAddress() {
		return residenceAddress;
	}
	public void setResidenceAddress(String residenceAddress) {
		this.residenceAddress = residenceAddress;
	}
	public String getMotherTongue() {
		return motherTongue;
	}
	public void setMotherTongue(String motherTongue) {
		this.motherTongue = motherTongue;
	}
}

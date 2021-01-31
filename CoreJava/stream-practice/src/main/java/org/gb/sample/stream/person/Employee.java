package org.gb.sample.stream.person;

import java.math.BigDecimal;
import java.util.Date;

public class Employee extends Person {

	enum Sex {
		MALE, FEMALE
	}

	private Sex sex;
	private Date joiningDate;
	private String jobTitle;
	private String department;
	private String highestAcademicQlfn;
	private BigDecimal yearlyGrossSalary;

	Employee(String firstName, String lastName, String motherTongue, Sex sex, Date joiningDate, String jobTitle) {
		super(firstName, lastName, motherTongue);
		this.sex = sex;
		this.joiningDate = joiningDate;
		this.jobTitle = jobTitle;
	}

	Employee(String firstName, String lastName, String motherTongue, Sex sex, Date joiningDate, String jobTitle,
			String department, BigDecimal yearlyGrossSalary) {
		this(firstName, lastName, motherTongue, sex, joiningDate, jobTitle);
		this.department = department;
		this.yearlyGrossSalary = yearlyGrossSalary;
	}

	Employee(String firstName, String lastName, Date dateOfBirth, String residenceAddress, String motherTongue,
			Sex sex, Date joiningDate, String jobTitle, String department, String highestAcademicQlfn,
			BigDecimal yearlyGrossSalary) {
		super(firstName, lastName, dateOfBirth, residenceAddress, motherTongue);
		this.sex = sex;
		this.joiningDate = joiningDate;
		this.jobTitle = jobTitle;
		this.department = department;
		this.highestAcademicQlfn = highestAcademicQlfn;
		this.yearlyGrossSalary = yearlyGrossSalary;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getHighestAcademicQlfn() {
		return highestAcademicQlfn;
	}

	public void setHighestAcademicQlfn(String highestAcademicQlfn) {
		this.highestAcademicQlfn = highestAcademicQlfn;
	}

	public BigDecimal getYearlyGrossSalary() {
		return yearlyGrossSalary;
	}

	public void setYearlyGrossSalary(BigDecimal yearlyGrossSalary) {
		this.yearlyGrossSalary = yearlyGrossSalary;
	}
}

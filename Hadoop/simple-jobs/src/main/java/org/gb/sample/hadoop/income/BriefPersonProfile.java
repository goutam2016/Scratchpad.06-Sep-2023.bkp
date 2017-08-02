package org.gb.sample.hadoop.income;

public class BriefPersonProfile {

	private String fullName;
	private String companyName;
	private String emailAddress;

	String getFullName() {
		return fullName;
	}
	void setFullName(String fullName) {
		this.fullName = fullName;
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
	public String toString() {
		String stringRepresentation = String.join(", ", fullName, companyName, emailAddress);
		return stringRepresentation;
	}
}

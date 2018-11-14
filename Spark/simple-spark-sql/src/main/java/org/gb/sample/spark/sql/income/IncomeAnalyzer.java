package org.gb.sample.spark.sql.income;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;

class IncomeAnalyzer {

	private Dataset<Row> nameVsIncomeRows;
	private Dataset<Row> personProfileRows;

	IncomeAnalyzer(Dataset<Row> nameVsIncomeRows, Dataset<Row> personProfileRows) {
		this.nameVsIncomeRows = nameVsIncomeRows.toDF("firstName", "lastName", "income");
		this.personProfileRows = personProfileRows.toDF("firstName", "lastName", "companyName", "address", "city",
				"county", "postCode", "phoneNumber1", "phoneNumber2", "emailAddress", "website");
	}

	Map<Integer, List<PersonProfile>> getTopIncomePersonProfiles(int topNum) {
		Column joinOnFirstName = nameVsIncomeRows.col("firstName").equalTo(personProfileRows.col("firstName"));
		Column joinOnLastName = nameVsIncomeRows.col("lastName").equalTo(personProfileRows.col("lastName"));
		Dataset<Row> joined = nameVsIncomeRows.join(personProfileRows, joinOnFirstName.and(joinOnLastName));
		List<Row> topIncomeRows = joined.orderBy(joined.col("income").cast(DataTypes.IntegerType).desc())
				.takeAsList(topNum);
		Map<Integer, List<PersonProfile>> topIncomeVsPersProfiles = topIncomeRows.stream()
				.collect(Collectors.groupingBy(this::getIncome, LinkedHashMap::new,
						Collectors.mapping(this::convertToPersProfile, Collectors.toList())));
		return topIncomeVsPersProfiles;
	}

	private Integer getIncome(Row row) {
		return Integer.parseInt(row.getAs("income"));
	}

	private PersonProfile convertToPersProfile(Row row) {
		PersonProfile profile = new PersonProfile();
		profile.setFirstName(row.getAs("firstName"));
		profile.setLastName(row.getAs("lastName"));
		profile.setCompanyName(row.getAs("companyName"));
		profile.setAddress(row.getAs("address"));
		profile.setCity(row.getAs("city"));
		profile.setCounty(row.getAs("county"));
		profile.setPostCode(row.getAs("postCode"));
		profile.setPhoneNumber1(row.getAs("phoneNumber1"));
		profile.setPhoneNumber2(row.getAs("phoneNumber2"));
		profile.setEmailAddress(row.getAs("emailAddress"));
		profile.setWebsite(row.getAs("website"));
		return profile;
	}
}
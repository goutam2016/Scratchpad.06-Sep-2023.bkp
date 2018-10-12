package org.gb.sample.spark.sql.income;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.gb.sample.spark.sql.income.PersonName;
import org.gb.sample.spark.sql.income.PersonProfile;

public class TopIncomesMain {

	public static void main(String[] args) {
        String nameVsIncomeFile = args[0];
        String personProfileFile = args[1];




        long start = System.currentTimeMillis();
        System.out.println("start >>> : " + start);

        SparkSession spark = SparkSession
                .builder()
                .appName("Person profiles with top incomes")
                //.master("local[2]")
                .getOrCreate();

        Dataset<PersonName> incomes = spark.read()
                .csv(nameVsIncomeFile)
                .toDF("firstName",
                        "lastName",
                        "salary")
                .as(Encoders.bean(PersonName.class));

        Dataset<PersonProfile> profiles = spark.read()
                .csv(personProfileFile)
                .toDF("firstName",
                        "lastName",
                        "companyName",
                        "address",
                        "city",
                        "county",
                        "postCode",
                        "phoneNumber1",
                        "phoneNumber2",
                        "emailAddress",
                        "website")
                .as(Encoders.bean(PersonProfile.class));

        incomeAnalyzer(incomes, profiles);

        spark.close();
        System.out.println("end >>> : " + (System.currentTimeMillis() - start) / 1_000);
    }

    private static void incomeAnalyzer(Dataset<PersonName> nameVsIncomeLines, Dataset<PersonProfile> personProfileLines) {
        nameVsIncomeLines
                .join(personProfileLines.alias("profile"),
                        nameVsIncomeLines.col("lastName").equalTo(personProfileLines.col("lastName"))
                        .and(
                                nameVsIncomeLines.col("firstName").equalTo(personProfileLines.col("firstName"))
                        )
                )
                .orderBy(nameVsIncomeLines.col("lastName"), nameVsIncomeLines.col("firstName"))
                .limit(3)
//                .selectExpr("profile.*")
                .show()
                ;

//		dt.foreach(System.out::println);
    }

}

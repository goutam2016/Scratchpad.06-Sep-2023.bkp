package org.gb.sample.spark.income;

import java.io.Serializable;

import org.apache.commons.csv.CSVRecord;
import org.apache.spark.HashPartitioner;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

public abstract class AbstractIncomeAnalyzer implements IncomeAnalyzer, Serializable {
	
	private static final long serialVersionUID = 5828505003700563977L;
	private final boolean usePartitioning;
	final Converter converter;
	final JavaPairRDD<PersonName, PersonProfile> nameVsProfilePairs;
	
	AbstractIncomeAnalyzer(JavaRDD<String> personProfileLines, boolean usePartitioning) {
		this.usePartitioning = usePartitioning;
		this.converter = Converter.getInstance();
		JavaPairRDD<PersonName, PersonProfile> nameVsProfileTuples = personProfileLines
				.map(converter::convertToPersProfile).filter(profile -> profile != null)
				.mapToPair(this::tuplizePersonNameVsPersonProfile);

		if (usePartitioning) {
			nameVsProfilePairs = nameVsProfileTuples.partitionBy(new HashPartitioner(12)).cache();
		} else {
			this.nameVsProfilePairs = nameVsProfileTuples;
		}
	}
	
	private Tuple2<PersonName, PersonProfile> tuplizePersonNameVsPersonProfile(PersonProfile personProfile) {
		PersonName personName = new PersonName(personProfile.getFirstName(), personProfile.getLastName());
		return new Tuple2<PersonName, PersonProfile>(personName, personProfile);
	}

	final Tuple2<PersonName, Integer> tuplizePersonNameVsIncome(CSVRecord csvRecord) {
		PersonName personName = new PersonName(csvRecord.get(0), csvRecord.get(1));
		Integer income = Integer.parseInt(csvRecord.get(2));
		return new Tuple2<PersonName, Integer>(personName, income);
	}

	@Override
	public void cleanup() {
		if (usePartitioning) {
			nameVsProfilePairs.unpersist();
		}
	}

}

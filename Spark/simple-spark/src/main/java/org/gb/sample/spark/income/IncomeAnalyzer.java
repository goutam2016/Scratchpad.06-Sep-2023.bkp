package org.gb.sample.spark.income;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVRecord;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

public class IncomeAnalyzer implements Serializable {

	private static final long serialVersionUID = -9024791218564168820L;

	private JavaPairRDD<PersonName, PersonProfile> nameVsProfilePairs;
	private JavaPairRDD<PersonName, Integer> nameVsIncomePairs;

	IncomeAnalyzer(JavaRDD<String> nameVsIncomeLines, JavaRDD<String> personProfileLines) {
		Converter converter = Converter.getInstance();
		nameVsProfilePairs = personProfileLines.map(converter::convertToPersProfile).filter(profile -> profile != null)
				.mapToPair(this::tuplizePersonNameVsPersonProfile);
		nameVsIncomePairs = nameVsIncomeLines.map(converter::convertToCSVRecord).filter(csvRecord -> csvRecord != null)
				.mapToPair(this::tuplizePersonNameVsIncome);
	}

	private Tuple2<PersonName, PersonProfile> tuplizePersonNameVsPersonProfile(PersonProfile personProfile) {
		PersonName personName = new PersonName(personProfile.getFirstName(), personProfile.getLastName());
		return new Tuple2<PersonName, PersonProfile>(personName, personProfile);
	}

	private Tuple2<PersonName, Integer> tuplizePersonNameVsIncome(CSVRecord csvRecord) {
		PersonName personName = new PersonName(csvRecord.get(0), csvRecord.get(1));
		Integer income = Integer.parseInt(csvRecord.get(2));
		return new Tuple2<PersonName, Integer>(personName, income);
	}

	Map<Integer, List<PersonProfile>> getTopIncomePersonProfiles(int topNum) {
		Comparator<Tuple2<Integer, PersonProfile>> incomeComparator = new IncomeComparator();
		List<Tuple2<Integer, PersonProfile>> incomeVsPersProfileTuples = nameVsIncomePairs.join(nameVsProfilePairs)
				.values().mapToPair(incomeVsProfile -> incomeVsProfile).takeOrdered(topNum, incomeComparator);
		Collector<Tuple2<Integer, PersonProfile>, ?, Map<Integer, List<PersonProfile>>> incomeVsPersProfilesCollector = Collectors
				.groupingBy(Tuple2::_1, LinkedHashMap::new, Collectors.mapping(Tuple2::_2, Collectors.toList()));
		return incomeVsPersProfileTuples.stream().collect(incomeVsPersProfilesCollector);
	}

	private static class IncomeComparator implements Comparator<Tuple2<Integer, PersonProfile>>, Serializable {

		private static final long serialVersionUID = -8645615871089128763L;

		@Override
		public int compare(Tuple2<Integer, PersonProfile> incomeVsProfile1,
				Tuple2<Integer, PersonProfile> incomeVsProfile2) {
			return incomeVsProfile2._1().intValue() - incomeVsProfile1._1().intValue();
		}
	}

}

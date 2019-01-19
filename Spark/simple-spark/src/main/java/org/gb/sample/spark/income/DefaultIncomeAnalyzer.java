package org.gb.sample.spark.income;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

public class DefaultIncomeAnalyzer extends AbstractIncomeAnalyzer {

	private static final long serialVersionUID = 2561789023299390019L;

	DefaultIncomeAnalyzer(JavaRDD<String> personProfileLines) {
		super(personProfileLines, false);
	}

	DefaultIncomeAnalyzer(JavaRDD<String> personProfileLines, boolean usePartitioning) {
		super(personProfileLines, usePartitioning);
	}

	@Override
	public Map<Integer, List<PersonProfile>> getTopIncomePersonProfiles(JavaRDD<String> nameVsIncomeLines, int topNum) {
		Comparator<Tuple2<Integer, PersonProfile>> incomeComparator = new IncomeComparator();
		JavaPairRDD<PersonName, Integer> nameVsIncomePairs = nameVsIncomeLines.map(converter::convertToCSVRecord)
				.filter(csvRecord -> csvRecord != null).mapToPair(this::tuplizePersonNameVsIncome);
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

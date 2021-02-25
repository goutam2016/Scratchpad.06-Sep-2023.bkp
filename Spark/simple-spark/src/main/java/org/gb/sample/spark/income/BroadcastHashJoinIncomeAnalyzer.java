package org.gb.sample.spark.income;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.broadcast.Broadcast;

import scala.Tuple2;

public class BroadcastHashJoinIncomeAnalyzer extends AbstractIncomeAnalyzer {

	private static final long serialVersionUID = 7532122133153276549L;

	BroadcastHashJoinIncomeAnalyzer(JavaRDD<String> personProfileLines) {
		super(personProfileLines, true);
	}

	@Override
	public Map<Integer, List<PersonProfile>> getTopIncomePersonProfiles(JavaRDD<String> nameVsIncomeLines, int topNum) {
		Comparator<Tuple2<PersonName, Integer>> incomeComparator = new IncomeComparator();
		JavaPairRDD<PersonName, Integer> nameVsIncomePairs = nameVsIncomeLines.map(converter::convertToCSVRecord)
				.filter(csvRecord -> csvRecord != null).mapToPair(this::tuplizePersonNameVsIncome);
		List<Tuple2<PersonName, Integer>> nameVsIncomeTuples = nameVsIncomePairs.top(topNum, incomeComparator);
		Map<PersonName, Integer> nameVsIncome = nameVsIncomeTuples.stream()
				.collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));

		@SuppressWarnings("resource")
		JavaSparkContext sparkContext = new JavaSparkContext(nameVsIncomePairs.context());
		Broadcast<Map<PersonName, Integer>> bdcstNameVsIncome = sparkContext.broadcast(nameVsIncome);

		PairFlatMapFunction<Iterator<Tuple2<PersonName, PersonProfile>>, Integer, PersonProfile> incomeVsPersProfileMapper = (
				nameVsPersProfileTuples) -> mapToIncomeVsPersProfileTuples(bdcstNameVsIncome, nameVsPersProfileTuples);
		JavaPairRDD<Integer, PersonProfile> incomeVsPersProfilePairs = nameVsProfilePairs
				.mapPartitionsToPair(incomeVsPersProfileMapper);
		List<Tuple2<Integer, PersonProfile>> incomeVsPersProfileTuples = incomeVsPersProfilePairs.collect();
		Collector<Tuple2<Integer, PersonProfile>, ?, Map<Integer, List<PersonProfile>>> incomeVsPersProfilesCollector = Collectors
				.groupingBy(Tuple2::_1, LinkedHashMap::new, Collectors.mapping(Tuple2::_2, Collectors.toList()));
		Comparator<Tuple2<Integer, PersonProfile>> incomeComparator2 = Comparator.comparing(Tuple2::_1);
		return incomeVsPersProfileTuples.stream().sorted(incomeComparator2.reversed()).collect(incomeVsPersProfilesCollector);
	}

	private Iterator<Tuple2<Integer, PersonProfile>> mapToIncomeVsPersProfileTuples(
			Broadcast<Map<PersonName, Integer>> bdcstNameVsIncome,
			Iterator<Tuple2<PersonName, PersonProfile>> nameVsPersProfileTuples) {
		List<Tuple2<Integer, PersonProfile>> incomeVsPersProfileTuples = new ArrayList<>();
		nameVsPersProfileTuples.forEachRemaining(nameVsPersProfileTuple -> addIfExists(nameVsPersProfileTuple,
				bdcstNameVsIncome.value(), incomeVsPersProfileTuples));
		return incomeVsPersProfileTuples.iterator();
	}

	private void addIfExists(Tuple2<PersonName, PersonProfile> nameVsPersProfileTuple,
			Map<PersonName, Integer> nameVsIncome, List<Tuple2<Integer, PersonProfile>> incomeVsPersProfileTuples) {
		PersonName name = nameVsPersProfileTuple._1();
		PersonProfile profile = nameVsPersProfileTuple._2();

		if (nameVsIncome.containsKey(name)) {
			Integer income = nameVsIncome.get(name);
			incomeVsPersProfileTuples.add(new Tuple2<Integer, PersonProfile>(income, profile));
		}
	}

	private static class IncomeComparator implements Comparator<Tuple2<PersonName, Integer>>, Serializable {

		private static final long serialVersionUID = -2069717995240429079L;

		@Override
		public int compare(Tuple2<PersonName, Integer> nameVsIncome1, Tuple2<PersonName, Integer> nameVsIncome2) {
			return nameVsIncome1._2().intValue() - nameVsIncome2._2().intValue();
		}
	}

}

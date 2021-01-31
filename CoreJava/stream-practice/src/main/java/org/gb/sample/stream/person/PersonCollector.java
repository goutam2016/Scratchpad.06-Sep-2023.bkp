package org.gb.sample.stream.person;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class PersonCollector implements Collector<Person, PersonAccumulator, Map<String, List<Person>>> {

	@Override
	public Supplier<PersonAccumulator> supplier() {
		return PersonAccumulator::new;
	}

	@Override
	public BiConsumer<PersonAccumulator, Person> accumulator() {
		return PersonAccumulator::mapByMotherTongue;
	}

	@Override
	public BinaryOperator<PersonAccumulator> combiner() {
		return PersonAccumulator::merge;
	}

	@Override
	public Function<PersonAccumulator, Map<String, List<Person>>> finisher() {
		return PersonAccumulator::getPeopleByMotherTongue;
	}

	@Override
	public Set<java.util.stream.Collector.Characteristics> characteristics() {
		Set<Collector.Characteristics> characteristicsSet = new HashSet<>();
		//characteristicsSet.add(Collector.Characteristics.IDENTITY_FINISH);
		return characteristicsSet;
	}
}

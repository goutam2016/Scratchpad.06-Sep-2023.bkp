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

public class PersonCollector2 implements Collector<Person, PersonAccumulator2, Map<String, List<Person>>> {

	private Function<Person, String> classifier;

	public PersonCollector2(Function<Person, String> classifier) {
		this.classifier = classifier;
	}

	@Override
	public Supplier<PersonAccumulator2> supplier() {
		return () -> new PersonAccumulator2(classifier);
	}

	@Override
	public BiConsumer<PersonAccumulator2, Person> accumulator() {
		return PersonAccumulator2::mapByStringProperty;
	}

	@Override
	public BinaryOperator<PersonAccumulator2> combiner() {
		return PersonAccumulator2::merge;
	}

	@Override
	public Function<PersonAccumulator2, Map<String, List<Person>>> finisher() {
		return PersonAccumulator2::getPeopleByStringProperty;
	}

	@Override
	public Set<java.util.stream.Collector.Characteristics> characteristics() {
		Set<Collector.Characteristics> characteristicsSet = new HashSet<>();
		// characteristicsSet.add(Collector.Characteristics.IDENTITY_FINISH);
		return characteristicsSet;
	}
}

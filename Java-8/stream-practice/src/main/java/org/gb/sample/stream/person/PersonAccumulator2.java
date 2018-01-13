package org.gb.sample.stream.person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PersonAccumulator2 {

	private Function<Person, String> classifier;
	private Map<String, List<Person>> peopleByStringProperty;

	public PersonAccumulator2(Function<Person, String> classifier) {
		this.classifier = classifier;
		peopleByStringProperty = new HashMap<>();
	}

	public void mapByStringProperty(Person person) {
		String stringProperty = classifier.apply(person);
		
		BiFunction<List<Person>, List<Person>, List<Person>> remappingFunction = (List<Person> selectedPeoplePart1,
				List<Person> selectedPeoplePart2) -> {
			selectedPeoplePart1.addAll(selectedPeoplePart2);
			return selectedPeoplePart1;
		};
		peopleByStringProperty.merge(stringProperty, new ArrayList<>(Arrays.asList(person)), remappingFunction);		
	}

	public PersonAccumulator2 merge(PersonAccumulator2 otherAccumulator) {
		if (otherAccumulator.getPeopleByStringProperty() == null) {
			return this;
		}

		BiFunction<List<Person>, List<Person>, List<Person>> remappingFunction = (List<Person> selectedPeoplePart1,
				List<Person> selectedPeoplePart2) -> {
			selectedPeoplePart1.addAll(selectedPeoplePart2);
			return selectedPeoplePart1;
		};
		BiConsumer<String, List<Person>> biConsumer = (String stringProperty, List<Person> selectedPeople) -> peopleByStringProperty
				.merge(stringProperty, selectedPeople, remappingFunction);
		otherAccumulator.getPeopleByStringProperty().forEach(biConsumer);

		return this;
	}

	public Map<String, List<Person>> getPeopleByStringProperty() {
		return peopleByStringProperty;
	}
}

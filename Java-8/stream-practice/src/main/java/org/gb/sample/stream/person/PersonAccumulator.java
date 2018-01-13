package org.gb.sample.stream.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class PersonAccumulator {

	private Map<String, List<Person>> peopleByMotherTongue;
	
	public PersonAccumulator() {
		peopleByMotherTongue = new HashMap<>();
	}
	
	public void mapByMotherTongue(Person person) {
		String motherTongue = person.getMotherTongue();
		List<Person> selectedPeople = peopleByMotherTongue.get(motherTongue);
		
		if(selectedPeople == null) {
			selectedPeople = new ArrayList<>();
			selectedPeople.add(person);
			peopleByMotherTongue.put(motherTongue, selectedPeople);
		} else {
			selectedPeople.add(person);
		}
	}
	
	public PersonAccumulator merge(PersonAccumulator otherAccumulator) {
		if(otherAccumulator.getPeopleByMotherTongue() == null) {
			return this;
		}
		
		BiConsumer<String, List<Person>> biConsumer = (String motherTongue, List<Person> selectedPeople) -> {
			List<Person> alreadySelectedPeople = peopleByMotherTongue.get(motherTongue);
			if(alreadySelectedPeople == null) {
				List<Person> newlySelectedPeople = new ArrayList<>(selectedPeople);
				peopleByMotherTongue.put(motherTongue, newlySelectedPeople);
			} else {
				alreadySelectedPeople.addAll(selectedPeople);
			}
		};
		otherAccumulator.getPeopleByMotherTongue().forEach(biConsumer);
		
		return this;
	}
	
	public Map<String, List<Person>> getPeopleByMotherTongue() {
		return peopleByMotherTongue;
	}
}

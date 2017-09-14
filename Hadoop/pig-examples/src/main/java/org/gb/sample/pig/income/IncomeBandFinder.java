package org.gb.sample.pig.income;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class IncomeBandFinder extends EvalFunc<String> {

	private List<Integer> incomeSlabs;

	public IncomeBandFinder(String slabs) {
		System.out.printf("Inside IncomeBandFinder, slabs: %s\n.", slabs);
		incomeSlabs = Stream.of(slabs.split(",")).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
		System.out.printf("Inside IncomeBandFinder, incomeSlabs: %s\n.", incomeSlabs);
	}

	@Override
	public String exec(Tuple tuple) throws IOException {
		Band band = null;
		Integer income = (Integer) tuple.get(0);

		for (int i = 0; i < incomeSlabs.size(); i++) {
			Integer currentSlab = incomeSlabs.get(i);
			Integer nextSlab = null;

			if ((i + 1) < incomeSlabs.size()) {
				nextSlab = incomeSlabs.get(i + 1);
			}

			if (nextSlab == null) {
				if (income.intValue() > currentSlab.intValue()) {
					band = new Band(currentSlab, nextSlab);
					break;
				}
			} else if (income.intValue() > currentSlab.intValue() && income.intValue() <= nextSlab.intValue()) {
				band = new Band(currentSlab, nextSlab);
				break;
			}
		}

		return "band";
	}

}

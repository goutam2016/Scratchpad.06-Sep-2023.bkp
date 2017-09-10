package org.gb.sample.pig.income;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;

public class RecordCounter extends EvalFunc<Integer> {

	@Override
	public Integer exec(Tuple tuple) throws IOException {
		DataBag bag = (DataBag) tuple.get(0);
		int count = (int) bag.size();
		System.out.printf("Inside RecordCounter, no. of records: %d.\n", count);
		return count;
	}

}

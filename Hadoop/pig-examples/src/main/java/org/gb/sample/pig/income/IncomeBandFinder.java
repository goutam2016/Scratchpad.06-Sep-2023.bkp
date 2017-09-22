package org.gb.sample.pig.income;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

/**
 * Does not work with parameterized type: Band org.apache.pig.backend.executionengine.ExecException: ERROR 2080: Foreach
 * currently does not handle type Unknown
 * 
 * @author goutam
 *
 */
public class IncomeBandFinder extends EvalFunc<String> {

	private List<Integer> incomeSlabs;

	public IncomeBandFinder(String slabs) {
		System.out.printf("Inside IncomeBandFinder, slabs: %s.\n", slabs);
		incomeSlabs = Stream.of(slabs.split(",")).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
		System.out.printf("Inside IncomeBandFinder, incomeSlabs: %s.\n", incomeSlabs);
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
		System.out.printf("Income: %d falls in band: %s.\n", income.intValue(), band);
		return band.toString();
	}

	@Override
	public Schema outputSchema(Schema input) {
		if (input.size() != 1) {
			throw new RuntimeException(
					String.format("Expected single field (Integer), found %s fields.", input.size()));
		} else {
			try {
				if (input.getField(0).type != DataType.INTEGER) {
					String msg = String.format("Expected single field (Integer), found (%s).",
							DataType.findTypeName(input.getField(0).type));
					throw new RuntimeException(msg);
				}
			} catch (FrontendException e) {
				throw new RuntimeException(e);
			}
		}
		Schema.FieldSchema fieldSchema = new Schema.FieldSchema("incomeBand", DataType.CHARARRAY);
		return new Schema(fieldSchema);
	}
}

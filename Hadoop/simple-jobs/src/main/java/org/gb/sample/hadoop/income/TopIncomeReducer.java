package org.gb.sample.hadoop.income;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TopIncomeReducer extends Reducer<Text, PersonIncome, BigDecimal, Text> {
	@Override
	protected void reduce(Text key, Iterable<PersonIncome> values,
			Reducer<Text, PersonIncome, BigDecimal, Text>.Context context) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.reduce(key, values, context);
	}
}

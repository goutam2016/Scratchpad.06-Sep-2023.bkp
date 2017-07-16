package org.gb.sample.hadoop.temperature;

public class EqualsSeparatedTempMapper extends AbstractYearlyMaxMinTempMapper {

	@Override
	String getSeparator() {
		return "=";
	}
}

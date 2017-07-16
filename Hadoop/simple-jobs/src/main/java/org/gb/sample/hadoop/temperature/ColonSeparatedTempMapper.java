package org.gb.sample.hadoop.temperature;

public class ColonSeparatedTempMapper extends AbstractYearlyMaxMinTempMapper {

	@Override
	String getSeparator() {
		return ":";
	}
}

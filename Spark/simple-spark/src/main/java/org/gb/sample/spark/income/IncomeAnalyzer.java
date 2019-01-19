package org.gb.sample.spark.income;

import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;

public interface IncomeAnalyzer {

	Map<Integer, List<PersonProfile>> getTopIncomePersonProfiles(JavaRDD<String> nameVsIncomeLines, int topNum);

	void cleanup();

}
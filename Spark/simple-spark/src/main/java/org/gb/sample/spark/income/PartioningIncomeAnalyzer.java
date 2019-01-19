package org.gb.sample.spark.income;

import org.apache.spark.api.java.JavaRDD;

public class PartioningIncomeAnalyzer extends DefaultIncomeAnalyzer {

	private static final long serialVersionUID = -8871549817805612827L;

	PartioningIncomeAnalyzer(JavaRDD<String> personProfileLines) {
		super(personProfileLines, true);
	}
}

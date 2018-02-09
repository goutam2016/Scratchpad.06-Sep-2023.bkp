package org.gb.sample.spark.income;

import org.apache.hadoop.mapred.TextInputFormat;

public class HadoopOldAPICustomTextInputFormat extends TextInputFormat {

	@Override
	protected long computeSplitSize(long goalSize, long minSize, long blockSize) {
		long computedSplitSize = super.computeSplitSize(goalSize, minSize, blockSize);
		System.out.println(String.format(
				"Inside CustomTextInputFormat.computeSplitSize, goalSize: %d, minSize: %d, blockSize: %d, computedSplitSize: %d.", goalSize,
				minSize, blockSize, computedSplitSize));
		return 10240;
	}
}

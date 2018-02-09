package org.gb.sample.spark.income;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

public class HadoopNewAPICustomTextInputFormat extends TextInputFormat {

	@Override
	protected long computeSplitSize(long goalSize, long minSize, long blockSize) {
		long computedSplitSize = super.computeSplitSize(goalSize, minSize, blockSize);
		System.out.println(String.format(
				"Inside NewAPICustomTextInputFormat.computeSplitSize, goalSize: %d, minSize: %d, blockSize: %d, computedSplitSize: %d.",
				goalSize, minSize, blockSize, computedSplitSize));
		return 2 * 1024 * 1024;
	}

	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext context) {
		try {
			System.out.println(String.format("Inside NewAPICustomTextInputFormat.createRecordReader, split length: %d.",
					split.getLength()));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return super.createRecordReader(split, context);
	}

	@Override
	public List<InputSplit> getSplits(JobContext job) throws IOException {
		System.out.println(
				String.format("Inside NewAPICustomTextInputFormat.getSplits, job name: %s.", job.getJobName()));
		List<FileStatus> files = listStatus(job);
		for (FileStatus file : files) {
			Path path = file.getPath();
			long length = file.getLen();
			System.out.println(String.format("Inside NewAPICustomTextInputFormat.getSplits, path: %s, length: %d.",
					path.getName(), length));
		}
		List<InputSplit> inputSplits = super.getSplits(job);
		System.out.println(String.format("Inside NewAPICustomTextInputFormat.getSplits, no. of inputSplits: %d.",
				inputSplits.size()));
		return inputSplits;
	}
}

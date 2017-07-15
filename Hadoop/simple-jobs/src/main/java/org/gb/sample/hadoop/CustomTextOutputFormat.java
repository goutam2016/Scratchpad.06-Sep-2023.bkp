package org.gb.sample.hadoop;

import java.io.IOException;

import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class CustomTextOutputFormat extends TextOutputFormat<String, Integer> {

	@Override
	public RecordWriter<String, Integer> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		System.out.println("Inside CustomTextOutputFormat.getRecordWriter");
		return super.getRecordWriter(job);
	}
}

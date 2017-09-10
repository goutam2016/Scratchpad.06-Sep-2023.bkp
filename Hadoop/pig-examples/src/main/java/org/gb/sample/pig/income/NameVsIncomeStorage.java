package org.gb.sample.pig.income;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.pig.StoreFunc;
import org.apache.pig.data.Tuple;

public class NameVsIncomeStorage extends StoreFunc {

	private RecordWriter<Text, Text> recordWriter;

	@Override
	public void putNext(Tuple tuple) throws IOException {
		System.out.printf("From NameVsIncomeStorage...putNext called, 0: %s, 1: %s, 2: %s.\n", tuple.get(0),
				tuple.get(1), tuple.get(2));
		/*
		 * String nameVsIncomeRecordAsString = String.join((String) tuple.get(0), " ", (String) tuple.get(1), " : ",
		 * tuple.get(2).toString());
		 */
		String nameVsIncomeRecordAsString = new StringBuilder((String) tuple.get(0)).append(" ")
				.append((String) tuple.get(1)).append(" : ").append(((Integer) tuple.get(2)).intValue()).toString();
		try {
			recordWriter.write(null, new Text(nameVsIncomeRecordAsString));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public OutputFormat getOutputFormat() throws IOException {
		return new TextOutputFormat<WritableComparable<Text>, Text>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void prepareToWrite(RecordWriter recordWriter) throws IOException {
		System.out.println("From NameVsIncomeStorage...prepareToWrite called!");
		this.recordWriter = recordWriter;
	}

	@Override
	public void setStoreLocation(String location, Job job) throws IOException {
		System.out.printf("From NameVsIncomeStorage.setStoreLocation(), location: %s, job-name: %s.", location,
				job.getJobName());
		FileOutputFormat.setOutputPath(job, new Path(location));
	}

}

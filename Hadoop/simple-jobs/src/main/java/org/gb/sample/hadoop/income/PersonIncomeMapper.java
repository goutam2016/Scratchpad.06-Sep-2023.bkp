package org.gb.sample.hadoop.income;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PersonIncomeMapper extends Mapper<LongWritable, Text, Text, PersonIncome> {

	@Override
	protected void setup(Mapper<LongWritable, Text, Text, PersonIncome>.Context context)
			throws IOException, InterruptedException {
		String personProfileFileName = context.getConfiguration().get("PERSON_PROFILE_METADATA");
		Path personProfilePath = FileSystems.getDefault().getPath(personProfileFileName);
		System.out.printf("personProfileFileName: %s, exists?: %s\n", personProfileFileName, Files.exists(personProfilePath));
	}

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, PersonIncome>.Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		super.map(key, value, context);
	}
}

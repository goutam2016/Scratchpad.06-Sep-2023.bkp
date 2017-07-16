package org.gb.sample.hadoop.temperature;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public abstract class AbstractYearlyMaxMinTempMapper extends Mapper<LongWritable, Text, Text, Text> {

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] parts = line.split(getSeparator());
		String year = parts[0];
		String[] temps = parts[1].split(",");
		String maxTemp = temps[0];
		String minTemp = temps[1];

		String yearlyMax = year.concat(",").concat(maxTemp);
		String yearlyMin = year.concat(",").concat(minTemp);

		context.write(new Text("max-temp"), new Text(yearlyMax));
		context.write(new Text("min-temp"), new Text(yearlyMin));
	}
	
	abstract String getSeparator();
}

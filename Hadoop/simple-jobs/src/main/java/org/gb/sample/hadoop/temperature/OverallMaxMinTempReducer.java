package org.gb.sample.hadoop.temperature;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class OverallMaxMinTempReducer extends Reducer<Text, Text, IntWritable, IntWritable> {

	@Override
	protected void reduce(Text key, Iterable<Text> values,
			Reducer<Text, Text, IntWritable, IntWritable>.Context context) throws IOException, InterruptedException {
		if (key.toString().equals("max-temp")) {
			Integer overallMaxTemp = null;
			Integer yearOfOvrlMaxTemp = null;
			for (Text value : values) {
				String[] parts = value.toString().split(",");
				Integer year = Integer.parseInt(parts[0]);
				Integer temperature = Integer.parseInt(parts[1]);

				if (overallMaxTemp == null) {
					overallMaxTemp = temperature;
					yearOfOvrlMaxTemp = year;
				} else if (temperature.intValue() > overallMaxTemp.intValue()) {
					overallMaxTemp = temperature;
					yearOfOvrlMaxTemp = year;
				}
			}

			context.write(new IntWritable(overallMaxTemp), new IntWritable(yearOfOvrlMaxTemp));
		} else if (key.toString().equals("min-temp")) {
			Integer overallMinTemp = null;
			Integer yearOfOvrlMinTemp = null;
			for (Text value : values) {
				String[] parts = value.toString().split(",");
				Integer year = Integer.parseInt(parts[0]);
				Integer temperature = Integer.parseInt(parts[1]);

				if (overallMinTemp == null) {
					overallMinTemp = temperature;
					yearOfOvrlMinTemp = year;
				} else if (temperature.intValue() < overallMinTemp.intValue()) {
					overallMinTemp = temperature;
					yearOfOvrlMinTemp = year;
				}
			}

			context.write(new IntWritable(overallMinTemp), new IntWritable(yearOfOvrlMinTemp));
		}

	}
}

package org.gb.sample.hadoop.temperature;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class MaxMinTempMain extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.println(
					"Usage: MaxMinTempMain [generic options] <input-path-1> [input-path-2] [input-path-n] <output-path>");
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		System.out.println("Running max, min temperature job...");
		Job job = Job.getInstance(getConf(), "Max, min temperature");
		job.setJarByClass(getClass());

		for (int i = 0; i < args.length - 1; i++) {
			Path inputPath = new Path(args[i]);
			
			if(args[i].startsWith("19")) {
				MultipleInputs.addInputPath(job, inputPath, TextInputFormat.class, EqualsSeparatedTempMapper.class);
			} else if (args[i].startsWith("20")) {
				MultipleInputs.addInputPath(job, inputPath, TextInputFormat.class, ColonSeparatedTempMapper.class);
			}
		}

		Path outputPath = new Path(args[args.length - 1]);

		FileSystem fs = FileSystem.get(getConf());

		if (fs.exists(outputPath)) {
			fs.delete(outputPath, true);
		}

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setReducerClass(OverallMaxMinTempReducer.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);

		int retVal = job.waitForCompletion(true) ? 0 : 1;

		return retVal;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new MaxMinTempMain(), args);
		System.exit(exitCode);
	}
}

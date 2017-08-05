package org.gb.sample.hadoop.income;

import java.math.BigDecimal;
import java.net.URI;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.TaskCounter;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Main extends Configured implements Tool {

	public static void main(String[] args) {
		try {
			ToolRunner.run(new Main(), args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setMapperReducerTypes1(Job job) {
		job.setMapperClass(PersonIncomeMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(PersonIncome.class);

		job.setReducerClass(TopIncomeReducer.class);
		job.setOutputKeyClass(BigDecimal.class);
		job.setOutputValueClass(BriefPersonProfile.class);
	}

	private void setMapperReducerTypes2(Job job) {
		job.setMapperClass(PersonIncomeMapper2.class);
		job.setMapOutputKeyClass(IncomeWritable.class);
		job.setMapOutputValueClass(PersonIncome.class);

		//job.setSortComparatorClass(IncomeComparator.class);
		//WritableComparator.define(IncomeWritable.class, new IncomeComparator());

		job.setReducerClass(TopIncomeReducer2.class);
		job.setOutputKeyClass(BigDecimal.class);
		job.setOutputValueClass(BriefPersonProfile.class);
	}

	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 3) {
			System.err
					.println("Usage: Main [generic options] <name-vs-income-path> <person-profile-path> <output-path>");
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		System.out.println("This job will find the top few incomes and profile of the associated person.");
		Job job = Job.getInstance(getConf(), "Top incomes with person profiles.");
		job.setJarByClass(getClass());

		Path nameVsIncomePath = new Path(args[0]);
		Path outputPath = new Path(args[2]);
		String personProfileFileName = args[1].substring(args[1].lastIndexOf('/') + 1);

		FileSystem fs = FileSystem.get(getConf());

		if (fs.exists(outputPath)) {
			fs.delete(outputPath, true);
		}

		FileInputFormat.addInputPath(job, nameVsIncomePath);
		FileOutputFormat.setOutputPath(job, outputPath);

		setMapperReducerTypes2(job);

		job.getConfiguration().set("PERSON_PROFILE_METADATA", personProfileFileName);
		job.addCacheFile(new URI(args[1]));

		long startTime = System.currentTimeMillis();
		
		int retVal = job.waitForCompletion(false) ? 0 : 1;
		
		long endTime = System.currentTimeMillis();
		
		double jobDuration = (endTime - startTime)/(double)1000;
		
		System.out.printf("This job took %2.6f seconds.\n", jobDuration);

		long mapInputRecords = job.getCounters().findCounter(TaskCounter.MAP_INPUT_RECORDS).getValue();
		long mapOutputRecords = job.getCounters().findCounter(TaskCounter.MAP_OUTPUT_RECORDS).getValue();
		long reduceInputRecords = job.getCounters().findCounter(TaskCounter.REDUCE_INPUT_RECORDS).getValue();
		long reduceOutputRecords = job.getCounters().findCounter(TaskCounter.REDUCE_OUTPUT_RECORDS).getValue();

		System.out.printf(
				"mapInputRecords: %d, mapOutputRecords: %d, reduceInputRecords: %d, reduceOutputRecords: %d.\n",
				mapInputRecords, mapOutputRecords, reduceInputRecords, reduceOutputRecords);

		return retVal;
	}

}

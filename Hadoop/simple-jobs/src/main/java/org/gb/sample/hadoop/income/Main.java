package org.gb.sample.hadoop.income;

import java.math.BigDecimal;
import java.net.URI;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
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
		String personProfileFileName = args[1].substring(args[1].lastIndexOf('/'));

		FileSystem fs = FileSystem.get(getConf());

		if (fs.exists(outputPath)) {
			fs.delete(outputPath, true);
		}

		FileInputFormat.addInputPath(job, nameVsIncomePath);
		FileOutputFormat.setOutputPath(job, outputPath);

		job.setMapperClass(PersonIncomeMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(PersonIncome.class);

		job.setReducerClass(TopIncomeReducer.class);
		job.setOutputKeyClass(BigDecimal.class);
		job.setOutputValueClass(Text.class);

		job.getConfiguration().set("PERSON_PROFILE_METADATA", personProfileFileName);
		job.addCacheFile(new URI(args[1]));

		return job.waitForCompletion(false) ? 0 : 1;
	}

}

package org.gb.sample.hadoop;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCountMain extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: WordCountMain [generic options] <input-path> <output-path>");
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		System.out.println("Running Word Counting job...");
		Job job = Job.getInstance(getConf(), "Word Counting");
		job.setJarByClass(getClass());
		
		Path inputPath = new Path(args[0]);
		Path outputPath = new Path(args[1]);
		
		FileSystem fs = FileSystem.get(getConf());
		
		if(fs.exists(outputPath)) {
			fs.delete(outputPath, true);
		}

		FileInputFormat.addInputPath(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputPath);

		job.setMapperClass(WordMapper.class);
		job.setReducerClass(WordCounter.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		String inputFormatClassName = job.getInputFormatClass().getName();
		String outputFormatClassName = job.getOutputFormatClass().getName();
		
		System.out.println("inputFormatClassName: " + inputFormatClassName);
		System.out.println("outputFormatClassName: " + outputFormatClassName);

		int retVal = job.waitForCompletion(true) ? 0 : 1;

		return retVal;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new WordCountMain(), args);
		System.exit(exitCode);
	}
}

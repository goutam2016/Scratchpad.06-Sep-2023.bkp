package org.gb.sample.spark.nytaxitrips;

import org.apache.spark.scheduler.SparkListener;
import org.apache.spark.scheduler.SparkListenerApplicationEnd;
import org.apache.spark.scheduler.SparkListenerApplicationStart;
import org.apache.spark.scheduler.SparkListenerExecutorAdded;
import org.apache.spark.scheduler.SparkListenerExecutorRemoved;
import org.apache.spark.scheduler.SparkListenerJobEnd;
import org.apache.spark.scheduler.SparkListenerJobStart;
import org.apache.spark.scheduler.SparkListenerStageCompleted;
import org.apache.spark.scheduler.SparkListenerStageSubmitted;

public class TripAnalysisListener extends SparkListener {

	@Override
	public void onApplicationEnd(SparkListenerApplicationEnd applicationEnd) {
		System.out.printf("onApplicationEnd called, applicationEnd: %s.\n", applicationEnd.toString());
	}

	@Override
	public void onApplicationStart(SparkListenerApplicationStart applicationStart) {
		System.out.printf("onApplicationStart called, app-name: %s, app-id: %s, spark-user: %s.\n",
				applicationStart.appName(), applicationStart.appId().get(), applicationStart.sparkUser());
	}

	@Override
	public void onExecutorAdded(SparkListenerExecutorAdded executorAdded) {
		System.out.printf("onExecutorAdded called, executor-id: %s, total-cores: %d.\n", executorAdded.executorId(),
				executorAdded.executorInfo().totalCores());
	}

	@Override
	public void onExecutorRemoved(SparkListenerExecutorRemoved executorRemoved) {
		System.out.printf("onExecutorRemoved called, executor-id: %s, reason: %s.\n", executorRemoved.executorId(),
				executorRemoved.reason());
	}

	@Override
	public void onJobEnd(SparkListenerJobEnd jobEnd) {
		System.out.printf("onJobEnd called, job-id: %d.\n", jobEnd.jobId());
	}

	@Override
	public void onJobStart(SparkListenerJobStart jobStart) {
		System.out.printf("onJobStart called, job-id: %d.\n", jobStart.jobId());
	}

	@Override
	public void onStageCompleted(SparkListenerStageCompleted stageCompleted) {
		System.out.printf("onStageCompleted called, name: %s, stage-id: %d, num-tasks: %d.\n",
				stageCompleted.stageInfo().name(), stageCompleted.stageInfo().stageId(),
				stageCompleted.stageInfo().numTasks());
	}

	@Override
	public void onStageSubmitted(SparkListenerStageSubmitted stageSubmitted) {
		System.out.printf("onStageSubmitted called, name: %s, stage-id: %d, num-tasks: %d.\n",
				stageSubmitted.stageInfo().name(), stageSubmitted.stageInfo().stageId(),
				stageSubmitted.stageInfo().numTasks());
	}

}

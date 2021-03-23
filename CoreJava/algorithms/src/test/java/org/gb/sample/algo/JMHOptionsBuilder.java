package org.gb.sample.algo;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openjdk.jmh.profile.JavaFlightRecorderProfiler;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;

public final class JMHOptionsBuilder {

	private static final String JFR_RECORDING_SWITCH_PROPTY = "JFRRecordingOn";
	private static final String JFR_TEMPLATE_NAME_PROPTY = "JFRTemplateName";
	private static final String DEFAULT_JFR_TEMPLATE = "default.jfc";
	private static final String JFR_RECORDING_NAME_PROPTY = "JFRRecordingName";

	private JMHOptionsBuilder() {

	}

	public static Map<String, String> parseCmdLineArgs(final String[] args) {
		Stream<String[]> argNameAndValuePairs = Arrays.stream(args[0].split(",")).map(arg -> arg.split("="))
				.filter(argNameAndValue -> argNameAndValue.length == 2);
		return argNameAndValuePairs
				.collect(Collectors.toUnmodifiableMap(argNameAndValue -> argNameAndValue[0].trim(), argNameAndValue -> argNameAndValue[1].trim()));
	}

	public static ChainedOptionsBuilder addJFRProfiler(final Map<String, String> argNameVsValue, final ChainedOptionsBuilder optBuilder,
			final String defaultRecordingName) {
		final String jfrRecordingOn = argNameVsValue.getOrDefault(JFR_RECORDING_SWITCH_PROPTY, "false");
		ChainedOptionsBuilder optBuilderWithJFRFlags = null;

		if (Boolean.parseBoolean(jfrRecordingOn)) {
			String jfrRecordingName = argNameVsValue.getOrDefault(JFR_RECORDING_NAME_PROPTY, defaultRecordingName);
			String jfrTemplateName = argNameVsValue.getOrDefault(JFR_TEMPLATE_NAME_PROPTY, DEFAULT_JFR_TEMPLATE);

			StringBuilder jfrStartCmdBuilder = new StringBuilder("-XX:StartFlightRecording=disk=true,dumponexit=false");
			String jfrStartCmd = jfrStartCmdBuilder.append(",").append("name=").append(jfrRecordingName).append(",").append("settings=").append(jfrTemplateName)
					.toString();
			String[] jfrFlags = new String[] { "-XX:+FlightRecorder", jfrStartCmd };

			optBuilderWithJFRFlags = optBuilder.jvmArgs(jfrFlags).addProfiler(JavaFlightRecorderProfiler.class);
		}
		return optBuilderWithJFRFlags;
	}
}

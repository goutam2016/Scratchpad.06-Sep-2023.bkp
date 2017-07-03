package org.gb.sample.hadoop;

import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class SimpleMessageWriter {

	public static void main(String[] args) throws Exception {
		String destUri = args[0];
		String message = "ekta misti mojar Gulli eshe ichchhe moto ginni pona korchhe!";
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(destUri), conf);
		Path filePath = new Path(destUri);
		OutputStream os = null;

		try {
			os = fs.create(filePath, true);
			os.write(message.getBytes());
		} finally {
			os.close();
		}

	}

}

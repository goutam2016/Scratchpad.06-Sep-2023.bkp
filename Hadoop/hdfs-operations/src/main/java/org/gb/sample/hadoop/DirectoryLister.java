package org.gb.sample.hadoop;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public class DirectoryLister {

	public static void main(String[] args) throws Exception {
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		Path path = new Path(uri);
		
		FileStatus[] fileStatuses = fs.listStatus(path);
		Path[] listedPaths = FileUtil.stat2Paths(fileStatuses);
		
		for (Path listedPath : listedPaths) {
			System.out.println(listedPath);
		}
	}

}

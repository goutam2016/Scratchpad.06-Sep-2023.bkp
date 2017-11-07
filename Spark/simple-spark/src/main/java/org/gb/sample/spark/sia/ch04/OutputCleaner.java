package org.gb.sample.spark.sia.ch04;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class OutputCleaner extends SimpleFileVisitor<Path> {

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Files.delete(file);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException ex) throws IOException {
		if (ex == null) {
			Files.delete(dir);
			return FileVisitResult.CONTINUE;
		} else {
			throw ex;
		}
	}
}

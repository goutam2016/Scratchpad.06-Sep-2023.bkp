package org.gb.sample.algo.matrix;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

public class TraverserTest {

	private static final Logger LOGGER = Logger.getLogger(TraverserTest.class);

	@Test
	public void topLeftToBottomRight_1by1() {
		int totalRows = 1;
		int totalColumns = 1;
		int totalRoutes = Traverser.topLeftToBottomRight(totalRows, totalColumns);
		LOGGER.info(
				String.format("For %d x %d matrix, total possible routes: %d.", totalRows, totalColumns, totalRoutes));
	}
	
	@Test
	public void topLeftToBottomRight_2by2() {
		int totalRows = 2;
		int totalColumns = 2;
		int totalRoutes = Traverser.topLeftToBottomRight(totalRows, totalColumns);
		LOGGER.info(
				String.format("For %d x %d matrix, total possible routes: %d.", totalRows, totalColumns, totalRoutes));
	}
	
	@Test
	public void topLeftToBottomRight_5by5() {
		int totalRows = 5;
		int totalColumns = 5;
		int totalRoutes = Traverser.topLeftToBottomRight(totalRows, totalColumns);
		LOGGER.info(
				String.format("For %d x %d matrix, total possible routes: %d.", totalRows, totalColumns, totalRoutes));
	}

	@Test
	public void topLeftToBottomRight_10by10() {
		int totalRows = 10;
		int totalColumns = 10;
		int totalRoutes = Traverser.topLeftToBottomRight(totalRows, totalColumns);
		LOGGER.info(
				String.format("For %d x %d matrix, total possible routes: %d.", totalRows, totalColumns, totalRoutes));
	}

	@Test
	public void topLeftToBottomRightParallel_1by1() {
		int totalRows = 1;
		int totalColumns = 1;
		int totalRoutes = Traverser.topLeftToBottomRightParallel(totalRows, totalColumns);
		LOGGER.info(
				String.format("For %d x %d matrix, total possible routes: %d.", totalRows, totalColumns, totalRoutes));
	}
	
	@Test
	public void topLeftToBottomRightParallel_2by2() {
		int totalRows = 2;
		int totalColumns = 2;
		int totalRoutes = Traverser.topLeftToBottomRightParallel(totalRows, totalColumns);
		LOGGER.info(
				String.format("For %d x %d matrix, total possible routes: %d.", totalRows, totalColumns, totalRoutes));
	}

	@Test
	public void topLeftToBottomRightParallel_5by5() {
		int totalRows = 5;
		int totalColumns = 5;
		int totalRoutes = Traverser.topLeftToBottomRightParallel(totalRows, totalColumns);
		LOGGER.info(
				String.format("For %d x %d matrix, total possible routes: %d.", totalRows, totalColumns, totalRoutes));
	}

	@Test
	public void topLeftToBottomRightParallel_10by10() {
		int totalRows = 10;
		int totalColumns = 10;
		int totalRoutes = Traverser.topLeftToBottomRightParallel(totalRows, totalColumns);
		LOGGER.info(
				String.format("For %d x %d matrix, total possible routes: %d.", totalRows, totalColumns, totalRoutes));
	}
	
	@Ignore
	@Test
	public void topLeftToBottomRightParallel_15by15() {
		int totalRows = 15;
		int totalColumns = 15;
		int totalRoutes = Traverser.topLeftToBottomRightParallel(totalRows, totalColumns);
		LOGGER.info(
				String.format("For %d x %d matrix, total possible routes: %d.", totalRows, totalColumns, totalRoutes));
	}
	
}

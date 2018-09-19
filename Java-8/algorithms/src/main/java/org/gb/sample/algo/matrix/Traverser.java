package org.gb.sample.algo.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class Traverser {

	public static int topLeftToBottomRight(final int totalRows, final int totalColumns) {
		Position startingPosition = new Position(1, 1);
		List<Route> allPossibleRoutes = getSubsequentRoutes(startingPosition, totalRows, totalColumns);
		return allPossibleRoutes.size();
	}

	private static List<Route> getSubsequentRoutes(final Position currentPosition, final int totalRows,
			final int totalColumns) {
		List<Route> subsequentRoutes = new ArrayList<>();
		if (currentPosition.getRowLoc() < totalRows) {
			Position rightShifted = new Position(currentPosition.getRowLoc() + 1, currentPosition.getColumnLoc());
			List<Route> rightShiftedSubRoutes = getSubsequentRoutes(rightShifted, totalRows, totalColumns);

			if (rightShiftedSubRoutes.isEmpty()) {
				Route rightShiftedSubRoute = new Route();
				rightShiftedSubRoute.pushTraversedPosition(rightShifted);
				subsequentRoutes.add(rightShiftedSubRoute);
			} else {
				for (Route rightShiftedSubRoute : rightShiftedSubRoutes) {
					rightShiftedSubRoute.pushTraversedPosition(rightShifted);
					subsequentRoutes.add(rightShiftedSubRoute);
				}
			}
		}

		if (currentPosition.getColumnLoc() < totalColumns) {
			Position downShifted = new Position(currentPosition.getRowLoc(), currentPosition.getColumnLoc() + 1);
			List<Route> downShiftedSubRoutes = getSubsequentRoutes(downShifted, totalRows, totalColumns);

			if (downShiftedSubRoutes.isEmpty()) {
				Route downShiftedSubRoute = new Route();
				downShiftedSubRoute.pushTraversedPosition(downShifted);
				subsequentRoutes.add(downShiftedSubRoute);
			} else {
				for (Route downShiftedSubRoute : downShiftedSubRoutes) {
					downShiftedSubRoute.pushTraversedPosition(downShifted);
					subsequentRoutes.add(downShiftedSubRoute);
				}
			}
		}

		return subsequentRoutes;
	}

	public static int topLeftToBottomRightParallel(final int totalRows, final int totalColumns) {
		final Position startingPosition = new Position(1, 1);
		ForkJoinPool forkJoinPool = new ForkJoinPool(8);
		ForkJoinTask<List<Route>> routeFinder = new RouteFinder(totalRows, totalColumns, startingPosition);
		
		List<Route> allPossibleRoutes = forkJoinPool.invoke(routeFinder);
		return allPossibleRoutes.size();
	}
}

package org.gb.sample.algo.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import org.apache.log4j.Logger;

public class RouteFinder extends RecursiveTask<List<Route>> {

	private static final long serialVersionUID = -7588724134034190007L;
	private static final Logger LOGGER = Logger.getLogger(RouteFinder.class);

	private final int totalRows;
	private final int totalColumns;
	private final Position currentPosition;

	public RouteFinder(int totalRows, int totalColumns, Position currentPosition) {
		super();
		this.totalRows = totalRows;
		this.totalColumns = totalColumns;
		this.currentPosition = currentPosition;
	}

	@Override
	protected List<Route> compute() {
		List<Route> subsequentRoutes = new ArrayList<>();
		Position rightShifted = null;
		Position downShifted = null;
		ForkJoinTask<List<Route>> rightShiftedRouteFinderTask = null;
		ForkJoinTask<List<Route>> downShiftedRouteFinderTask = null;

		if (currentPosition.getRowLoc() < totalRows) {
			rightShifted = new Position(currentPosition.getRowLoc() + 1, currentPosition.getColumnLoc());
			RouteFinder rightShiftedRouteFinder = new RouteFinder(totalRows, totalColumns, rightShifted);
			rightShiftedRouteFinderTask = rightShiftedRouteFinder.fork();
		}
		if (currentPosition.getColumnLoc() < totalColumns) {
			downShifted = new Position(currentPosition.getRowLoc(), currentPosition.getColumnLoc() + 1);
			RouteFinder downShiftedRouteFinder = new RouteFinder(totalRows, totalColumns, downShifted);
			downShiftedRouteFinderTask = downShiftedRouteFinder.fork();
		}

		if (rightShiftedRouteFinderTask != null) {
			List<Route> rightShiftedSubRoutes = rightShiftedRouteFinderTask.join();

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
		if (downShiftedRouteFinderTask != null) {
			List<Route> downShiftedSubRoutes = downShiftedRouteFinderTask.join();

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
}

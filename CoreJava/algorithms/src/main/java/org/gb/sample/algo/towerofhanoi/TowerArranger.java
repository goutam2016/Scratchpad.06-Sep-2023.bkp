package org.gb.sample.algo.towerofhanoi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TowerArranger {

	private static class TargetAndBufferPegs {
		private Peg target;
		private Peg buffer;

		private TargetAndBufferPegs(Peg target, Peg buffer) {
			this.target = target;
			this.buffer = buffer;
		}
	}

	static List<Movement> transfer(Peg source, Peg target, Peg buffer) {
		List<Ring> allRingsInAscOrder = Collections.unmodifiableList(source.getRingsTopToBottom());
		Map<Ring, TargetAndBufferPegs> ringVsTargetAndBufferPegs = mapRingToTargetAndBufferPegs(allRingsInAscOrder,
				target, buffer);

		List<Movement> movements = new ArrayList<>();
		Ring topRingOnSource = source.removeFromTop();

		while (topRingOnSource != null) {
			TargetAndBufferPegs targetAndBufferPegs = ringVsTargetAndBufferPegs.get(topRingOnSource);
			List<Movement> stageMovements = doStage(topRingOnSource.getCurrentPeg(), targetAndBufferPegs.target,
					targetAndBufferPegs.buffer, topRingOnSource, allRingsInAscOrder);
			movements.addAll(stageMovements);
			topRingOnSource = source.removeFromTop();
		}

		return movements;
	}

	/*
	 * A stage is defined by transferring a given ring and rings smaller than itself to the target peg of the given
	 * ring. A stage is not complete until all rings smaller than the given one are assembled (in increasing order of
	 * diameter) on top of the given ring.
	 */
	private static List<Movement> doStage(Peg source, Peg target, Peg buffer, Ring ringToMove,
			List<Ring> allRingsInAscOrder) {
		return move(source, target, buffer, ringToMove, allRingsInAscOrder);
	}

	private static List<Movement> move(Peg source, Peg target, Peg buffer, Ring ringToMove,
			List<Ring> allRingsInAscOrder) {
		target.addToTop(ringToMove);

		Movement movement = new Movement(ringToMove, source.getName(), target.getName());
		List<Movement> movements = new ArrayList<>(Arrays.asList(movement));

		/*
		 * Is there a ring smaller than ringToMove? If no, the current stage is complete. If yes, then move the smaller
		 * ring on top of target.
		 */
		List<Ring> smallerRingsInAscOrder = allRingsInAscOrder.stream()
				.filter(ring -> ring.getDiameter() < ringToMove.getDiameter()).collect(Collectors.toList());

		if (!smallerRingsInAscOrder.isEmpty()) {
			Map<Ring, TargetAndBufferPegs> ringVsTargetAndBufferPegs = mapRingToTargetAndBufferPegs(
					smallerRingsInAscOrder, target, source);
			for (Ring smallerRing : smallerRingsInAscOrder) {
				TargetAndBufferPegs targetAndBufferPegs = ringVsTargetAndBufferPegs.get(smallerRing);
				Peg sourceOfSmallerRing = smallerRing.getCurrentPeg();
				smallerRing = sourceOfSmallerRing.removeFromTop();

				List<Movement> subsequentMovements = move(sourceOfSmallerRing, targetAndBufferPegs.target,
						targetAndBufferPegs.buffer, smallerRing, allRingsInAscOrder);
				movements.addAll(subsequentMovements);
			}
		}

		return movements;
	}

	private static Map<Ring, TargetAndBufferPegs> mapRingToTargetAndBufferPegs(List<Ring> ascRings, Peg target,
			Peg buffer) {
		Stream<Integer> indices = Stream.iterate(0, idx -> idx + 1).limit(ascRings.size());
		return indices.collect(Collectors.toMap(idx -> ascRings.get(ascRings.size() - 1 - idx),
				idx -> resolveTargetAndBuffer(idx, target, buffer)));
	}

	private static TargetAndBufferPegs resolveTargetAndBuffer(int ringIdx, Peg target, Peg buffer) {
		return (ringIdx % 2 == 0) ? new TargetAndBufferPegs(target, buffer) : new TargetAndBufferPegs(buffer, target);
	}
}

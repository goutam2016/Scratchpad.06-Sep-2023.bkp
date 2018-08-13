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

		for (Ring ring : allRingsInAscOrder) {
			TargetAndBufferPegs targetAndBufferPegs = ringVsTargetAndBufferPegs.get(ring);
			System.out.println("Starting stage for ring diameter: " + ring.getDiameter() + ", target peg: "
					+ targetAndBufferPegs.target.getName());
			List<Movement> movements = doStage(source, targetAndBufferPegs.target, targetAndBufferPegs.buffer,
					allRingsInAscOrder);
			movements.forEach(System.out::println);
			System.out.println("Stage complete for ring diameter: " + ring.getDiameter());
			System.out.println("Source peg: " + source);
			System.out.println("Target peg: " + target);
			System.out.println("Buffer peg: " + buffer);
		}

		/*System.out.println("Before transfer...");
		System.out.println("Source peg: " + source);
		System.out.println("Target peg: " + target);
		System.out.println("Buffer peg: " + buffer);
		System.out.println("---------------------------------------------");
		List<Movement> movements = doStage(source, target, buffer, allRingsInAscOrder);
		System.out.println("After transfer...");
		System.out.println("Source peg: " + source);
		System.out.println("Target peg: " + target);
		System.out.println("Buffer peg: " + buffer);
		System.out.println("---------------------------------------------");
		movements.forEach(System.out::println);*/

		return Collections.emptyList();
	}

	private static List<Movement> moveOne(Peg source, Peg target, Peg buffer, List<Ring> allRingsInAscOrder) {
		List<Ring> movedRings = target.getRingsTopToBottom();

		if (movedRings.size() == allRingsInAscOrder.size()) {
			// All rings have moved, no more movement required.
			return Collections.emptyList();
		}

		Map<Ring, TargetAndBufferPegs> ringVsTargetAndBufferPegs = null;
		if (movedRings.isEmpty()) {
			ringVsTargetAndBufferPegs = mapRingToTargetAndBufferPegs(allRingsInAscOrder, target, buffer);
		} else {
			Ring topRingOnTarget = movedRings.get(0);
			List<Ring> smallerRingsInAscOrder = allRingsInAscOrder.stream()
					.filter(ring -> ring.getDiameter() < topRingOnTarget.getDiameter()).collect(Collectors.toList());

		}

		Ring ringToMove = source.removeFromTop();
		Peg targetForRingToMove = ringVsTargetAndBufferPegs.get(ringToMove).target;
		targetForRingToMove.addToTop(ringToMove);
		Movement movement = new Movement(ringToMove, source.getName(), targetForRingToMove.getName());
		List<Movement> movements = new ArrayList<>(Arrays.asList(movement));

		return movements;
	}

	private static List<Movement> doStage(Peg source, Peg target, Peg buffer, List<Ring> allRingsInAscOrder) {
		Ring ringToMove = source.removeFromTop();

		if (ringToMove == null) {
			return Collections.emptyList();
		}
		// System.out.println("ringToMove diameter: " + ringToMove.getDiameter());
		target.addToTop(ringToMove);

		Movement movement = new Movement(ringToMove, source.getName(), target.getName());
		List<Movement> movements = new ArrayList<>(Arrays.asList(movement));

		/*
		 * Is there a ring smaller than ringToMove? If no, this stage is complete. If
		 * yes, then move the smaller ring on top of target.
		 */
		List<Ring> smallerRingsInAscOrder = allRingsInAscOrder.stream()
				.filter(ring -> ring.getDiameter() < ringToMove.getDiameter()).collect(Collectors.toList());

		if (!smallerRingsInAscOrder.isEmpty()) {
			Map<Ring, TargetAndBufferPegs> ringVsTargetAndBufferPegs = mapRingToTargetAndBufferPegs(
					smallerRingsInAscOrder, target, source);
			// System.out.println("Stage incomplete, next moves: " +
			// ringVsTargetAndBufferPegs);
			for (Ring smallerRing : smallerRingsInAscOrder) {
				TargetAndBufferPegs targetAndBufferPegs = ringVsTargetAndBufferPegs.get(smallerRing);
				List<Movement> subsequentMovements = doStage(buffer, targetAndBufferPegs.target,
						targetAndBufferPegs.buffer, allRingsInAscOrder);
				movements.addAll(subsequentMovements);
			}
		} else {
			// System.out.println("Stage complete for ring diameter: " +
			// ringToMove.getDiameter());
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

	private static List<Movement> move(Peg source, Peg target, Peg buffer, List<Ring> allAscRings,
			List<Movement> movements) {
		Ring ringToMove = source.removeFromTop();

		if (ringToMove == null) {
			return movements;
		}
		target.addToTop(ringToMove);
		Movement movement = new Movement(ringToMove, source.getName(), target.getName());
		return Arrays.asList(movement);
	}

	public static void main(String[] args) {
		Ring ring1 = new Ring(1);
		Ring ring2 = new Ring(2);
		Ring ring3 = new Ring(3);
		Ring ring4 = new Ring(4);
		Ring ring5 = new Ring(5);
		Ring ring6 = new Ring(6);
		Ring ring7 = new Ring(7);
		Ring ring8 = new Ring(8);
		Ring ring9 = new Ring(9);
		Ring ring10 = new Ring(10);
		Ring ring11 = new Ring(11);
		Ring ring12 = new Ring(12);
		Ring ring13 = new Ring(13);
		Ring ring14 = new Ring(14);
		Ring ring15 = new Ring(15);
		Peg source = new Peg("A", 15);
		Peg target = new Peg("B", 15);
		Peg buffer = new Peg("C", 15);
		source.addToTop(ring15);
		source.addToTop(ring14);
		source.addToTop(ring13);
		source.addToTop(ring12);
		source.addToTop(ring11);
		source.addToTop(ring10);
		source.addToTop(ring9);
		source.addToTop(ring8);
		source.addToTop(ring7);		
		source.addToTop(ring6);
		source.addToTop(ring5);
		source.addToTop(ring4);
		source.addToTop(ring3);
		source.addToTop(ring2);
		source.addToTop(ring1);
		transfer(source, target, buffer);
	}
}

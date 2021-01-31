package org.gb.sample.algo.towerofhanoi;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TowerArrangerTest {

	private static final Logger LOGGER = Logger.getLogger(TowerArrangerTest.class);

	private static Peg source;
	private static Peg target;
	private static Peg buffer;

	@BeforeClass
	public static void setupForAll() {
		source = new Peg("A", 20);
		target = new Peg("B", 20);
		buffer = new Peg("C", 20);
	}

	@After
	public void teardown() {
		emptyPeg(source);
		emptyPeg(target);
		emptyPeg(buffer);
	}

	private void emptyPeg(Peg peg) {
		Ring topmostRing = peg.removeFromTop();

		while (topmostRing != null) {
			topmostRing = peg.removeFromTop();
		}
	}
	
	private void fillSource(final int totalRings) {
		for (int diam = totalRings; diam > 0; diam--) {
			Ring ring = new Ring(diam);
			source.addToTop(ring);
		}
	}
	
	private void verifyResults(final int totalRings, final List<Movement> movements) {
		LOGGER.info(String.format("With %d rings...", totalRings));
		LOGGER.info(String.format("Total no. of movements: %d.", movements.size()));
		LOGGER.info(String.format("Source peg: %s", source));
		LOGGER.info(String.format("Target peg: %s", target));
		LOGGER.info(String.format("Buffer peg: %s", buffer));
		Assert.assertEquals(0, source.getRingsTopToBottom().size());
		Assert.assertEquals(totalRings, target.getRingsTopToBottom().size());
		Assert.assertEquals(0, buffer.getRingsTopToBottom().size());
		Assert.assertEquals((int)Math.pow(2, totalRings) - 1, movements.size());
		LOGGER.info("-----------------------------------------------------------------------------------------------------------------------------------");
	}

	@Test
	public void transfer_3Rings() {
		final int totalRings = 3;

		fillSource(totalRings);

		List<Movement> movements = TowerArranger.transfer(source, target, buffer);
		verifyResults(totalRings, movements);
	}
	
	@Test
	public void transfer_5Rings() {
		final int totalRings = 5;

		fillSource(totalRings);

		List<Movement> movements = TowerArranger.transfer(source, target, buffer);
		verifyResults(totalRings, movements);		
	}
	
	@Test
	public void transfer_10Rings() {
		final int totalRings = 10;

		fillSource(totalRings);

		List<Movement> movements = TowerArranger.transfer(source, target, buffer);
		verifyResults(totalRings, movements);		
	}
	
	@Test
	public void transfer_15Rings() {
		final int totalRings = 15;

		fillSource(totalRings);

		List<Movement> movements = TowerArranger.transfer(source, target, buffer);
		verifyResults(totalRings, movements);		
	}
}

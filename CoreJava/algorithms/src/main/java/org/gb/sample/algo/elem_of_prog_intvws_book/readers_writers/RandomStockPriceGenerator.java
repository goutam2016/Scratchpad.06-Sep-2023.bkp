package org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers;

import java.util.Random;

public class RandomStockPriceGenerator {

	private static Random random = new Random();

	public static int generate() {
		return random.ints(200, 501).findFirst().getAsInt();
	}
}

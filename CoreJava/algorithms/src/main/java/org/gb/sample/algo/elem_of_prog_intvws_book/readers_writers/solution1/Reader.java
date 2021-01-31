package org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers.solution1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers.RecordedStockPrices;
import org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers.StockPrices;

public class Reader implements Callable<RecordedStockPrices> {

	private static final Logger LOGGER = Logger.getLogger(Reader.class);
	private StockPrices stockPrices;
	private Semaphore writePermit;
	private AtomicInteger readerCount;
	private AtomicInteger updateCount;
	private CountDownLatch exitSignal;
	private long sleepTimeInMills;
	private CountDownLatch firstUpdateDone;

	Reader(StockPrices stockPrices, Semaphore writePermit, AtomicInteger readerCount, AtomicInteger updateCount, CountDownLatch exitSignal,
			long sleepTimeInMills, CountDownLatch firstUpdateDone) {
		super();
		this.stockPrices = stockPrices;
		this.writePermit = writePermit;
		this.readerCount = readerCount;
		this.updateCount = updateCount;
		this.exitSignal = exitSignal;
		this.sleepTimeInMills = sleepTimeInMills;
		this.firstUpdateDone = firstUpdateDone;
	}

	@Override
	public RecordedStockPrices call() throws Exception {
		Map<Integer, StockPrices> stockPricesPerUpdate = new HashMap<>();

		LOGGER.info("First update hasn't happened yet, reader has to wait.");
		firstUpdateDone.await();

		try {
			while (true) {

				synchronized (stockPrices) {
					if (writePermit.availablePermits() == 0) {
						LOGGER.info("Writer thread updating stock prices, reader has to wait.");
						stockPrices.wait();
					}
				}

				readerCount.getAndIncrement();
				
				LOGGER.debug(String.format(
						"Reading stock prices; Apple: %d, Google: %d, Microsoft: %d, IBM: %d, Oracle: %d, Accenture: %d, Walmart: %d, Tesla: %d, Amazon: %d, Intel: %d",
						stockPrices.getAppleStockPrice(), stockPrices.getGoogleStockPrice(), stockPrices.getMicrosoftStockPrice(),
						stockPrices.getIbmStockPrice(), stockPrices.getOracleStockPrice(), stockPrices.getAccentureStockPrice(),
						stockPrices.getWalmartStockPrice(), stockPrices.getTeslaStockPrice(), stockPrices.getAmazonStockPrice(),
						stockPrices.getIntelStockPrice()));
				
				if(!stockPricesPerUpdate.containsKey(updateCount.get())) {
					StockPrices stockPricesSnapshot = stockPrices.copy();
					stockPricesPerUpdate.put(updateCount.get(), stockPricesSnapshot);
				}

				doSomeProcessing();

				readerCount.getAndDecrement();

				synchronized (stockPrices) {
					if (readerCount.get() == 0) {
						LOGGER.debug("No active readers, waking up writer...");
						stockPrices.notify();
					}
				}

				if (exitSignal.getCount() == 0) {
					LOGGER.info("Reader shutting down...");
					break;
				}
				
				doSomeProcessing();
			}
		} catch (Exception e) {
			LOGGER.error("Exception caught!", e);
		}

		String currentThreadName = Thread.currentThread().getName();

		return new RecordedStockPrices(currentThreadName, stockPricesPerUpdate);
	}

	/*
	 * Activities like network calls, database access, filesystem access, mathematical calculations simulated by sleep.
	 */
	private void doSomeProcessing() throws InterruptedException {
		LOGGER.debug("Reader going to sleep...");
		Thread.sleep(sleepTimeInMills);
	}

}

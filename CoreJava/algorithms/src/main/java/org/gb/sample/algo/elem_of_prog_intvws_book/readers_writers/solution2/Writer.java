package org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers.solution2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.log4j.Logger;
import org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers.RandomStockPriceGenerator;
import org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers.StockPrices;

public class Writer implements Callable<Map<Integer, StockPrices>> {

	private static final Logger LOGGER = Logger.getLogger(Writer.class);
	private StockPrices stockPrices;
	private AtomicInteger updateCount;
	private ReadWriteLock rwLock;
	private CountDownLatch exitSignal;
	private long sleepTimeInMills;
	private int maxNoOfUpdates;
	private CountDownLatch firstUpdateDone;

	Writer(StockPrices stockPrices, AtomicInteger updateCount, ReadWriteLock rwLock, CountDownLatch exitSignal,
			long sleepTimeInMills, int maxNoOfUpdates, CountDownLatch firstUpdateDone) {
		super();
		this.stockPrices = stockPrices;
		this.updateCount = updateCount;
		this.rwLock = rwLock;
		this.exitSignal = exitSignal;
		this.sleepTimeInMills = sleepTimeInMills;
		this.maxNoOfUpdates = maxNoOfUpdates;
		this.firstUpdateDone = firstUpdateDone;
	}

	@Override
	public Map<Integer, StockPrices> call() throws Exception {
		Map<Integer, StockPrices> stockPricesPerUpdate = new HashMap<>();
		try {
			for (int updateIdx = 0; updateIdx < maxNoOfUpdates; updateIdx++) {
				
				rwLock.writeLock().lock();

				int appleStockPrice = RandomStockPriceGenerator.generate();
				int googleStockPrice = RandomStockPriceGenerator.generate();
				int microsoftStockPrice = RandomStockPriceGenerator.generate();
				int ibmStockPrice = RandomStockPriceGenerator.generate();
				int oracleStockPrice = RandomStockPriceGenerator.generate();
				int accentureStockPrice = RandomStockPriceGenerator.generate();
				int walmartStockPrice = RandomStockPriceGenerator.generate();
				int teslaStockPrice = RandomStockPriceGenerator.generate();
				int amazonStockPrice = RandomStockPriceGenerator.generate();
				int intelStockPrice = RandomStockPriceGenerator.generate();

				LOGGER.info("Writer thread updating stock prices...");
				doSomeProcessing();

				stockPrices.setAppleStockPrice(appleStockPrice);
				stockPrices.setGoogleStockPrice(googleStockPrice);
				stockPrices.setMicrosoftStockPrice(microsoftStockPrice);
				stockPrices.setIbmStockPrice(ibmStockPrice);
				stockPrices.setOracleStockPrice(oracleStockPrice);
				stockPrices.setAccentureStockPrice(accentureStockPrice);
				stockPrices.setWalmartStockPrice(walmartStockPrice);
				stockPrices.setTeslaStockPrice(teslaStockPrice);
				stockPrices.setAmazonStockPrice(amazonStockPrice);
				stockPrices.setIntelStockPrice(intelStockPrice);

				LOGGER.info(String.format(
						"UpdateIdx: %d; Updating stock prices; Apple: %d, Google: %d, Microsoft: %d, IBM: %d, Oracle: %d, Accenture: %d, Walmart: %d, Tesla: %d, Amazon: %d, Intel: %d",
						updateIdx, appleStockPrice, googleStockPrice, microsoftStockPrice, ibmStockPrice, oracleStockPrice, accentureStockPrice, walmartStockPrice,
						teslaStockPrice, amazonStockPrice, intelStockPrice));
				updateCount.set(updateIdx);

				StockPrices stockPricesSnapshot = stockPrices.copy();
				stockPricesPerUpdate.put(updateIdx, stockPricesSnapshot);

				if (updateIdx == 0) {
					LOGGER.info("First update done, readers can now start.");
					firstUpdateDone.countDown();
				}

				rwLock.writeLock().unlock();
				
				doSomeProcessing();
			}

		} catch (Exception e) {
			LOGGER.error("Exception caught!", e);
		}

		LOGGER.info("Writer shutting down...");
		exitSignal.countDown();

		return stockPricesPerUpdate;
	}

	/*
	 * Activities like network calls, database access, filesystem access, mathematical calculations simulated by sleep.
	 */
	private void doSomeProcessing() throws InterruptedException {
		LOGGER.debug("Writer going to sleep...");
		Thread.sleep(sleepTimeInMills);
	}

}

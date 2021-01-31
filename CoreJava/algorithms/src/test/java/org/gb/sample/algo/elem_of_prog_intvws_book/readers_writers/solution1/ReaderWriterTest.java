package org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers.solution1;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers.RecordedStockPrices;
import org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers.StockPrices;
import org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers.StockPricesUpdatesVerifier;
import org.junit.Assert;
import org.junit.Test;

public class ReaderWriterTest {

	private static final Logger LOGGER = Logger.getLogger(ReaderWriterTest.class);

	@Test
	public void oneWriterMultipleReaders() {
		final StockPrices stockPrices = new StockPrices();
		final Semaphore writePermit = new Semaphore(1);
		final AtomicInteger readerCount = new AtomicInteger(0);
		final AtomicInteger updateCount = new AtomicInteger(0);
		final CountDownLatch exitSignal = new CountDownLatch(1);
		final CountDownLatch firstUpdateDone = new CountDownLatch(1);
		final long readerSleepTimeInMills = 300;
		final long writerSleepTimeInMills = 200;
		final int maxNoOfUpdates = 10;
		final int noOfReaders = 20;

		Stream<Reader> readers = Stream.iterate(0, i -> i + 1).limit(noOfReaders)
				.map(i -> new Reader(stockPrices, writePermit, readerCount, updateCount, exitSignal, readerSleepTimeInMills, firstUpdateDone));

		Writer writer = new Writer(stockPrices, writePermit, readerCount, updateCount, exitSignal, writerSleepTimeInMills, maxNoOfUpdates,
				firstUpdateDone);

		try {
			ExecutorService threadPool = Executors.newFixedThreadPool(noOfReaders + 1);
			List<Future<RecordedStockPrices>> stockPricesRecordedByAllReadersFutures = readers.map(r -> threadPool.submit(r)).collect(Collectors.toList());
			Future<Map<Integer, StockPrices>> stockPricesRecordedByWriterFuture = threadPool.submit(writer);
			LOGGER.info("Tasks submitted.");
			LOGGER.info("Initiating shutdown...");
			threadPool.shutdown();
			LOGGER.info("awaitTermination...");
			threadPool.awaitTermination(5, TimeUnit.MINUTES);

			StockPricesUpdatesVerifier.verifyReadersRecordsAgainstWriterRecords(stockPricesRecordedByAllReadersFutures, stockPricesRecordedByWriterFuture);

		} catch (Exception e) {
			LOGGER.error("Exception caught!", e);
			Assert.fail("Exception caught, test case fails.");
		}
		LOGGER.info("Exiting test case...");
	}

}

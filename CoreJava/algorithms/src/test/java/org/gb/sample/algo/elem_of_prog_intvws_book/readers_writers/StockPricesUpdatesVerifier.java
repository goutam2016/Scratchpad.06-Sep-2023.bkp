package org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.junit.Assert;

public final class StockPricesUpdatesVerifier {

	private static final Logger LOGGER = Logger.getLogger(StockPricesUpdatesVerifier.class);
	
	public static void verifyReadersRecordsAgainstWriterRecords(List<Future<RecordedStockPrices>> stockPricesRecordedByAllReadersFutures,
			Future<Map<Integer, StockPrices>> stockPricesRecordedByWriterFuture) throws InterruptedException, ExecutionException {
	
		List<RecordedStockPrices> recordedStockPricesByAllReaders = 
				stockPricesRecordedByAllReadersFutures.stream().map(StockPricesUpdatesVerifier::extractRecordedStockPrices).collect(Collectors.toList());
		
		Set<Entry<Integer, StockPrices>> stockPricesRecordedByWriterEntries = stockPricesRecordedByWriterFuture.get().entrySet();
		
		stockPricesRecordedByWriterEntries.stream().forEach(writerEntry -> compareAgainstAllReadersRecords(writerEntry, recordedStockPricesByAllReaders));	
	}
	
	private static RecordedStockPrices extractRecordedStockPrices(Future<RecordedStockPrices> recordedStockPricesFuture) {
		RecordedStockPrices recordedStockPrices = null;
		try {
			recordedStockPrices = recordedStockPricesFuture.get();
			String readerThreadName = recordedStockPrices.getReaderThreadName();
			Map<Integer, StockPrices> stockPricesPerUpdateRecorded = recordedStockPrices.getStockPricesPerUpdate();
			LOGGER.info(String.format("Reader thread: %s has received %d updates", readerThreadName, stockPricesPerUpdateRecorded.size()));	
		} catch (Exception e) {
			LOGGER.error("Exception caught!", e);
			Assert.fail("Exception caught, test case fails.");
		}
		return recordedStockPrices;
	}
			
	private static void compareAgainstAllReadersRecords(Entry<Integer, StockPrices> stockPricesRecordedByWriterEntry,
			List<RecordedStockPrices> recordedStockPricesByAllReaders) {
		Integer updateIdx = stockPricesRecordedByWriterEntry.getKey();
		StockPrices stockPricesRecordedByWriter = stockPricesRecordedByWriterEntry.getValue();

		LOGGER.info(String.format(
				"Stock prices recorded by writer -- UpdateIdx: %d; Apple: %d, Google: %d, Microsoft: %d, IBM: %d, Oracle: %d, Accenture: %d, Walmart: %d, Tesla: %d, Amazon: %d, Intel: %d",
				updateIdx, stockPricesRecordedByWriter.getAppleStockPrice(), stockPricesRecordedByWriter.getGoogleStockPrice(),
				stockPricesRecordedByWriter.getMicrosoftStockPrice(), stockPricesRecordedByWriter.getIbmStockPrice(),
				stockPricesRecordedByWriter.getOracleStockPrice(), stockPricesRecordedByWriter.getAccentureStockPrice(),
				stockPricesRecordedByWriter.getWalmartStockPrice(), stockPricesRecordedByWriter.getTeslaStockPrice(),
				stockPricesRecordedByWriter.getAmazonStockPrice(), stockPricesRecordedByWriter.getIntelStockPrice()));
		
		for (RecordedStockPrices recordedStockPricesByEachReader : recordedStockPricesByAllReaders) {
			String readerThreadName = recordedStockPricesByEachReader.getReaderThreadName();
			Map<Integer, StockPrices> stockPricesPerUpdateRecordedByEachReader = recordedStockPricesByEachReader.getStockPricesPerUpdate();
			compareAgainstEachReaderRecords(stockPricesRecordedByWriterEntry, readerThreadName, stockPricesPerUpdateRecordedByEachReader);
		}
	}

	private static void compareAgainstEachReaderRecords(Entry<Integer, StockPrices> stockPricesRecordedByWriterEntry, String readerThreadName,
			Map<Integer, StockPrices> stockPricesPerUpdateRecordedByReader) {
		Integer updateIdx = stockPricesRecordedByWriterEntry.getKey();
		StockPrices stockPricesRecordedByWriter = stockPricesRecordedByWriterEntry.getValue();
	
		if (stockPricesPerUpdateRecordedByReader.containsKey(updateIdx)) {
			StockPrices stockPricesRecordedByEachReader = stockPricesPerUpdateRecordedByReader.get(updateIdx);
	
			Assert.assertEquals(stockPricesRecordedByWriter.getAppleStockPrice(), stockPricesRecordedByEachReader.getAppleStockPrice());
			Assert.assertEquals(stockPricesRecordedByWriter.getGoogleStockPrice(), stockPricesRecordedByEachReader.getGoogleStockPrice());
			Assert.assertEquals(stockPricesRecordedByWriter.getMicrosoftStockPrice(), stockPricesRecordedByEachReader.getMicrosoftStockPrice());
			Assert.assertEquals(stockPricesRecordedByWriter.getIbmStockPrice(), stockPricesRecordedByEachReader.getIbmStockPrice());
			Assert.assertEquals(stockPricesRecordedByWriter.getOracleStockPrice(), stockPricesRecordedByEachReader.getOracleStockPrice());
			Assert.assertEquals(stockPricesRecordedByWriter.getAccentureStockPrice(), stockPricesRecordedByEachReader.getAccentureStockPrice());
			Assert.assertEquals(stockPricesRecordedByWriter.getWalmartStockPrice(), stockPricesRecordedByEachReader.getWalmartStockPrice());
			Assert.assertEquals(stockPricesRecordedByWriter.getTeslaStockPrice(), stockPricesRecordedByEachReader.getTeslaStockPrice());
			Assert.assertEquals(stockPricesRecordedByWriter.getAmazonStockPrice(), stockPricesRecordedByEachReader.getAmazonStockPrice());
			Assert.assertEquals(stockPricesRecordedByWriter.getIntelStockPrice(), stockPricesRecordedByEachReader.getIntelStockPrice());
		} else {
			LOGGER.info(String.format("Reader thread: %s has missed updateIdx: %d", readerThreadName, updateIdx));
		}
	
	}
}

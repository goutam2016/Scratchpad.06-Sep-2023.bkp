package org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers;

import java.util.Map;

public class RecordedStockPrices {

	private String readerThreadName;
	private Map<Integer, StockPrices> stockPricesPerUpdate;
	
	public RecordedStockPrices(String readerThreadName, Map<Integer, StockPrices> stockPricesPerUpdate) {
		super();
		this.readerThreadName = readerThreadName;
		this.stockPricesPerUpdate = stockPricesPerUpdate;
	}

	public String getReaderThreadName() {
		return readerThreadName;
	}
	public Map<Integer, StockPrices> getStockPricesPerUpdate() {
		return stockPricesPerUpdate;
	}
}

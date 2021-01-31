package org.gb.sample.algo.elem_of_prog_intvws_book.readers_writers;

public class StockPrices {

	private int appleStockPrice;
	private int googleStockPrice;
	private int microsoftStockPrice;
	private int ibmStockPrice;
	private int oracleStockPrice;
	private int accentureStockPrice;
	private int walmartStockPrice;
	private int teslaStockPrice;
	private int amazonStockPrice;
	private int intelStockPrice;

	public int getAppleStockPrice() {
		return appleStockPrice;
	}

	public void setAppleStockPrice(int appleStockPrice) {
		this.appleStockPrice = appleStockPrice;
	}

	public int getGoogleStockPrice() {
		return googleStockPrice;
	}

	public void setGoogleStockPrice(int googleStockPrice) {
		this.googleStockPrice = googleStockPrice;
	}

	public int getMicrosoftStockPrice() {
		return microsoftStockPrice;
	}

	public void setMicrosoftStockPrice(int microsoftStockPrice) {
		this.microsoftStockPrice = microsoftStockPrice;
	}

	public int getIbmStockPrice() {
		return ibmStockPrice;
	}

	public void setIbmStockPrice(int ibmStockPrice) {
		this.ibmStockPrice = ibmStockPrice;
	}

	public int getOracleStockPrice() {
		return oracleStockPrice;
	}

	public void setOracleStockPrice(int oracleStockPrice) {
		this.oracleStockPrice = oracleStockPrice;
	}

	public int getAccentureStockPrice() {
		return accentureStockPrice;
	}

	public void setAccentureStockPrice(int accentureStockPrice) {
		this.accentureStockPrice = accentureStockPrice;
	}

	public int getWalmartStockPrice() {
		return walmartStockPrice;
	}

	public void setWalmartStockPrice(int walmartStockPrice) {
		this.walmartStockPrice = walmartStockPrice;
	}

	public int getTeslaStockPrice() {
		return teslaStockPrice;
	}

	public void setTeslaStockPrice(int teslaStockPrice) {
		this.teslaStockPrice = teslaStockPrice;
	}

	public int getAmazonStockPrice() {
		return amazonStockPrice;
	}

	public void setAmazonStockPrice(int amazonStockPrice) {
		this.amazonStockPrice = amazonStockPrice;
	}

	public int getIntelStockPrice() {
		return intelStockPrice;
	}

	public void setIntelStockPrice(int intelStockPrice) {
		this.intelStockPrice = intelStockPrice;
	}

	public StockPrices copy() {
		StockPrices stockPricesCopy = new StockPrices();
		stockPricesCopy.setAppleStockPrice(getAppleStockPrice());
		stockPricesCopy.setGoogleStockPrice(getGoogleStockPrice());
		stockPricesCopy.setMicrosoftStockPrice(getMicrosoftStockPrice());
		stockPricesCopy.setIbmStockPrice(getIbmStockPrice());
		stockPricesCopy.setOracleStockPrice(getOracleStockPrice());
		stockPricesCopy.setAccentureStockPrice(getAccentureStockPrice());
		stockPricesCopy.setWalmartStockPrice(getWalmartStockPrice());
		stockPricesCopy.setTeslaStockPrice(getTeslaStockPrice());
		stockPricesCopy.setAmazonStockPrice(getAmazonStockPrice());
		stockPricesCopy.setIntelStockPrice(getIntelStockPrice());

		return stockPricesCopy;
	}
}

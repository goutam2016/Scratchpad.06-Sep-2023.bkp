package org.gb.sample.spark.sql.income;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.functions;
import org.gb.sample.spark.sql.income.Band;

class IncomeBandMapper implements Serializable {

	private static final long serialVersionUID = -4824928956855362703L;

	private Dataset<Row> nameVsIncomeRows;
	private List<Integer> incomeSlabs;

	IncomeBandMapper(Dataset<Row> nameVsIncomeRows, List<Integer> incomeSlabs) {
		this.nameVsIncomeRows = nameVsIncomeRows.toDF("firstName", "lastName", "income");
		this.incomeSlabs = incomeSlabs;
	}

	Map<Band, Long> getCountPerIncomeBand_old() {
		Dataset<Band> incomeBands = nameVsIncomeRows.map(row -> getValue(row, "income"), Encoders.STRING())
				.map(this::mapToBand, Encoders.bean(Band.class)).filter(Objects::nonNull);
		return incomeBands.javaRDD().countByValue();
	}

	Map<Band, Long> getCountPerIncomeBand() {
		List<Row> groupedByBandRows = nameVsIncomeRows
				.withColumn("band", functions.callUDF("mapToBandUDF", nameVsIncomeRows.col("income"))).groupBy("band").count()
				.collectAsList();
		return groupedByBandRows.stream().collect(Collectors.toMap(this::createBand, row -> getValue(row, "count")));
	}

	private <T> T getValue(Row row, String colName) {
		return row.getAs(colName);
	}
	
	private Band createBand(Row row) {
		Row bandAsRow = getValue(row, "band");
		Integer lowerLimit = getValue(bandAsRow, "lowerLimit");
		Integer upperLimit = getValue(bandAsRow, "upperLimit");
		return new Band(lowerLimit, upperLimit);
	}
	
	Row mapToBandUDF(String incomeStr) {
		System.out.println("incomeStr: " + incomeStr);
		Band band = mapToBand(incomeStr);
		
		if(band == null) {
			return null;
		}

		return RowFactory.create(band.getLowerLimit(), band.getUpperLimit());
	}

	private Band mapToBand(String incomeStr) {
		Integer income = null;
		try {
			income = Integer.parseInt(incomeStr);
		} catch (NumberFormatException nfe) {
			System.err.println(String.format("incomeStr: %s could not be converted to an integer", incomeStr));
			return null;
		}

		Band band = null;
		for (int i = 0; i < incomeSlabs.size(); i++) {
			Integer currentSlab = incomeSlabs.get(i);
			Integer nextSlab = null;

			if ((i + 1) < incomeSlabs.size()) {
				nextSlab = incomeSlabs.get(i + 1);
			}

			if (nextSlab == null) {
				if (income.intValue() > currentSlab.intValue()) {
					band = new Band(currentSlab, nextSlab);
					break;
				}
			} else if (income.intValue() > currentSlab.intValue() && income.intValue() <= nextSlab.intValue()) {
				band = new Band(currentSlab, nextSlab);
				break;
			}
		}
		System.out.printf("Income: %d falls in band: %s.\n", income.intValue(), band);
		return band;
	}

}

package org.gb.sample.spark.sql.income;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;

public class IncomeBandMain {

	public static void main(String[] args) {
		SparkSession session = SparkSession.builder().appName("Person count per income band - using Spark SQL")
				.getOrCreate();
		String nameVsIncomeFile = args[0];
		List<Integer> incomeSlabs = Stream.of(args[1].split(",")).map(Integer::parseInt).collect(Collectors.toList());
		Dataset<Row> nameVsIncomeRows = session.read().csv(nameVsIncomeFile);
		IncomeBandMapper incomeBandMapper = new IncomeBandMapper(nameVsIncomeRows, incomeSlabs);
		StructField lowerLimitField = DataTypes.createStructField("lowerLimit", DataTypes.IntegerType, true);
		StructField upperLimitField = DataTypes.createStructField("upperLimit", DataTypes.IntegerType, true);
		StructField[] bandLimitFields = { lowerLimitField, upperLimitField };
		session.udf().register("mapToBandUDF", (String incomeStr) -> incomeBandMapper.mapToBandUDF(incomeStr),
				DataTypes.createStructType(bandLimitFields));
		Map<Band, Long> countPerIncomeBand = incomeBandMapper.getCountPerIncomeBand_old();
		countPerIncomeBand.forEach((band, count) -> System.out.println(band + " : " + count));
		session.close();
	}

}

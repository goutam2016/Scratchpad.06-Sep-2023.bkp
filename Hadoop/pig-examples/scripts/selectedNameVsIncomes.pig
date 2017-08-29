-- pig -x mapreduce -param minIncome=139950 simple-jobs/pig/selectedNameVsIncome.pig

nameVsIncome = LOAD 'pig-examples/input/income/name-vs-income_50000.txt' USING PigStorage(',') AS (firstName:chararray,lastName:chararray,income:int);
namesWithIncomeOverThreshold = FILTER nameVsIncome BY income > 139990;
DUMP namesWithIncomeOverThreshold;
groupedNamesWithIncome = GROUP namesWithIncomeOverThreshold ALL;
recordCount = FOREACH groupedNamesWithIncome GENERATE COUNT(namesWithIncomeOverThreshold);
DUMP recordCount;

-- write a pig script that would find the years when the maximum temperature was at least 35 degrees. Use a UDF to evaluate the max. temperature.   
-- pig -x mapreduce -param minIncome=139950 simple-jobs/pig/selectedNameVsIncome.pig

nameVsIncome = LOAD 'pig-examples/data/income/name-vs-income_50000.txt' USING PigStorage(',') AS (firstName:chararray,lastName:chararray,income:int);
namesWithIncomeOverThreshold = FILTER nameVsIncome BY income > $minIncome;
DUMP namesWithIncomeOverThreshold;
groupedNamesWithIncome = GROUP namesWithIncomeOverThreshold ALL;
recordCount = FOREACH groupedNamesWithIncome GENERATE COUNT(namesWithIncomeOverThreshold);
DUMP recordCount;

-- pig -x local -param_file pig-examples/scripts/countPerIncomeBand.param.local pig-examples/scripts/countPerIncomeBand.pig 
-- pig -x mapreduce -param_file pig-examples/scripts/countPerIncomeBand.param.mr pig-examples/scripts/countPerIncomeBand.pig 

REGISTER pig-examples/target/pig-examples-0.0.1-SNAPSHOT.jar;
DEFINE incomeBand org.gb.sample.pig.income.IncomeBandFinder('$incomeSlabs');

nameVsIncome = LOAD '$nameVsIncomeDataFile' USING PigStorage(',') AS (firstName:chararray,lastName:chararray,income:int);
nameVsIncomeBands = FOREACH nameVsIncome GENERATE incomeBand(income) as incomeBand, firstName, lastName;
groupedNameVsIncomeBands = GROUP nameVsIncomeBands by incomeBand;
countPerIncomeBand = FOREACH groupedNameVsIncomeBands GENERATE group, COUNT(nameVsIncomeBands);
-- Delete the output directory if it already exists.
RMF $outputPath
STORE countPerIncomeBand INTO '$outputPath' USING PigStorage(':');

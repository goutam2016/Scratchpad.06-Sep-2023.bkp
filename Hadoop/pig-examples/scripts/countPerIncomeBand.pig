-- pig -x local pig-examples/scripts/countPerIncomeBand.pig

REGISTER pig-examples/target/pig-examples-0.0.1-SNAPSHOT.jar;
DEFINE incomeBand org.gb.sample.pig.income.IncomeBandFinder();

nameVsIncome = LOAD 'pig-examples/data/income/name-vs-income_500.txt' USING PigStorage(',') AS (firstName:chararray,lastName:chararray,income:int);
nameVsIncomeBands = FOREACH nameVsIncome GENERATE incomeBand(income) as incomeBand, firstName, lastName;
groupedNameVsIncomeBands = GROUP nameVsIncomeBands by incomeBand;
countPerIncomeBand = FOREACH groupedNameVsIncomeBands GENERATE group, COUNT(nameVsIncomeBands);
-- Delete the output directory if it already exists.
rmf pig-examples/data/income/output
STORE countPerIncomeBand INTO 'pig-examples/data/income/output/' USING PigStorage(':');

To run a benchmark test
-------------------------------
mvn exec:exec -DmainClass="org.gb.sample.algo.jmh.FibonacciBenchmark"
mvn exec:exec -DmainClass="org.gb.sample.algo.matrix.jmh.TraverserBenchmark"
mvn exec:exec -DmainClass="org.gb.sample.algo.permutation.PermutationsBenchmark"

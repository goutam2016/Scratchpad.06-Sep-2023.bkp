To run a benchmark test
-------------------------------
mvn exec:exec -DmainClass="org.gb.sample.algo.FibonacciBenchmark"
mvn exec:exec -DmainClass="org.gb.sample.algo.matrix.TraverserBenchmark"
mvn exec:exec -DmainClass="org.gb.sample.algo.permutation.PermutationsBenchmark"
mvn exec:exec -DmainClass="org.gb.sample.algo.intro_to_algo_book.matrix_multiplication.SquareMatrixMultiplierBenchmark"
mvn exec:exec -DmainClass="org.gb.sample.algo.intro_to_algo_book.matrix_multiplication.MatrixChainMultiplierBenchmark"

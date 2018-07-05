To run a benchmark test
-------------------------------
iterations: 5, warm-up iterations: 5, fork on each benchmark: 1, threads: 1
sbt jmh:run -i 5 -wi 5 -f1 -t1

package org.gb.sample.scala

import scala.collection.mutable.IndexedSeq
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.annotations.Fork

@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
class PermutationsBenchmark {

    @Benchmark
    def permute(): Unit = {
        val characters = IndexedSeq('a', 'b', 'c', 'd', 'e', 'f', 'g')
        val permutations = Permutations.permute(characters)
        printf("Number of permutations: %d\n", permutations.size)
    }
}
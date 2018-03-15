package org.gb.sample.scala

import scala.collection.mutable.IndexedSeq
import org.scalatest.FunSuite

class PermutationsTest extends FunSuite {
    test("permutations of 5 distinct items") {
        val characters = IndexedSeq('a', 'b', 'c', 'd', 'e')
        val permutations = Permutations.permute(characters);
        assert(permutations.size == 120)
    }
}
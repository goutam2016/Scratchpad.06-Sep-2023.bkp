package org.gb.sample.scala

import scala.collection.mutable.TreeSet
import scala.collection.mutable.Set
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.IndexedSeq

object Permutations {

    def permute(characters: IndexedSeq[Char]): Set[String] = {
        val permutations = TreeSet[String]()

        if (characters.size == 1) {
            val alignment = String.valueOf(characters(0))
            permutations.add(alignment)
        } else if (characters.size == 2) {
            val forwardAlignment = String.valueOf(characters(0)).concat(String.valueOf(characters(1)))
            val reverseAlignment = String.valueOf(characters(1)).concat(String.valueOf(characters(0)))
            permutations.add(forwardAlignment)
            permutations.add(reverseAlignment)
        } else {
            for (charIdx <- 0 until (characters.size)) {
                val permutableCharacters = ArrayBuffer.concat(characters)
                val fixedCharacter = permutableCharacters.remove(charIdx)

                val partialPermutations = permute(permutableCharacters)
                for (partialPermutation <- partialPermutations) {
                    val permutation = String.valueOf(fixedCharacter).concat(partialPermutation)
                    permutations.add(permutation)
                }
            }
        }

        return permutations
    }

    def main(args: Array[String]): Unit = {
        val characters = IndexedSeq('a', 'b', 'c', 'd', 'e', 'f', 'g')
        val permutations = permute(characters)
        printf("Number of permutations: %d\n", permutations.size)
    }
}
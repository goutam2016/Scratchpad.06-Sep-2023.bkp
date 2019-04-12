package org.gb.sample.scala.hackerrank.candies

object Solution {
    private def incrementRight(segmentRatings: Array[Int], minRating: Int, minCandies: Int, segmentCandies: Array[Int], errorIdx: Int): Unit = {
        segmentCandies(errorIdx) = segmentCandies(errorIdx) + 1

        val ratingAndCandiesChecker = (idx: Int) => {
            val rating = segmentRatings(idx)
            val ratingRight = if (idx == segmentRatings.size - 1) minRating else segmentRatings(idx + 1)
            val candies = segmentCandies(idx)
            val candiesRight = if (idx == segmentRatings.size - 1) minCandies else segmentCandies(idx + 1)
            ratingRight > rating && candiesRight <= candies
        }

        Range(errorIdx, segmentRatings.size).toStream.takeWhile(ratingAndCandiesChecker).foreach(idx => segmentCandies.update(idx + 1, segmentCandies(idx) + 1))
    }

    private def incrementLeft(segmentRatings: Array[Int], minRating: Int, minCandies: Int, segmentCandies: Array[Int], errorIdx: Int): Unit = {
        segmentCandies(errorIdx) = segmentCandies(errorIdx) + 1

        val ratingAndCandiesChecker = (idx: Int) => {
            val rating = segmentRatings(idx)
            val ratingLeft = if (idx == 0) minRating else segmentRatings(idx - 1)
            val candies = segmentCandies(idx)
            val candiesLeft = if (idx == 0) minCandies else segmentCandies(idx - 1)
            ratingLeft > rating && candiesLeft <= candies
        }

        Range(errorIdx, -1, -1).toStream.takeWhile(ratingAndCandiesChecker).foreach(idx => segmentCandies.update(idx - 1, segmentCandies(idx) + 1))
    }

    private def distributeLeft(ratings: Array[Int], segmentStartIdx: Int, segmentEndIdx: Int, minRating: Int, minCandies: Int): Array[Int] = {
        val segmentRatings = ratings.slice(segmentStartIdx, segmentEndIdx)
        val segmentCandies = segmentRatings.map(_ => -1)
        
        val allocateCandies = (idx: Int) => {
            val ratingRight = if (idx == segmentRatings.size - 1) minRating else segmentRatings(idx + 1)
            val candiesRight = if (idx == segmentRatings.size - 1) minCandies else segmentCandies(idx + 1)

            val rating = segmentRatings(idx)
            val candies = if (rating > ratingRight) candiesRight + 1 else if (candiesRight - 1 > minCandies) minCandies else candiesRight - 1
            segmentCandies(idx) = candies

            if (candies == 0) {
                incrementRight(segmentRatings, minRating, minCandies, segmentCandies, idx)
            }
        }
        
        Range(segmentRatings.size - 1, -1, -1).toStream.foreach(allocateCandies)

        return segmentCandies
    }

    private def distributeRight(ratings: Array[Int], segmentStartIdx: Int, segmentEndIdx: Int, minRating: Int, minCandies: Int): Array[Int] = {
        val segmentRatings = ratings.slice(segmentStartIdx, segmentEndIdx)
        val segmentCandies = segmentRatings.map(_ => -1)
        val segmentEndsWithMinRating = segmentEndIdx < ratings.size

        val allocateCandies = (idx: Int) => {
            val ratingLeft = if (idx == 0) minRating else segmentRatings(idx - 1)
            val candiesLeft = if (idx == 0) minCandies else segmentCandies(idx - 1)

            val rating = segmentRatings(idx)
            val candies = if (rating > ratingLeft) candiesLeft + 1 else if (candiesLeft - 1 > minCandies) minCandies else candiesLeft - 1
            segmentCandies(idx) = candies

            if (candies == 0) {
                incrementLeft(segmentRatings, minRating, minCandies, segmentCandies, idx)
            }
            
            if (segmentEndsWithMinRating && idx == segmentRatings.size - 1 && segmentCandies(idx) == minCandies) {
                incrementLeft(segmentRatings, minRating, minCandies, segmentCandies, idx)
            }
        }

        Range(0, segmentRatings.size).toStream.foreach(allocateCandies)

        return segmentCandies
    }

    def distributeCandies(ratings: Array[Int]): Long = {
        val minRating = ratings.min
        val minCandies = 1

        val minRatingIndices = ratings.zipWithIndex.filter(_._1 == minRating).map(_._2)
        val minRating1stIdx = minRatingIndices.head

        val leftSgmtCandies = if (minRating1stIdx > 0) distributeLeft(ratings, 0, minRating1stIdx, minRating, minCandies) else Array.emptyIntArray

        val segmentStartEndFinder = (minRatingIdxCtr: Int) => {
            val segmentStartIdx = minRatingIndices(minRatingIdxCtr) + 1
            val segmentEndIdx = if (minRatingIdxCtr == minRatingIndices.size - 1) ratings.size else minRatingIndices(minRatingIdxCtr + 1)
            (segmentStartIdx, segmentEndIdx)
        }

        val segmentStartEndIdxTuples = Range(0, minRatingIndices.size).map(segmentStartEndFinder)
        val sgmtCandies = segmentStartEndIdxTuples.map(segmentStartEndIdxTuple => distributeRight(ratings, segmentStartEndIdxTuple._1, segmentStartEndIdxTuple._2, minRating, 1))

        val insertMinCandies = (allSgmtCandies: Seq[Array[Int]], sgmtIdx: Int) => allSgmtCandies :+ Array[Int](minCandies) :+ sgmtCandies(sgmtIdx)

        val allSgmtCandiesWithMins = Range(0, minRatingIndices.size).foldLeft(Seq[Array[Int]](leftSgmtCandies))(insertMinCandies)
        val distributedCandies = allSgmtCandiesWithMins.flatten.map(_.toLong)

        return distributedCandies.sum
    }
}
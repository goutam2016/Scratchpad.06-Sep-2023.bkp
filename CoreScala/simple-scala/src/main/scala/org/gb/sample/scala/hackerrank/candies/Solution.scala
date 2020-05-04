package org.gb.sample.scala.hackerrank.candies

object Solution {
    private def incrementBack(segmentRatings: Array[Int], minRating: Int, minCandies: Int, segmentCandies: Array[Int], beginIdx: Int, errorIdx: Int): Unit = {
        segmentCandies(errorIdx) = segmentCandies(errorIdx) + 1

        val ratingAndCandiesChecker = (idx: Int) => {
            val prevIdx = if (beginIdx == 0) idx - 1 else idx + 1
            val rating = segmentRatings(idx)
            val prevRating = if (idx == beginIdx) minRating else segmentRatings(prevIdx)
            val candies = segmentCandies(idx)
            val prevCandies = if (idx == beginIdx) minCandies else segmentCandies(prevIdx)
            prevRating > rating && prevCandies <= candies
        }

        /*
         * When beginIdx = 0, it means we are distributing right and need to back adjust left.
         * When beginIdx > 0, it means we are distributing left and need to back adjust right.
         */
        if (beginIdx == 0) {
            Range(errorIdx, -1, -1).toStream.takeWhile(ratingAndCandiesChecker).foreach(idx => segmentCandies.update(idx - 1, segmentCandies(idx) + 1))
        } else {
            Range(errorIdx, segmentRatings.size).toStream.takeWhile(ratingAndCandiesChecker).foreach(idx => segmentCandies.update(idx + 1, segmentCandies(idx) + 1))
        }
    }
    
    private def allocateCandiesAtCurrIdx(segmentRatings: Array[Int], segmentCandies: Array[Int], minRating: Int, minCandies: Int, beginIdx: Int, currIdx: Int): Unit = {
        /*
         * When beginIdx = 0, it means we are distributing rightwards.
         * When beginIdx > 0, it means we are distributing leftwards.
         */
        val prevIdx = if (beginIdx == 0) currIdx - 1 else currIdx + 1
        val prevRating = if (currIdx == beginIdx) minRating else segmentRatings(prevIdx)
        val prevCandies = if (currIdx == beginIdx) minCandies else segmentCandies(prevIdx)

        val rating = segmentRatings(currIdx)
        val candies = if (rating > prevRating) prevCandies + 1 else if (prevCandies - 1 > minCandies) minCandies else prevCandies - 1
        segmentCandies(currIdx) = candies

        if (candies == 0) {
            incrementBack(segmentRatings, minRating, minCandies, segmentCandies, beginIdx, currIdx)
        }
    }

    /*
     * Take one segment and distribute candies to each index one by one towards the left, until index 0 is reached, which ends the segment.
     */
    private def distributeLeft(ratings: Array[Int], segmentStartIdx: Int, segmentEndIdx: Int, minRating: Int, minCandies: Int): Array[Int] = {
        val segmentRatings = ratings.slice(segmentStartIdx, segmentEndIdx)
        val segmentCandies = segmentRatings.map(_ => -1)

        val allocateCandies = (idx: Int) => allocateCandiesAtCurrIdx(segmentRatings, segmentCandies, minRating, minCandies, segmentRatings.size - 1, idx)

        Range(segmentRatings.size - 1, -1, -1).toStream.foreach(allocateCandies)

        return segmentCandies
    }

    /*
     * Take one segment and distribute candies to each index one by one towards the right, until a minimum rating is reached, which ends the segment.
     */
    private def distributeRight(ratings: Array[Int], segmentStartIdx: Int, segmentEndIdx: Int, minRating: Int, minCandies: Int): Array[Int] = {
        val segmentRatings = ratings.slice(segmentStartIdx, segmentEndIdx)
        val segmentCandies = segmentRatings.map(_ => -1)
        val segmentEndsWithMinRating = segmentEndIdx < ratings.size

        val allocateCandies = (idx: Int) => {
            allocateCandiesAtCurrIdx(segmentRatings, segmentCandies, minRating, minCandies, 0, idx)

            if (segmentEndsWithMinRating && idx == segmentRatings.size - 1 && segmentCandies(idx) == minCandies) {
                incrementBack(segmentRatings, minRating, minCandies, segmentCandies, 0, idx)
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
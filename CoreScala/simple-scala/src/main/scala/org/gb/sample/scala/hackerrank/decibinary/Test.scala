package org.gb.sample.scala.hackerrank.decibinary

object Test {
    def main(args: Array[String]): Unit = {
        var noOfMismatches = 0
        for(index <- 1 to 10000) {
            val result1 = Solution2.computeDecibinaryNumber(index)
            val result2 = Solution3.computeDecibinaryNumber(index)
            
            if(result1 != result2) {
                println(s"index: ${index} has a mismtach, result1: ${result1}, result2: ${result2}")
                noOfMismatches += 1
            } 
        }
        println(s"Total no. of mismatches: ${noOfMismatches}")
    }
}
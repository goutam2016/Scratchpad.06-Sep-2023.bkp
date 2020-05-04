package org.gb.sample.scala

object PartFunc {
    def main(args: Array[String]): Unit = {
        val convert1to5 = new PartialFunction[Int, String] {
            val nums = Array("one", "two", "three", "four", "five")
            def apply(i: Int) = nums(i - 1)
            def isDefinedAt(i: Int) = i > 0 && i < 6
        }
        val convert6to10 = new PartialFunction[Int, String] {
            val nums = Array("six", "seven", "eight", "nine", "ten")
            def apply(i: Int) = nums(i - 6)
            def isDefinedAt(i: Int) = i > 5 && i < 11
        }
        val convert11to15 = new PartialFunction[Int, String] {
            val nums = Array("eleven", "twelve", "thirteen", "fourteen", "fifteen")
            def apply(i: Int) = nums(i - 11)
            def isDefinedAt(i: Int) = i > 11 && i < 16
        }

        val convert1to15 = convert1to5.orElse(convert6to10).orElse(convert11to15)
        val number = 15
        println(s"${number}: ${convert1to15(number)}")

        val greaterThan20: PartialFunction[Any, Int] = {
            case i: Int if i > 20 => i
        }

        val colltdNumbers = List(1, 45, 10, "blah", true, 25).collect(greaterThan20)
        println(colltdNumbers)

        //Currying example.
        val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val numberFunc = numbers.foldLeft(0)_

        val sum = numberFunc((s, n) => s + n)
        println(s"sum: ${sum}")

        val sqrSum = numberFunc((s, n) => s + n * n)
        println(s"sqrSum: ${sqrSum}")
    }
}

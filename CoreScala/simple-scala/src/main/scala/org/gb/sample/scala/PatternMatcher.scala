package org.gb.sample.scala

object PatternMatcher {

  def main(args: Array[String]): Unit = {
    val resp1 = echoWhatYouGaveMe(List(1))
    println(resp1)
    val resp2 = echoWhatYouGaveMe(Person("Gumli", "Rani"))
    println(resp2)
    val resp3 = echoWhatYouGaveMe(Dog("Suka"))
    println(resp3)
    val resp4 = echoWhatYouGaveMe(Dog("Lalu"))
    println(resp4)
    val resp5 = echoWhatYouGaveMe(List(0, "1st", "2nd"))
    println(resp5)
    val resp6 = echoWhatYouGaveMe(List(10, 20, 30, 40, 50))
    println(resp6)
  }

  def echoWhatYouGaveMe(x: Any): String = x match {
    case 0                           => "zero"
    case true                        => "true"
    case "hello"                     => "You said 'hello'"
    case List(0, _, _)               => "A 3 element list with 0 as first element"
    case List(1, _*)                 => "A list beginning with 1, having any number of elements"
    case list @ List(_*)             => s"A list: $list"
    case Person(firstName, lastName) => s"A person with name: $firstName $lastName."
    case Dog("Suka")                 => "A dog named Suka"
    case d: Dog                      => s"A dog named ${d.name}"
    case _                           => "Unknown"
  }
}

case class Person(firstName: String, lastName: String)
case class Dog(name: String)
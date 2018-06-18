package org.gb.sample.spark.income

case class PersonName(firstPart: String, lastPart: String)

case class Band(lowerLimit: Int, upperLimit: Int)

class PersonProfile(val firstName: String, val lastName: String, val companyName: String, address: String,
    city: String, county: String, postCode: String, phoneNumber1: String, phoneNumber2: String,
    val emailAddress: String, website: String) extends Serializable {

}

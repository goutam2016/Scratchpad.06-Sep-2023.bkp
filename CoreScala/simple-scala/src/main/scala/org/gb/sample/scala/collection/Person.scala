package org.gb.sample.scala.collection

import java.util.Date
import org.gb.sample.scala.collection.Sex.Sex

abstract class Person(firstName: String, lastName: String, dateOfBirth: Date = null, residenceAddress: String = null, motherTongue: String) {

    def getFirstName(): String = {
        firstName
    }
    def getLastName(): String = {
        lastName
    }
    def getDateOfBirth(): Date = {
        dateOfBirth
    }
    def getResidenceAddress(): String = {
        residenceAddress
    }
    def getMotherTongue(): String = {
        motherTongue
    }
}

object Sex extends Enumeration {
    type Sex = Value
    val MALE, FEMALE = Value
}

class Employee(firstName: String, lastName: String, dateOfBirth: Date = null, residenceAddress: String = null, motherTongue: String,
               sex: Sex.Value, joiningDate: Date, jobTitle: String, department: String, highestAcademicQlfn: String = null,
               yearlyGrossSalary: BigDecimal)
        extends Person(firstName, lastName, dateOfBirth, residenceAddress, motherTongue) {

    def getSex(): Sex.Value = {
        sex
    }
    def getJoiningDate(): Date = {
        joiningDate
    }
    def getJobTitle(): String = {
        jobTitle
    }
    def getDepartment(): String = {
        department
    }
    def getHighestAcademicQlfn(): String = {
        highestAcademicQlfn
    }
    def getYearlyGrossSalary(): BigDecimal = {
        yearlyGrossSalary
    }
}
package org.gb.sample.scala.collection

import scala.collection.immutable.IndexedSeq
import scala.collection.immutable.Map
import java.util.Date

object PersonOperations {
    def groupByMotherTongue(people: IndexedSeq[Person]): Map[String, IndexedSeq[Person]] = {
        people.groupBy(_.getMotherTongue())
    }

    def filterEmployeesJoinedAfter(employees: IndexedSeq[Employee], joiningDate: Date): IndexedSeq[Employee] = {
        employees.filter(_.getJoiningDate() != null).filter(_.getJoiningDate().after(joiningDate))
    }

    def getTotalExperienceOfAllEmployees(employees: IndexedSeq[Employee]): Long = {
        val millisPerDay = 24 * 60 * 60 * 1000
        employees.filter(_.getJoiningDate() != null).map(System.currentTimeMillis() - _.getJoiningDate().getTime).map(_ / millisPerDay).sum
    }
}
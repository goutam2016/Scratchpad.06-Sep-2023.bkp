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

  def getTotalExperienceOfFemaleEmployees(employees: IndexedSeq[Employee]): Long = {
    val millisPerDay = 24 * 60 * 60 * 1000
    employees.filter(_.getSex() == Sex.FEMALE).filter(_.getJoiningDate() != null).map(System.currentTimeMillis() - _.getJoiningDate().getTime).map(_ / millisPerDay).sum
  }

  def maxSalaryPerDept(employees: IndexedSeq[Employee]): Map[String, BigDecimal] = {
    employees.groupBy(_.getDepartment()).view.mapValues(_.map(_.getYearlyGrossSalary())).mapValues(_.max).toMap
  }

  def empNameWithMaxSalaryPerDept(employees: IndexedSeq[Employee]): Map[String, String] = {
    employees.groupBy(_.getDepartment()).view.mapValues(_.maxBy(_.getYearlyGrossSalary())).
      mapValues(emp => String.join(" ", emp.getFirstName(), emp.getLastName())).toMap
  }

  def employeeCountPerSexPerDept(employees: IndexedSeq[Employee], baseSalary: BigDecimal): Map[String, Map[String, Int]] = {
    val sexClassifier = (emp: Employee) => emp.getSex() match {
      case Sex.MALE => "MALE_COUNT"
      case Sex.FEMALE => "FEMALE_COUNT"
    }
    val employeesPerSexPerDept = employees.filter(_.getYearlyGrossSalary() >= baseSalary).groupBy(_.getDepartment()).view.mapValues(_.groupBy(sexClassifier))
    employeesPerSexPerDept.mapValues(_.view.mapValues(_.size).toMap).toMap
  }

  def averageSalaryPerDepartment(employees: IndexedSeq[Employee]): Map[String, BigDecimal] = {
    val salaryAdder = (salary1: BigDecimal, salary2: BigDecimal) => salary1 + salary2
    val summedSalaryPerDept = employees.groupBy(_.getDepartment()).mapValues(_.map(_.getYearlyGrossSalary())).
      mapValues(salaries => Tuple2(salaries.size, salaries.reduce(salaryAdder)))
    summedSalaryPerDept.mapValues(salaryCntVsSummedSalary => salaryCntVsSummedSalary._2 / BigDecimal.valueOf(salaryCntVsSummedSalary._1)).toMap
  }
}

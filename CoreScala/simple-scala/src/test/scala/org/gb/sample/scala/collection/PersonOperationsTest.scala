package org.gb.sample.scala.collection

import org.scalatest.FunSuite
import scala.collection.immutable.IndexedSeq
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.math.BigDecimal

class PersonOperationsTest extends FunSuite {
    val dateFormatter: DateFormat = new SimpleDateFormat("dd/MM/yyyy");

    test("people are grouped by mother tongue") {
        val people = loadPeople()
        val peopleByMotherTongue = PersonOperations.groupByMotherTongue(people)
        val englishPeople = peopleByMotherTongue.get("English");
        val punjabiPeople = peopleByMotherTongue.get("Punjabi");
        val russianPeople = peopleByMotherTongue.get("Russian");
        val konkaniPeople = peopleByMotherTongue.get("Konkani");
        println(peopleByMotherTongue)
        assertResult(5)(englishPeople.get.size)
        assertResult(2)(punjabiPeople.get.size)
        assertResult(1)(russianPeople.get.size)
        assertResult(1)(konkaniPeople.get.size)
    }

    private def loadPeople(): IndexedSeq[Person] = {
        val emp1 = new Employee(firstName = "Laura", lastName = "Edwards", motherTongue = "English",
            sex = Sex.FEMALE, joiningDate = convertToDate("12/05/2013"), jobTitle = "Business Analyst", department = "Operations",
            yearlyGrossSalary = new BigDecimal(45000.00))
        val emp2 = new Employee(firstName = "Arun", lastName = "Ranganathan", motherTongue = "Tamil",
            sex = Sex.MALE, joiningDate = convertToDate("15/12/2014"), jobTitle = "Director, Software Engineering", department = "Development",
            yearlyGrossSalary = new BigDecimal(80000.00))
        val emp3 = new Employee(firstName = "Chandra", lastName = "Bhadraraju", motherTongue = "Telegu",
            sex = Sex.MALE, joiningDate = convertToDate("06/07/2012"), jobTitle = "Developer", department = "Development",
            yearlyGrossSalary = new BigDecimal(45000.00))
        val emp4 = new Employee(firstName = "Daeem", lastName = "Parker", motherTongue = "Konkani",
            sex = Sex.MALE, joiningDate = convertToDate("03/07/2013"), jobTitle = "Senior Developer", department = "Development",
            yearlyGrossSalary = new BigDecimal(52000.00))
        val emp5 = new Employee(firstName = "Mehul", lastName = "Khandarkar", motherTongue = "Gujrati",
            sex = Sex.MALE, joiningDate = convertToDate("21/03/2013"), jobTitle = "Business Analyst", department = "Operations",
            yearlyGrossSalary = new BigDecimal(50000.00))
        val emp6 = new Employee(firstName = "Mehmet", lastName = "Divilioglu", motherTongue = "Turkish",
            sex = Sex.MALE, joiningDate = convertToDate("10/05/2015"), jobTitle = "Senior Developer", department = "Development",
            yearlyGrossSalary = new BigDecimal(50000.00))
        val emp7 = new Employee(firstName = "Faisal", lastName = "Kamal", motherTongue = "Bengali",
            sex = Sex.MALE, joiningDate = convertToDate("12/08/2009"), jobTitle = "Senior Support Engineer", department = "IT",
            yearlyGrossSalary = new BigDecimal(46000.00))
        val emp8 = new Employee(firstName = "Kate", lastName = "Avery", motherTongue = "English",
            sex = Sex.FEMALE, joiningDate = convertToDate("12/08/2009"), jobTitle = "HR Manager", department = "HR",
            yearlyGrossSalary = new BigDecimal(70000.00))
        val emp9 = new Employee(firstName = "Robdeep", lastName = "Sangha", motherTongue = "Punjabi",
            sex = Sex.MALE, joiningDate = convertToDate("16/07/2012"), jobTitle = "Sales Account Manager", department = "Marketing",
            yearlyGrossSalary = new BigDecimal(58000.00))
        val emp10 = new Employee(firstName = "Andrey", lastName = "Perverzin", motherTongue = "Russian",
            sex = Sex.MALE, joiningDate = convertToDate("22/01/2015"), jobTitle = "Senior Developer", department = "Development",
            yearlyGrossSalary = new BigDecimal(55000.00))
        val emp11 = new Employee(firstName = "Velmurugan", lastName = "Muthu", motherTongue = "Tamil",
            sex = Sex.MALE, joiningDate = convertToDate("04/01/2011"), jobTitle = "Development Manager", department = "Development",
            yearlyGrossSalary = new BigDecimal(58000.00))
        val emp12 = new Employee(firstName = "Nisha", lastName = "Vaswani", motherTongue = "Hindi",
            sex = Sex.FEMALE, joiningDate = convertToDate("11/03/2015"), jobTitle = "Developer", department = "Development",
            yearlyGrossSalary = new BigDecimal(40000.00))
        val emp13 = new Employee(firstName = "Adarshpal", lastName = "Brar", motherTongue = "Punjabi",
            sex = Sex.MALE, joiningDate = convertToDate("08/06/2015"), jobTitle = "Developer", department = "Development",
            yearlyGrossSalary = new BigDecimal(42000.00))
        val emp14 = new Employee(firstName = "Riina", lastName = "Sulonen", motherTongue = "English",
            sex = Sex.FEMALE, joiningDate = convertToDate("11/05/2015"), jobTitle = "Manager", department = "Finance",
            yearlyGrossSalary = new BigDecimal(65000.00))
        val emp15 = new Employee(firstName = "Megha", lastName = "Shah", motherTongue = "Gujrati",
            sex = Sex.FEMALE, joiningDate = convertToDate("18/03/2013"), jobTitle = "Marketing Specialist", department = "Marketing",
            yearlyGrossSalary = new BigDecimal(48000.00))
        val emp16 = new Employee(firstName = "Joanna", lastName = "Ashton", motherTongue = "English",
            sex = Sex.FEMALE, joiningDate = convertToDate("20/01/2014"), jobTitle = "Team Lead", department = "Marketing",
            yearlyGrossSalary = new BigDecimal(65000.00))
        val emp17 = new Employee(firstName = "Kaori", lastName = "Reynolds", motherTongue = "Japanese",
            sex = Sex.FEMALE, joiningDate = convertToDate("10/02/2012"), jobTitle = "Administration Assistant", department = "Office Management",
            yearlyGrossSalary = new BigDecimal(52000.00))
        val emp18 = new Employee(firstName = "Bonnie", lastName = "MacArthur", motherTongue = "English",
            sex = Sex.FEMALE, joiningDate = convertToDate("08/10/2014"), jobTitle = "Mgr, Customer Service", department = "Operations",
            yearlyGrossSalary = new BigDecimal(52000.00))
        val emp19 = new Employee(firstName = "Chaminda", lastName = "Gunarathne", motherTongue = "Sinhalese",
            sex = Sex.MALE, joiningDate = convertToDate("23/06/2014"), jobTitle = "Information Systems Engineer", department = "IT",
            yearlyGrossSalary = new BigDecimal(45000.00))
        val emp20 = new Employee(firstName = "Manuel", lastName = "Moreno", motherTongue = "Spanish",
            sex = Sex.MALE, joiningDate = convertToDate("05/02/2014"), jobTitle = "Fraud Specialist", department = "Operations",
            yearlyGrossSalary = new BigDecimal(51000.00))

        IndexedSeq(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9, emp10, emp11, emp12, emp13, emp14, emp15, emp16, emp17, emp18, emp19, emp20)
    }

    private def convertToDate(dateStr: String): Date = {
        dateFormatter.parse(dateStr)
    }
}

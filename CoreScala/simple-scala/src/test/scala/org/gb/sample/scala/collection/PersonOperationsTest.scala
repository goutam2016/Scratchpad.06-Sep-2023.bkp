package org.gb.sample.scala.collection

import org.scalatest.funsuite.AnyFunSuite
import scala.collection.immutable.IndexedSeq
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.math.BigDecimal

class PersonOperationsTest extends AnyFunSuite {
    val dateFormatter: DateFormat = new SimpleDateFormat("dd/MM/yyyy");

    test("people grouped by mother tongue") {
        val people = loadEmployees()
        val peopleByMotherTongue = PersonOperations.groupByMotherTongue(people)
        val englishPeople = peopleByMotherTongue.get("English");
        val punjabiPeople = peopleByMotherTongue.get("Punjabi");
        val russianPeople = peopleByMotherTongue.get("Russian");
        val konkaniPeople = peopleByMotherTongue.get("Konkani");
        assertResult(5)(englishPeople.get.size)
        assertResult(2)(punjabiPeople.get.size)
        assertResult(1)(russianPeople.get.size)
        assertResult(1)(konkaniPeople.get.size)
    }

    test("maximum salary per department") {
        val employees = loadEmployees()
        val maxSalaryPerDept = PersonOperations.maxSalaryPerDept(employees)
        val maxSalaryOfHR = maxSalaryPerDept.get("HR").get
        val maxSalaryOfDev = maxSalaryPerDept.get("Development").get
        val maxSalaryOfOps = maxSalaryPerDept.get("Operations").get
        val maxSalaryOfMktg = maxSalaryPerDept.get("Marketing").get
        val maxSalaryOfFin = maxSalaryPerDept.get("Finance").get
        val maxSalaryOfOffMgmt = maxSalaryPerDept.get("Office Management").get
        val maxSalaryOfIT = maxSalaryPerDept.get("IT").get
        assertResult(70000)(maxSalaryOfHR.intValue)
        assertResult(80000)(maxSalaryOfDev.intValue)
        assertResult(70000)(maxSalaryOfOps.intValue)
        assertResult(65000)(maxSalaryOfMktg.intValue)
        assertResult(65000)(maxSalaryOfFin.intValue)
        assertResult(52000)(maxSalaryOfOffMgmt.intValue)
        assertResult(46000)(maxSalaryOfIT.intValue)
    }

    test("employee name with maximum salary per department") {
        val employees = loadEmployees()
        val empNameWithMaxSalaryPerDept = PersonOperations.empNameWithMaxSalaryPerDept(employees)
        assertResult("Kate Avery")(empNameWithMaxSalaryPerDept.get("HR").get);
        assertResult("Arun Ranganathan")(empNameWithMaxSalaryPerDept.get("Development").get);
        assertResult("Bonnie MacArthur")(empNameWithMaxSalaryPerDept.get("Operations").get);
        assertResult("Riina Sulonen")(empNameWithMaxSalaryPerDept.get("Marketing").get);
        assertResult("Megha Shah")(empNameWithMaxSalaryPerDept.get("Finance").get);
        assertResult("Kaori Reynolds")(empNameWithMaxSalaryPerDept.get("Office Management").get);
        assertResult("Faisal Kamal")(empNameWithMaxSalaryPerDept.get("IT").get);
    }

    test("employee count per sex per department above a base salary") {
        val employees = loadEmployees()
        val employeeCountPerSexPerDept = PersonOperations.employeeCountPerSexPerDept(employees, new BigDecimal(30000.00))
        val empCountPerSexOfHR = employeeCountPerSexPerDept.get("HR").get
        val empCountPerSexOfDev = employeeCountPerSexPerDept.get("Development").get
        val empCountPerSexOfOps = employeeCountPerSexPerDept.get("Operations").get
        val empCountPerSexOfMktg = employeeCountPerSexPerDept.get("Marketing").get
        val empCountPerSexOfFin = employeeCountPerSexPerDept.get("Finance").get
        val empCountPerSexOfOffMgmt = employeeCountPerSexPerDept.get("Office Management").get
        val empCountPerSexOfIT = employeeCountPerSexPerDept.get("IT").get
        assertResult(1)(empCountPerSexOfHR.get("FEMALE_COUNT").get)
        assertResult(1)(empCountPerSexOfDev.get("FEMALE_COUNT").get)
        assertResult(7)(empCountPerSexOfDev.get("MALE_COUNT").get)
        assertResult(2)(empCountPerSexOfOps.get("FEMALE_COUNT").get)
        assertResult(2)(empCountPerSexOfOps.get("MALE_COUNT").get)
        assertResult(2)(empCountPerSexOfMktg.get("FEMALE_COUNT").get)
        assertResult(1)(empCountPerSexOfMktg.get("MALE_COUNT").get)
        assertResult(1)(empCountPerSexOfFin.get("FEMALE_COUNT").get)
        assertResult(1)(empCountPerSexOfOffMgmt.get("FEMALE_COUNT").get)
        assertResult(2)(empCountPerSexOfIT.get("MALE_COUNT").get)
    }

    test("average salary per department") {
        val employees = loadEmployees()
        val avgSalaryPerDept = PersonOperations.averageSalaryPerDepartment(employees)
        assertResult(70000)(avgSalaryPerDept.get("HR").get.intValue)
        assertResult(52750)(avgSalaryPerDept.get("Development").get.intValue)
        assertResult(54000)(avgSalaryPerDept.get("Operations").get.intValue)
        assertResult(57000)(avgSalaryPerDept.get("Marketing").get.intValue)
        assertResult(65000)(avgSalaryPerDept.get("Finance").get.intValue)
        assertResult(52000)(avgSalaryPerDept.get("Office Management").get.intValue)
        assertResult(45500)(avgSalaryPerDept.get("IT").get.intValue)
    }

    private def loadEmployees(): IndexedSeq[Employee] = {
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
            sex = Sex.FEMALE, joiningDate = convertToDate("18/03/2013"), jobTitle = "Team Lead", department = "Marketing",
            yearlyGrossSalary = new BigDecimal(65000.00))
        val emp15 = new Employee(firstName = "Megha", lastName = "Shah", motherTongue = "Gujrati",
            sex = Sex.FEMALE, joiningDate = convertToDate("11/05/2015"), jobTitle = "Manager", department = "Finance",
            yearlyGrossSalary = new BigDecimal(65000.00))
        val emp16 = new Employee(firstName = "Joanna", lastName = "Ashton", motherTongue = "English",
            sex = Sex.FEMALE, joiningDate = convertToDate("20/01/2014"), jobTitle = "Team Lead", department = "Marketing",
            yearlyGrossSalary = new BigDecimal(48000.00))
        val emp17 = new Employee(firstName = "Kaori", lastName = "Reynolds", motherTongue = "Japanese",
            sex = Sex.FEMALE, joiningDate = convertToDate("10/02/2012"), jobTitle = "Administration Assistant", department = "Office Management",
            yearlyGrossSalary = new BigDecimal(52000.00))
        val emp18 = new Employee(firstName = "Bonnie", lastName = "MacArthur", motherTongue = "English",
            sex = Sex.FEMALE, joiningDate = convertToDate("08/10/2014"), jobTitle = "Mgr, Customer Service", department = "Operations",
            yearlyGrossSalary = new BigDecimal(70000.00))
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

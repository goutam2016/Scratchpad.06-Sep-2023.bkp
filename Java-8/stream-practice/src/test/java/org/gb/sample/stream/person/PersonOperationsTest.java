package org.gb.sample.stream.person;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class PersonOperationsTest {

	private static final Logger LOGGER = Logger.getLogger(PersonOperationsTest.class);

	private static final DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

	@Test
	public void groupByMotherTongue() {
		List<Person> people = loadPeople();
		Map<String, List<Person>> peopleByMotherTongue = PersonOperations.groupByMotherTongue(people);
		List<Person> englishPeople = peopleByMotherTongue.get("English");
		List<Person> punjabiPeople = peopleByMotherTongue.get("Punjabi");
		List<Person> russianPeople = peopleByMotherTongue.get("Russian");
		List<Person> konkaniPeople = peopleByMotherTongue.get("Konkani");
		Assert.assertEquals(5, englishPeople.size());
		Assert.assertEquals(2, punjabiPeople.size());
		Assert.assertEquals(1, russianPeople.size());
		Assert.assertEquals(1, konkaniPeople.size());
	}

	@Test
	public void groupByMotherTongue2() {
		List<Person> people = loadPeople();
		Map<String, List<Person>> peopleByMotherTongue = PersonOperations.groupByMotherTongue2(people);
		List<Person> englishPeople = peopleByMotherTongue.get("English");
		List<Person> punjabiPeople = peopleByMotherTongue.get("Punjabi");
		List<Person> russianPeople = peopleByMotherTongue.get("Russian");
		List<Person> konkaniPeople = peopleByMotherTongue.get("Konkani");
		Assert.assertEquals(5, englishPeople.size());
		Assert.assertEquals(2, punjabiPeople.size());
		Assert.assertEquals(1, russianPeople.size());
		Assert.assertEquals(1, konkaniPeople.size());
	}
	
	@Test
	public void groupByMotherTongue3() {
		List<Person> people = loadPeople();
		Map<String, List<Person>> peopleByMotherTongue = PersonOperations.groupByMotherTongue3(people);
		List<Person> englishPeople = peopleByMotherTongue.get("English");
		List<Person> punjabiPeople = peopleByMotherTongue.get("Punjabi");
		List<Person> russianPeople = peopleByMotherTongue.get("Russian");
		List<Person> konkaniPeople = peopleByMotherTongue.get("Konkani");
		Assert.assertEquals(5, englishPeople.size());
		Assert.assertEquals(2, punjabiPeople.size());
		Assert.assertEquals(1, russianPeople.size());
		Assert.assertEquals(1, konkaniPeople.size());
	}

	@Test
	public void getEmployeesByJobTitle() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		List<Employee> businessAnalysts = PersonOperations.getEmployeesByJobTitle(employees, "Business Analyst");
		List<Employee> developers = PersonOperations.getEmployeesByJobTitle(employees, "Developer");
		List<Employee> hrManagers = PersonOperations.getEmployeesByJobTitle(employees, "HR Manager");
		Assert.assertEquals(2, businessAnalysts.size());
		Assert.assertEquals(3, developers.size());
		Assert.assertEquals(1, hrManagers.size());
	}

	@Test
	public void filterEmployeesJoinedAfter() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		List<Employee> filteredEmployees = PersonOperations.filterEmployeesJoinedAfter(employees,
				convertToDate("01/01/2012"));
		Assert.assertEquals(11, filteredEmployees.size());
	}

	@Test
	public void gatherEmployeesJoinedAfter() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		List<Employee> filteredEmployees = PersonOperations.gatherEmployeesJoinedAfter(employees,
				convertToDate("01/01/2012"));
		Assert.assertEquals(11, filteredEmployees.size());
	}

	@Test
	public void getTotalExperienceOfAllEmployees() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		Long totalExpInDays = PersonOperations.getTotalExperienceOfAllEmployees(employees);
		Assert.assertEquals(11841, totalExpInDays.longValue());
	}

	@Test
	public void getTotalExperienceOfFemaleEmployees() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		Long totalExpInDays = PersonOperations.getTotalExperienceOfFemaleEmployees(employees);
		Assert.assertEquals(2070, totalExpInDays.longValue());
	}

	@Test
	public void sumYearlySalariesByJobTitle() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		Map<String, BigDecimal> totalSalaryByJobTitle = PersonOperations.sumYearlySalariesByJobTitle(employees);
		Assert.assertEquals(new BigDecimal(46000.00), totalSalaryByJobTitle.get("Senior Support Engineer"));
		Assert.assertEquals(new BigDecimal(80000.00), totalSalaryByJobTitle.get("IT Director"));
		Assert.assertEquals(new BigDecimal(95000.00), totalSalaryByJobTitle.get("Business Analyst"));
		Assert.assertEquals(new BigDecimal(58000.00), totalSalaryByJobTitle.get("Development Manager"));
		Assert.assertEquals(new BigDecimal(58000.00), totalSalaryByJobTitle.get("Sales Account Manager"));
		Assert.assertEquals(new BigDecimal(127000.00), totalSalaryByJobTitle.get("Developer"));
		Assert.assertEquals(new BigDecimal(70000.00), totalSalaryByJobTitle.get("HR Manager"));
		Assert.assertEquals(new BigDecimal(157000.00), totalSalaryByJobTitle.get("Senior Developer"));
	}

	@Test
	public void maxSalaryPerDept() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		Map<String, BigDecimal> maxSalaryPerDept = PersonOperations.maxSalaryPerDept(employees);
		Assert.assertEquals(new BigDecimal(70000.00), maxSalaryPerDept.get("HR"));
		Assert.assertEquals(new BigDecimal(80000.00), maxSalaryPerDept.get("Development"));
		Assert.assertEquals(new BigDecimal(70000.00), maxSalaryPerDept.get("Operations"));
		Assert.assertEquals(new BigDecimal(65000.00), maxSalaryPerDept.get("Marketing"));
		Assert.assertEquals(new BigDecimal(65000.00), maxSalaryPerDept.get("Finance"));
		Assert.assertEquals(new BigDecimal(52000.00), maxSalaryPerDept.get("Office Management"));
		Assert.assertEquals(new BigDecimal(46000.00), maxSalaryPerDept.get("IT"));
	}

	@Test
	public void empNameWithMaxSalaryPerDept() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		Map<String, String> empNameVsDept = PersonOperations.empNameWithMaxSalaryPerDept(employees);
		Assert.assertEquals("Kate Avery", empNameVsDept.get("HR"));
		Assert.assertEquals("Arun Ranganathan", empNameVsDept.get("Development"));
		Assert.assertEquals("Bonnie MacArthur", empNameVsDept.get("Operations"));
		Assert.assertEquals("Riina Sulonen", empNameVsDept.get("Marketing"));
		Assert.assertEquals("Megha Shah", empNameVsDept.get("Finance"));
		Assert.assertEquals("Kaori Reynolds", empNameVsDept.get("Office Management"));
		Assert.assertEquals("Faisal Kamal", empNameVsDept.get("IT"));
	}

	@Test
	public void highestPaidFemaleEmployeePerDept() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		Map<String, String> empNameVsDept = PersonOperations.highestPaidFemaleEmployeePerDept(employees);
		printMap(empNameVsDept);
		Assert.assertEquals("Kate Avery", empNameVsDept.get("HR"));
		Assert.assertEquals("Nisha Vaswani", empNameVsDept.get("Development"));
		Assert.assertEquals("Bonnie MacArthur", empNameVsDept.get("Operations"));
		Assert.assertEquals("Riina Sulonen", empNameVsDept.get("Marketing"));
		Assert.assertEquals("Megha Shah", empNameVsDept.get("Finance"));
		Assert.assertEquals("Kaori Reynolds", empNameVsDept.get("Office Management"));
	}

	@Test
	public void noOfFemaleEmployeesOverBaseSalaryPerDept() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		Map<String, Integer> empCountPerDept = PersonOperations.noOfFemaleEmployeesOverBaseSalaryPerDept(employees,
				new BigDecimal(50000.00));
		printMap(empCountPerDept);
		Assert.assertEquals(new Integer(1), empCountPerDept.get("HR"));
		Assert.assertEquals(new Integer(0), empCountPerDept.get("Development"));
		Assert.assertEquals(new Integer(1), empCountPerDept.get("Operations"));
		Assert.assertEquals(new Integer(1), empCountPerDept.get("Marketing"));
		Assert.assertEquals(new Integer(1), empCountPerDept.get("Finance"));
		Assert.assertEquals(new Integer(1), empCountPerDept.get("Office Management"));
		Assert.assertEquals(new Integer(0), empCountPerDept.get("IT"));
	}

	@Test
	public void employeeCountPerSexPerDept() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		Map<String, Map<String, Integer>> employeeCountPerSexPerDept = PersonOperations.employeeCountPerSexPerDept(
				employees, new BigDecimal(30000.00));
		printMap(employeeCountPerSexPerDept);
	}
	
	@Test
	public void averageSalaryPerDepartment() {
		List<Employee> employees = new ArrayList<>();
		addEmployees(employees);
		Map<String, BigDecimal> avgSalaryPerDept = PersonOperations.averageSalaryPerDepartment(employees);
		printMap(avgSalaryPerDept);
	}

	private <K, V> void printMap(Map<K, V> map) {
		BiConsumer<K, V> printer = (K firstArg, V secondArg) -> LOGGER.info(firstArg + " <--> " + secondArg);
		map.forEach(printer);
	}

	private List<Person> loadPeople() {
		List<Person> people = new ArrayList<>();
		addEmployees(people);
		return people;
	}

	private void addEmployees(List<? super Employee> people) {
		people.add(new Employee("Laura", "Edwards", "English", Employee.Sex.FEMALE, convertToDate("12/05/2013"),
				"Business Analyst", "Operations", new BigDecimal(45000.00)));
		people.add(new Employee("Arun", "Ranganathan", "Tamil", Employee.Sex.MALE, convertToDate("15/12/2014"),
				"Director, Software Engineering", "Development", new BigDecimal(80000.00)));
		people.add(new Employee("Chandra", "Bhadraraju", "Telegu", Employee.Sex.MALE, convertToDate("06/07/2012"),
				"Developer", "Development", new BigDecimal(45000.00)));
		people.add(new Employee("Dareem", "Parker", "Konkani", Employee.Sex.MALE, convertToDate("03/07/2013"),
				"Senior Developer", "Development", new BigDecimal(52000.00)));
		people.add(new Employee("Mehul", "Khandarkar", "Gujrati", Employee.Sex.MALE, convertToDate("21/03/2013"),
				"Business Analyst", "Operations", new BigDecimal(50000.00)));
		people.add(new Employee("Mehmet", "Divilioglu", "Turkish", Employee.Sex.MALE, convertToDate("10/05/2015"),
				"Senior Developer", "Development", new BigDecimal(50000.00)));
		people.add(new Employee("Faisal", "Kamal", "Bengali", Employee.Sex.MALE, convertToDate("12/08/2009"),
				"Senior Support Engineer", "IT", new BigDecimal(46000.00)));
		people.add(new Employee("Kate", "Avery", "English", Employee.Sex.FEMALE, convertToDate("07/10/2013"),
				"HR Manager", "HR", new BigDecimal(70000.00)));
		people.add(new Employee("Robdeep", "Sangha", "Punjabi", Employee.Sex.MALE, convertToDate("16/07/2012"),
				"Sales Account Manager", "Marketing", new BigDecimal(58000.00)));
		people.add(new Employee("Andrey", "Perverzin", "Russian", Employee.Sex.MALE, convertToDate("22/01/2015"),
				"Senior Developer", "Development", new BigDecimal(55000.00)));
		people.add(new Employee("Velmurugan", "Muthu", "Tamil", Employee.Sex.MALE, convertToDate("04/01/2011"),
				"Development Manager", "Development", new BigDecimal(58000.00)));
		people.add(new Employee("Nisha", "Vaswani", "Hindi", Employee.Sex.FEMALE, convertToDate("11/03/2015"),
				"Developer", "Development", new BigDecimal(40000.00)));
		people.add(new Employee("Adarshpal", "Brar", "Punjabi", Employee.Sex.MALE, convertToDate("08/06/2015"),
				"Developer", "Development", new BigDecimal(42000.00)));
		people.add(new Employee("Riina", "Sulonen", "English", Employee.Sex.FEMALE, convertToDate("18/03/2013"),
				"Team Lead", "Marketing", new BigDecimal(65000.00)));
		people.add(new Employee("Megha", "Shah", "Gujrati", Employee.Sex.FEMALE, convertToDate("11/05/2015"),
				"Manager", "Finance", new BigDecimal(65000.00)));
		people.add(new Employee("Joanna", "Ashton", "English", Employee.Sex.FEMALE, convertToDate("20/01/2014"),
				"Marketing Specialist", "Marketing", new BigDecimal(48000.00)));
		people.add(new Employee("Kaori", "Reynolds", "Japanese", Employee.Sex.FEMALE, convertToDate("10/02/2012"),
				"Administration Assistant", "Office Management", new BigDecimal(52000.00)));
		people.add(new Employee("Bonnie", "MacArthur", "English", Employee.Sex.FEMALE, convertToDate("08/10/2014"),
				"Mgr, Customer Service", "Operations", new BigDecimal(70000.00)));
		people.add(new Employee("Chaminda", "Gunarathne", "Sinhalese", Employee.Sex.MALE, convertToDate("23/06/2014"),
				"Information Systems Engineer", "IT", new BigDecimal(45000.00)));
		people.add(new Employee("Manuel", "Moreno", "Spanish", Employee.Sex.MALE, convertToDate("05/02/2014"),
				"Fraud Specialist", "Operations", new BigDecimal(51000.00)));
	}

	private Date convertToDate(String dateStr) {
		try {
			return dateFormatter.parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error("Exception during date conversion.", e);
			return null;
		}
	}
}

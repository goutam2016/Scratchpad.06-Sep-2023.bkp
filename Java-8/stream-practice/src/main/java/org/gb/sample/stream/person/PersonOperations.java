package org.gb.sample.stream.person;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PersonOperations {

	public static Map<String, List<Person>> groupByMotherTongue(List<Person> people) {
		Function<Person, String> classifier = Person::getMotherTongue;
		Map<String, List<Person>> peopleByMotherTongue = people.stream().collect(Collectors.groupingBy(classifier));
		return peopleByMotherTongue;
	}
	
	public static Map<String, List<Person>> groupByMotherTongue2(List<Person> people) {
		Map<String, List<Person>> peopleByMotherTongue = people.stream().collect(new PersonCollector());
		return peopleByMotherTongue;
	}	

	public static Map<String, List<Person>> groupByMotherTongue3(List<Person> people) {
		Map<String, List<Person>> peopleByMotherTongue = people.stream().collect(new PersonCollector2(Person::getMotherTongue));
		return peopleByMotherTongue;
	}	

	public static List<Employee> getEmployeesByJobTitle(List<Employee> employees, String jobTitle) {
		Predicate<Employee> predicate = (Employee employee) -> jobTitle.equalsIgnoreCase(employee.getJobTitle());
		List<Employee> employeesByJobTitle = employees.stream().filter(predicate).collect(Collectors.toList());
		return employeesByJobTitle;
	}

	public static List<Employee> filterEmployeesJoinedAfter(List<Employee> employees, Date joiningDate) {
		Predicate<Employee> predicate = (Employee employee) -> (employee.getJoiningDate() == null) ? false : employee
				.getJoiningDate().after(joiningDate);
		List<Employee> employeesJoinedAfter = employees.stream().filter(predicate).collect(Collectors.toList());
		return employeesJoinedAfter;
	}

	public static List<Employee> gatherEmployeesJoinedAfter(List<Employee> employees, Date joiningDate) {
		List<Employee> selectedEmployees = new ArrayList<Employee>();
		Consumer<Employee> consumer = (Employee employee) -> {
			if (employee.getJoiningDate() != null && employee.getJoiningDate().after(joiningDate)) {
				selectedEmployees.add(employee);
			}
		};
		employees.stream().forEach(consumer);
		return selectedEmployees;
	}

	public static Long getTotalExperienceOfAllEmployees(List<Employee> employees) {
		Integer millisPerDay = 24 * 60 * 60 * 1000;
		ToLongFunction<Employee> experienceMapper = (Employee employee) -> (employee.getJoiningDate() == null) ? 0
				: (System.currentTimeMillis() - employee.getJoiningDate().getTime()) / millisPerDay;
		return employees.stream().mapToLong(experienceMapper).sum();
	}

	public static Long getTotalExperienceOfFemaleEmployees(List<Employee> employees) {
		Predicate<Employee> predicate = (Employee employee) -> (employee.getSex() == Employee.Sex.FEMALE);
		Integer millisPerDay = 24 * 60 * 60 * 1000;
		ToLongFunction<Employee> experienceMapper = (Employee employee) -> (employee.getJoiningDate() == null) ? 0
				: (System.currentTimeMillis() - employee.getJoiningDate().getTime()) / millisPerDay;
		return employees.stream().filter(predicate).mapToLong(experienceMapper).sum();
	}

	public static Map<String, BigDecimal> sumYearlySalariesByJobTitle(List<Employee> employees) {
		Function<Employee, String> jobTitleClassifier = Employee::getJobTitle;
		Function<Employee, BigDecimal> salaryExtractor = Employee::getYearlyGrossSalary;
		BinaryOperator<BigDecimal> salaryAdder = (BigDecimal firstSalary, BigDecimal secondSalary) -> (firstSalary == null) ? secondSalary
				: firstSalary.add(secondSalary);
		Collector<Employee, ?, BigDecimal> summingSalaries = Collectors.reducing(BigDecimal.ZERO, salaryExtractor,
				salaryAdder);
		Collector<Employee, ?, Map<String, BigDecimal>> summingSalariesByJobTitle = Collectors.groupingBy(
				jobTitleClassifier, summingSalaries);
		Map<String, BigDecimal> totalSalaryByJobTitle = employees.stream().collect(summingSalariesByJobTitle);
		return totalSalaryByJobTitle;
	}

	public static Map<String, BigDecimal> maxSalaryPerDept(List<Employee> employees) {
		BinaryOperator<BigDecimal> maxSalary = (BigDecimal firstSalary, BigDecimal secondSalary) -> {
			if (firstSalary == null) {
				return secondSalary;
			} else if (firstSalary.doubleValue() >= secondSalary.doubleValue()) {
				return firstSalary;
			} else {
				return secondSalary;
			}
		};
		Collector<Employee, ?, BigDecimal> empWithMaxSalary = Collectors.reducing(BigDecimal.ZERO,
				Employee::getYearlyGrossSalary, maxSalary);
		Map<String, BigDecimal> maxSalaryPerDept = employees.stream().collect(
				Collectors.groupingBy(Employee::getDepartment, empWithMaxSalary));
		return maxSalaryPerDept;
	}

	public static Map<String, String> empNameWithMaxSalaryPerDept(List<Employee> employees) {
		Comparator<Employee> comparatorBySalary = Comparator.comparing(Employee::getYearlyGrossSalary);
		BinaryOperator<Employee> maxSalaryFinder = BinaryOperator.maxBy(Comparator.nullsFirst(comparatorBySalary));
		Collector<Employee, ?, Employee> empWithMaxSalary = Collectors.reducing(null, maxSalaryFinder);
		Function<Employee, String> empNameExtractor = (Employee employee) -> employee.getFirstName() + " "
				+ employee.getLastName();
		Collector<Employee, ?, String> empNameWithMaxSalary = Collectors.collectingAndThen(empWithMaxSalary,
				empNameExtractor);
		Map<String, String> empNameWithMaxSalaryPerDept = employees.stream().collect(
				Collectors.groupingBy(Employee::getDepartment, empNameWithMaxSalary));
		return empNameWithMaxSalaryPerDept;
	}

	public static Map<String, String> highestPaidFemaleEmployeePerDept(List<Employee> employees) {
		Comparator<Employee> salaryComparator = Comparator.comparing(Employee::getYearlyGrossSalary);
		BinaryOperator<Employee> maxSalaryFinder = BinaryOperator.maxBy(Comparator.nullsFirst(salaryComparator));
		Collector<Employee, ?, Employee> highestPaidEmpCollector = Collectors.reducing(null, maxSalaryFinder);
		Function<Employee, String> empNameExtractor = (Employee employee) -> employee.getFirstName() + " "
				+ employee.getLastName();
		Collector<Employee, ?, String> highestPaidEmpNameCollector = Collectors.collectingAndThen(
				highestPaidEmpCollector, empNameExtractor);
		Predicate<Employee> isFemale = (Employee employee) -> (employee.getSex() == Employee.Sex.FEMALE);
		Map<String, String> highestPaidFemaleEmployeesPerDept = employees.stream().filter(isFemale)
				.collect(Collectors.groupingBy(Employee::getDepartment, highestPaidEmpNameCollector));
		return highestPaidFemaleEmployeesPerDept;
	}

	public static Map<String, Integer> noOfFemaleEmployeesOverBaseSalaryPerDept(List<Employee> employees,
			BigDecimal baseSalary) {
		Function<Employee, Integer> empSelector = (Employee employee) -> {
			if(employee.getSex() == null || employee.getSex() == Employee.Sex.MALE) {
				return 0;
			} else if (employee.getYearlyGrossSalary() == null) {
				return 0;
			} else {
				return (employee.getYearlyGrossSalary().doubleValue() >= baseSalary.doubleValue()) ? 1 : 0;
			}
		};
		BinaryOperator<Integer> empCounter = Integer::sum;
		Collector<Employee, ?, Integer> employeeCountOverBaseSalaryCollector = Collectors.reducing(0, empSelector,
				empCounter);
		Map<String, Integer> noOfFemaleEmployeesOverBaseSalaryPerDept = employees.stream().collect(
				Collectors.groupingBy(Employee::getDepartment, employeeCountOverBaseSalaryCollector));
		return noOfFemaleEmployeesOverBaseSalaryPerDept;
	}
	
	/**
	 * This method will return a map that contains number of male and female employees over a base salary per department.
	 * The return type looks like: Map<String, Map<String, Integer>>.
	 * The key to the outer map is department name. The key to the inner map will be either MALE_COUNT or FEMALE_COUNT. The value of the
	 * inner map is the actual count of male or female employees for a given department.
	 */
	public static Map<String, Map<String, Integer>> employeeCountPerSexPerDept(List<Employee> employees, BigDecimal baseSalary) {
		Function<Employee, Integer> empSelector = (Employee employee) -> {
			if (employee.getYearlyGrossSalary() == null) {
				return 0;
			} else {
				return (employee.getYearlyGrossSalary().doubleValue() >= baseSalary.doubleValue()) ? 1 : 0;
			}
		};
		BinaryOperator<Integer> empCounter = Integer::sum;
		Collector<Employee, ?, Integer> employeeCountCollector = Collectors.reducing(0, empSelector, empCounter);
		Function<Employee, String> sexClassifier = (Employee employee) -> (employee.getSex() == Employee.Sex.MALE) ? "MALE_COUNT"
				: "FEMALE_COUNT";
		Collector<Employee, ?, Map<String, Integer>> employeeCountPerSexCollector = Collectors.groupingBy(
				sexClassifier, employeeCountCollector);
		Map<String, Map<String, Integer>> employeeCountPerSexPerDept = employees.stream().collect(
				Collectors.groupingBy(Employee::getDepartment, employeeCountPerSexCollector));
		return employeeCountPerSexPerDept;
	}
	
	public static Map<String, BigDecimal> averageSalaryPerDepartment(List<Employee> employees) {
		ToDoubleFunction<Employee> salaryExtractor = (Employee employee) -> (employee.getYearlyGrossSalary() == null) ? 0.0
				: employee.getYearlyGrossSalary().doubleValue();
		Collector<Employee, ?, BigDecimal> avgSalaryCollector = Collectors.collectingAndThen(
				Collectors.averagingDouble(salaryExtractor),
				(Double avgSalary) -> new BigDecimal(avgSalary.doubleValue()));
		Map<String, BigDecimal> averageSalaryPerDepartment = employees.stream().collect(
				Collectors.groupingBy(Employee::getDepartment, avgSalaryCollector));
		return averageSalaryPerDepartment;
	}
}

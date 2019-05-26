package com.example.orgchart.repository;

import com.example.orgchart.domain.Employee;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class EmployeeRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	protected EmployeeRepository employeeRepository;

	private Long savedId;

	@Override
	@Before
	public void setUp() {
		super.setUp();
		session.query("CREATE (n:Employee {label: 'Tom Sawyer', canSign: true, level: 2, x: -180}) RETURN ID(n) as ID", Collections.EMPTY_MAP)
				.queryResults().forEach(resultMap -> savedId = (Long) resultMap.get("ID"));
	}

	@Test
	public void save_returnsCreatedEmployee_ifNotExist() {
		final Employee employee = Employee.builder().label("Allan Cooper").canSign(false).level(2L).x(200L).build();
		final Employee savedEmployee = employeeRepository.save(employee);
		assertNotNull(savedEmployee);
		assertEquals("Allan Cooper", savedEmployee.getLabel());
		assertEquals(false, savedEmployee.getCanSign());
		assertEquals(2, savedEmployee.getLevel().longValue());
		assertEquals(200, savedEmployee.getX().longValue());
	}

	@Test
	public void save_returnsUpdatedEmployee_ifExist() {
		final Employee employee = Employee.builder().id(0L).label("Tom Sawyer").canSign(false).level(2L).x(-200L).build();
		final Employee savedEmployee = employeeRepository.save(employee);
		assertNotNull(savedEmployee);
		assertEquals("Tom Sawyer", savedEmployee.getLabel());
		assertEquals(false, savedEmployee.getCanSign());
		assertEquals(2, savedEmployee.getLevel().longValue());
		assertEquals(-200, savedEmployee.getX().longValue());
	}

	@Test
	public void getById_returnsEmployee_ifExist() {
		final Optional<Employee> employeeOptional = employeeRepository.findById(savedId);
		assertTrue(employeeOptional.isPresent());
		final Employee employee = employeeOptional.get();
		assertEquals("Tom Sawyer", employee.getLabel());
		assertEquals(true, employee.getCanSign());
		assertEquals(2, employee.getLevel().longValue());
		assertEquals(-180, employee.getX().longValue());
	}

	@Test
	public void getById_returnsNull_ifNotExist() {
		final Optional<Employee> employeeOptional = employeeRepository.findById(1L);
		assertFalse(employeeOptional.isPresent());
	}

	@Test
	public void searchByLabel_returnsEmployees_IfExist() {
		final List<Employee> employees = employeeRepository.search("Tom Sawyer", null);
		assertEquals(1, employees.size());
		final Employee employee = employees.get(0);
		assertEquals("Tom Sawyer", employee.getLabel());
		assertEquals(true, employee.getCanSign());
		assertEquals(2, employee.getLevel().longValue());
		assertEquals(-180, employee.getX().longValue());
	}

	@Test
	public void searchByLabelPart_returnsEmployees_IfExist() {
		final List<Employee> employees = employeeRepository.search("Saw", null);
		assertEquals(1, employees.size());
		final Employee employee = employees.get(0);
		assertEquals("Tom Sawyer", employee.getLabel());
		assertEquals(true, employee.getCanSign());
		assertEquals(2, employee.getLevel().longValue());
		assertEquals(-180, employee.getX().longValue());
	}

	@Test
	public void searchByLabelPartAndCanSign_returnsEmployees_IfExist() {
		final List<Employee> employees = employeeRepository.search("Saw", true);
		assertEquals(1, employees.size());
		final Employee employee = employees.get(0);
		assertEquals("Tom Sawyer", employee.getLabel());
		assertEquals(true, employee.getCanSign());
		assertEquals(2, employee.getLevel().longValue());
		assertEquals(-180, employee.getX().longValue());
	}

	@Test
	public void searchByLabel_returnsEmptyList_IfNotExist() {
		final List<Employee> employees = employeeRepository.search("Allan", null);
		assertTrue(employees.isEmpty());
	}

	@Test
	public void searchByCanSign_returnsEmployees_IfExist() {
		final List<Employee> employees = employeeRepository.search(null, true);
		assertFalse(employees.isEmpty());
	}

	@Test
	public void searchByCanSign_returnsEmptyList_IfNotExist() {
		final List<Employee> employees = employeeRepository.search(null, false);
		assertTrue(employees.isEmpty());
	}

	@Test
	public void searchAll_returnsEmployees_IfExist() {
		final List<Employee> employees = employeeRepository.search(null, null);
		assertFalse(employees.isEmpty());
	}

}

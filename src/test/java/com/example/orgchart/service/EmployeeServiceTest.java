package com.example.orgchart.service;

import com.example.orgchart.domain.Employee;
import com.example.orgchart.domain.Relationship;
import com.example.orgchart.dto.EmployeeSearchDto;
import com.example.orgchart.repository.EmployeeRepository;
import com.example.orgchart.repository.RelationshipRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private RelationshipRepository relationshipRepository;

	@InjectMocks
	private EmployeeService employeeService;

	@Test
	public void findAll_returnsEmployee_ifFound() {
		final Employee employee = Employee.builder().id(1L).label("Tom Sawyer").canSign(true).level(2L).x(-180L).build();
		when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee));

		final Iterable<Employee> all = employeeService.findAll();

		verify(employeeRepository).findAll();
		assertThat(all).containsExactly(employee);
	}

	@Test
	public void findAll_returnsEmptyList_ifNotFound() {
		when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

		final Iterable<Employee> all = employeeService.findAll();

		verify(employeeRepository).findAll();
		assertThat(all).isEmpty();
	}

	@Test
	public void save_returnsEmployee_ifSaved() {
		final Employee employee = Employee.builder().label("Tom Sawyer").canSign(true).level(2L).x(-180L).build();
		final Employee employeeSaved = Employee.builder().id(1L).label("Tom Sawyer").canSign(true).level(2L).x(-180L).build();
		when(employeeRepository.save(employee)).thenReturn(employeeSaved);

		final Employee saved = employeeService.save(employee);

		verify(employeeRepository).save(employee);
		assertThat(saved).isEqualTo(employeeSaved);
	}

	@Test
	public void delete_doNothing_ifDeleted() {
		Relationship parents = Relationship.builder().id(1L).build();
		Relationship children = Relationship.builder().id(2L).to(Employee.builder().id(3L).build()).build();
		when(relationshipRepository.getParents(2L)).thenReturn(Collections.singletonList(parents));
		when(relationshipRepository.getChildren(2L)).thenReturn(Collections.singletonList(children));

		employeeService.delete(2L, 1L);

		verify(relationshipRepository).getParents(2L);
		verify(relationshipRepository).deleteAll(Collections.singletonList(parents));
		verify(relationshipRepository).getChildren(2L);
		verify(relationshipRepository).create(1L, 3L);
		verify(employeeRepository).deleteById(2L);
	}

	@Test
	public void search_returnsEmployee_ifFound() {
		final Employee employee = Employee.builder().label("Tom Sawyer").canSign(true).level(2L).x(-180L).build();
		when(employeeRepository.search("Tom Sawyer", true)).thenReturn(Collections.singletonList(employee));

		final Iterable<Employee> employees = employeeService.search(EmployeeSearchDto.builder().label("Tom Sawyer").canSign(true).build());

		verify(employeeRepository).search("Tom Sawyer", true);
		assertThat(employees).isEqualTo(Collections.singletonList(employee));
	}

	@Test
	public void search_returnsEmptyList_ifNotFound() {
		when(employeeRepository.search("Tom Sawyer", true)).thenReturn(Collections.emptyList());

		final Iterable<Employee> employees = employeeService.search(EmployeeSearchDto.builder().label("Tom Sawyer").canSign(true).build());

		verify(employeeRepository).search("Tom Sawyer", true);
		assertThat(employees).isEqualTo(Collections.emptyList());
	}
}

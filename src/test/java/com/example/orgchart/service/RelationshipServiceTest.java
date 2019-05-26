package com.example.orgchart.service;

import com.example.orgchart.domain.Employee;
import com.example.orgchart.domain.Relationship;
import com.example.orgchart.repository.EmployeeRepository;
import com.example.orgchart.repository.RelationshipRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private RelationshipRepository relationshipRepository;

	@InjectMocks
	RelationshipService relationshipService;

	@Test
	public void findAll_returnsAllRelationships_ifFound() {
		final Relationship relationship = Relationship.builder().build();
		when(relationshipRepository.findAll()).thenReturn(Collections.singletonList(relationship));

		final Iterable<Relationship> all = relationshipService.findAll();

		verify(relationshipRepository).findAll();
		assertThat(all).containsExactly(relationship);
	}

	@Test
	public void findAll_returnsEmptyList_ifNotFound() {
		when(relationshipRepository.findAll()).thenReturn(Collections.emptyList());

		final Iterable<Relationship> all = relationshipService.findAll();

		verify(relationshipRepository).findAll();
		assertThat(all).isEmpty();
	}

	@Test
	public void create_returnRelationship_ifEmployeesExist() {
		final Relationship relationshipExpected = Relationship.builder()
				.id(1L)
				.from(Employee.builder().id(1L).build())
				.to(Employee.builder().id(2L).build())
				.build();
		when(relationshipRepository.create(1L, 2L)).thenReturn(relationshipExpected);

		final Relationship relationshipSaved = relationshipService.create(1L, 2L);


		verify(relationshipRepository).create(1L, 2L);
		assertThat(relationshipSaved).isEqualTo(relationshipExpected);
	}

	@Test
	public void create_returnRelationship_ifEmployeesNotExist() {
		final Relationship relationshipExpected = Relationship.builder().id(1L).from(null).to(null).build();

		when(relationshipRepository.create(1L, 2L)).thenReturn(relationshipExpected);

		final Relationship relationshipSaved = relationshipService.create(1L, 2L);


		verify(relationshipRepository).create(1L, 2L);
		assertThat(relationshipSaved).isEqualTo(relationshipExpected);
	}
	@Test
	public void delete_doNothing_ifDeleted() {
		relationshipService.delete(1L);
		verify(relationshipRepository).deleteById(1L);
	}

	@Test
	public void create_returnsRelationship_ifCreated() {
		final Relationship relationship = Relationship.builder()
				.from(Employee.builder().id(1L).build())
				.to(Employee.builder().id(2L).build())
				.build();
		when(relationshipRepository.create(1L, 2L)).thenReturn(relationship);

		final Relationship relationshipCreated = relationshipService.create(1L, 2L);

		verify(relationshipRepository).create(1L, 2L);
		assertThat(relationshipCreated).isEqualTo(relationship);
	}
}

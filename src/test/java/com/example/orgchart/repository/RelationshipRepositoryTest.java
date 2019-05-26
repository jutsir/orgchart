package com.example.orgchart.repository;

import com.example.orgchart.domain.Relationship;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class RelationshipRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	private RelationshipRepository relationshipRepository;

	private Long relationId;
	private Long parentNodeId;
	private Long middleNodeId;
	private Long childNodeId;

	@Before
	public void setUp() {
		super.setUp();
		session.query("CREATE (parent:Employee {label: 'Tom Sawyer', canSign: true, level: 1, x: -180})-[:MANAGES]->" +
				"(middle:Employee {label: 'Bill Gates', canSign: true, level: 2, x: -100})-[p:MANAGES]->" +
				"(child:Employee {label: 'Allan Cooper', canSign: false, level: 3, x: -200}) RETURN ID(middle) as middle, ID(p) as id, " +
				"ID(parent) as parent, ID(child) as child",
				Collections.EMPTY_MAP).queryResults().forEach(resultMap -> {
					relationId = (Long) resultMap.get("id");
					parentNodeId = (Long) resultMap.get("parent");
					middleNodeId = (Long) resultMap.get("middle");
					childNodeId = (Long) resultMap.get("child");
				});
	}

	@Test
	public void getParents_returnsParentRelationships_ifFound() {
		final List<Relationship> parents = relationshipRepository.getParents(middleNodeId);
		assertFalse(parents.isEmpty());
	}

	@Test
	public void getParents_returnsEmptyList_ifNotFound() {
		final List<Relationship> parents = relationshipRepository.getParents(-1L);
		assertTrue(parents.isEmpty());
	}

	@Test
	public void getChildren_returnsChildRelationship_ifFound() {
		final List<Relationship> children = relationshipRepository.getChildren(middleNodeId);
		assertFalse(children.isEmpty());
	}

	@Test
	public void getChildren_returnsChildRelationship_ifNotFound() {
		final List<Relationship> children = relationshipRepository.getChildren(-1L);
		assertTrue(children.isEmpty());
	}

	@Test
	public void create_returnsRelationship_IfEmployeesFound() {
		final Relationship relationship = relationshipRepository.create(parentNodeId, childNodeId);
		assertNotNull(relationship);
		assertNotNull(relationship.getFrom());
		assertNotNull(relationship.getTo());
		assertEquals(parentNodeId, relationship.getFrom().getId());
		assertEquals(childNodeId, relationship.getTo().getId());
	}

	@Test
	public void create_returnsNull_IfEmployeesNotFound() {
		final Relationship relationship = relationshipRepository.create(-1L, -2L);
		assertNull(relationship);
	}

	@Test
	public void create_returnsNull_IfEmployeeNotFound() {
		final Relationship relationship = relationshipRepository.create(parentNodeId, -1L);
		assertNull(relationship);
	}

	@Test
	public void create_returnsNull_IfNullsParams() {
		final Relationship relationship = relationshipRepository.create(null, null);
		assertNull(relationship);
	}

	@Test
	public void create_returnsNull_IfNullParams() {
		final Relationship relationship = relationshipRepository.create(parentNodeId, null);
		assertNull(relationship);
	}

	@Test
	public void delete_doNothing_ifSuccess() {
		relationshipRepository.deleteById(relationId);
		final Optional<Relationship> relationship = relationshipRepository.findById(relationId);
		assertFalse(relationship.isPresent());
	}
}

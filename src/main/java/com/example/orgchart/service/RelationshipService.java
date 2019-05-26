package com.example.orgchart.service;

import com.example.orgchart.domain.Relationship;
import com.example.orgchart.repository.RelationshipRepository;
import com.example.orgchart.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for relationships operation between employees
 */
@Slf4j
@Service
public class RelationshipService {

	private final RelationshipRepository relationshipRepository;

	@Autowired
	public RelationshipService(RelationshipRepository relationshipRepository) {
		this.relationshipRepository = relationshipRepository;
	}

	/**
	 * Find all relationships between employees.
	 * @return list of relationship data with own id and related employees.
	 */
	public Iterable<Relationship> findAll() {
		return relationshipRepository.findAll();
	}

	/**
	 * Delete relationship between employees.
	 * @param id of relationship to delete.
	 */
	public void delete(Long id) {
		relationshipRepository.deleteById(id);
		log.debug("Deleted relationship with id: " + id);
	}

	/**
	 * Create relationship between employees.
	 * @param fromId id of source employee.
	 * @param toId id of destination employee.
	 * @return relationship data with own id and related employees.
	 */
	public Relationship create(Long fromId, Long toId) {
		final Relationship created = relationshipRepository.create(fromId, toId);
		log.debug("Created relationship: " + JsonUtil.asJsonString(created));
		return created;
	}
}

package com.example.orgchart.controllers;

import com.example.orgchart.dto.RelationshipDto;
import com.example.orgchart.mapper.RelationshipMapper;
import com.example.orgchart.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for relationships operation between employees
 */
@RestController
@RequestMapping(value = RelationshipController.RELATIONSHIPS_API)
@RequiredArgsConstructor
public class RelationshipController {

	static final String RELATIONSHIPS_API = "/relationships";
	private final RelationshipMapper relationshipMapper;
	private final RelationshipService relationshipService;

	/**
	 * Find all relationships between employees.
	 * @return list of relationship data with own id and ids of related employees.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<RelationshipDto> findAll() {
		return relationshipMapper.toDtoList(relationshipService.findAll());
	}

	/**
	 * Create relationship between employees.
	 * @param relationshipDto with ids of related employees.
	 * @return relationship data with own id and ids of related employees.
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public RelationshipDto create(@RequestBody RelationshipDto relationshipDto) {
		return relationshipMapper.toDto(relationshipService.create(relationshipDto.getFrom(), relationshipDto.getTo()));
	}

	/**
	 * Delete relationship between employees.
	 * @param id of relationship to delete.
	 */
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Long id) {
		relationshipService.delete(id);
	}
}

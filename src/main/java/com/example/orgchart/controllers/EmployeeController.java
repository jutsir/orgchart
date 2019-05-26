package com.example.orgchart.controllers;

import com.example.orgchart.domain.Employee;
import com.example.orgchart.domain.Relationship;
import com.example.orgchart.dto.EmployeeDto;
import com.example.orgchart.dto.EmployeeResultDto;
import com.example.orgchart.dto.EmployeeSearchDto;
import com.example.orgchart.mapper.EmployeeMapper;
import com.example.orgchart.mapper.RelationshipMapper;
import com.example.orgchart.service.EmployeeService;
import com.example.orgchart.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for operation with employees
 */
@RestController
@RequestMapping(value = EmployeeController.EMPLOYEES_API)
@RequiredArgsConstructor
public class EmployeeController {

	static final String EMPLOYEES_API = "/employees";
	private final EmployeeService employeeService;
	private final RelationshipService relationshipService;
	private final EmployeeMapper employeeMapper;
	private final RelationshipMapper relationshipMapper;

	/**
	 * Find all employees data.
	 * @return list of employees data with id, canSign flag, label, level, x and y coordinate.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<EmployeeDto> findAll() {
		return employeeMapper.toDtoList(employeeService.findAll());
	}

	/**
	 * Search employees data by label and canSign flag.
	 * If label and canSign is absent, search will return all data.
	 * @param searchDto data with label and canSign flag.
	 * @return list of employee data with id, canSign flag, label, level, x and y coordinate.
	 */
	@RequestMapping(path = "/search", method = RequestMethod.GET)
	public List<EmployeeDto> search(EmployeeSearchDto searchDto) {
		return employeeMapper.toDtoList(employeeService.search(searchDto));
	}

	/**
	 * Save employee data.
	 * @param employeeDto data with id, canSign flag, label, level, x.
	 * @return employee data with id, canSign flag, label, level, x and y coordinate.
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public EmployeeDto save(@RequestBody EmployeeDto employeeDto) {
		return employeeMapper.toDto(employeeService.save(employeeMapper.fromDto(employeeDto)));
	}

	/**
	 * Create new employee data with relationship to parent employee.
	 * @param employeeDto data with canSign flag, label, level, x coordinate.
	 * @param parentId of parent employee.
	 * @return employee data with id, canSign flag, label, level, x and y coordinate.
	 */
	@RequestMapping(path = "/{parentId}", method = RequestMethod.POST)
	public EmployeeResultDto create(@RequestBody EmployeeDto employeeDto, @PathVariable Long parentId) {
		final Employee employee = employeeService.save(employeeMapper.fromDto(employeeDto));
		Relationship relationship = relationshipService.create(parentId, employee.getId());
		return EmployeeResultDto.builder()
				.employee(employeeMapper.toDto(employee))
				.relationship(relationship != null ? relationshipMapper.toDto(relationship) : null)
				.build();
	}

	/**
	 * Delete employee data and relink its relationships from children to the parent employee.
	 * @param id of employee.
	 * @param parentId of parent employee.
	 */
	@RequestMapping(path = "/{id}/{parentId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Long id, @PathVariable Long parentId) {
		employeeService.delete(id, parentId);
	}
}

package com.example.orgchart.service;

import com.example.orgchart.domain.Employee;
import com.example.orgchart.dto.EmployeeSearchDto;
import com.example.orgchart.repository.EmployeeRepository;
import com.example.orgchart.repository.RelationshipRepository;
import com.example.orgchart.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for employee operations.
 */
@Slf4j
@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final RelationshipRepository relationshipRepository;

	@Autowired
	public EmployeeService(EmployeeRepository employeeRepository,
			RelationshipRepository relationshipRepository) {
		this.employeeRepository = employeeRepository;
		this.relationshipRepository = relationshipRepository;
	}

	/**
	 * Find all employees data.
	 * @return list of employees data with id, canSign flag, label, level and x coordinate.
	 */
	public Iterable<Employee> findAll() {
		return employeeRepository.findAll();
	}

	/**
	 * Save employee data.
	 * @param employee data with or without id, canSign flag, label, level and x coordinate.
	 * @return employee data with id, canSign flag, label, level and x coordinate.
	 */
	public Employee save(Employee employee) {
		final Employee saved = employeeRepository.save(employee);
		log.debug("Saved employee: " + JsonUtil.asJsonString(saved));
		return saved;
	}

	/**
	 * Delete employee data and relink its relationships from children to the parent employee.
	 * @param id of employee.
	 * @param parentId of parent employee.
	 */
	public void delete(Long id, Long parentId) {
		relationshipRepository.deleteAll(relationshipRepository.getParents(id));
		relationshipRepository.getChildren(id).forEach(relationship -> relationshipRepository.create(parentId, relationship.getTo().getId()));
		employeeRepository.deleteById(id);
		log.debug("Deleted employee with id: " + id);
	}

	/**
	 * Search employees data by label and canSign flag.
	 * If label and canSign is absent, search will return all data.
	 * @param searchDto data with label and canSign flag.
	 * @return employee data with id, canSign flag, label, level and x coordinate.
	 */
	public Iterable<Employee> search(EmployeeSearchDto searchDto) {
		return employeeRepository.search(searchDto.getLabel(), searchDto.getCanSign());
	}
}

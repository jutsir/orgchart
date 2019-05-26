package com.example.orgchart.mapper;

import com.example.orgchart.domain.Employee;
import com.example.orgchart.dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeMapper {

	@Value("${LEVEL_SEPARATION}")
	private int LEVEL_SEPARATION;

	public EmployeeDto toDto(Employee employee) {
		return EmployeeDto.builder()
				.id(employee.getId())
				.canSign(employee.getCanSign())
				.label(employee.getLabel())
				.level(employee.getLevel())
				.x(employee.getX())
				.y((employee.getLevel() - 1) * LEVEL_SEPARATION)
				.build();
	}

	public Employee fromDto(EmployeeDto employeeDto) {
		Employee.EmployeeBuilder employeeBuilder = Employee.builder()
				.canSign(employeeDto.getCanSign())
				.label(employeeDto.getLabel())
				.level(employeeDto.getLevel())
				.x(employeeDto.getX());

		if (employeeDto.getId() != null) {
			employeeBuilder.id(employeeDto.getId());
		}

		return employeeBuilder.build();
	}

	public List<EmployeeDto> toDtoList(Iterable<Employee> employees) {
		List<EmployeeDto> dtoList = new ArrayList<>();
		employees.forEach(employee -> dtoList.add(toDto(employee)));
		return dtoList;
	}
}

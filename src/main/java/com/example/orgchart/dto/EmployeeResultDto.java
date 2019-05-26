package com.example.orgchart.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmployeeResultDto {
	private EmployeeDto employee;
	private RelationshipDto relationship;
}

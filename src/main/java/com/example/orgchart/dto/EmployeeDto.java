package com.example.orgchart.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmployeeDto {

	private Long id;
	private Boolean canSign;
	private String label;
	private Long level;
	private Long x;
	private Long y;
}

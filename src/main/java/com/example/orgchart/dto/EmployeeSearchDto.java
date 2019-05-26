package com.example.orgchart.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmployeeSearchDto {
	private String label;
	private Boolean canSign;
}

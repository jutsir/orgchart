package com.example.orgchart.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RelationshipDto {
	private Long id;
	private Long from;
	private Long to;
}

package com.example.orgchart.domain;

import lombok.*;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
@EqualsAndHashCode
public class Employee {

	@Id
	@GeneratedValue
	private Long id;
	private Boolean canSign;
	private String label;
	private Long level;
	private Long x;
}

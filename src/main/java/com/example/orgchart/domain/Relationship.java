package com.example.orgchart.domain;

import lombok.*;
import org.neo4j.ogm.annotation.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RelationshipEntity(type="MANAGES")
@EqualsAndHashCode
public class Relationship {

	@Id
	@GeneratedValue
	private Long id;
	@StartNode
	private Employee from;
	@EndNode
	private Employee to;
}

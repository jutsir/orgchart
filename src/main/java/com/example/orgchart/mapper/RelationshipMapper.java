package com.example.orgchart.mapper;

import com.example.orgchart.domain.Relationship;
import com.example.orgchart.dto.RelationshipDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RelationshipMapper {

	public RelationshipDto toDto(Relationship relationship) {
		return RelationshipDto.builder()
				.id(relationship.getId())
				.from(relationship.getFrom() != null ? relationship.getFrom().getId(): null)
				.to(relationship.getTo() != null ? relationship.getTo().getId(): null)
				.build();
	}

	public List<RelationshipDto> toDtoList(Iterable<Relationship> edges) {
		List<RelationshipDto> dtoList = new ArrayList<>();
		edges.forEach(relationship -> dtoList.add(toDto(relationship)));
		return dtoList;
	}
}

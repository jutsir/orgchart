package com.example.orgchart.controllers;

import com.example.orgchart.domain.Employee;
import com.example.orgchart.domain.Relationship;
import com.example.orgchart.dto.RelationshipDto;
import com.example.orgchart.mapper.RelationshipMapper;
import com.example.orgchart.service.RelationshipService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RelationshipControllerTest extends AbstractControllerTest {

	@Spy
	private RelationshipMapper relationshipMapper;

	@Mock
	private RelationshipService relationshipService;

	@InjectMocks
	private RelationshipController relationshipController;

	@Override
	Object getController() {
		return relationshipController;
	}

	@Test
	public void get_returnsAllRelationships_ifFound() throws Exception {
		final Relationship relationship = Relationship.builder()
				.id(5L)
				.from(Employee.builder().id(1L).build())
				.to(Employee.builder().id(2L).build())
				.build();

		when(relationshipService.findAll()).thenReturn(Collections.singletonList(relationship));

		mockMvc.perform(get(RelationshipController.RELATIONSHIPS_API))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(contentIsJsonUtf8)
				.andExpect(jsonPath("$[0].id", Matchers.equalTo(5)))
				.andExpect(jsonPath("$[0].from", Matchers.equalTo(1)))
				.andExpect(jsonPath("$[0].to", Matchers.equalTo(2)));
	}

	@Test
	public void create_returnsSavedRelationship_ifSaved() throws Exception {
		final Relationship relationship = Relationship.builder()
				.id(5L)
				.from(Employee.builder().id(1L).build())
				.to(Employee.builder().id(2L).build())
				.build();

		when(relationshipService.create(1L, 2L)).thenReturn(relationship);

		final RelationshipDto relationshipDto = RelationshipDto.builder().from(1L).to(2L).build();
		mockMvc.perform(put(RelationshipController.RELATIONSHIPS_API)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(asJsonString(relationshipDto)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(contentIsJsonUtf8)
				.andExpect(jsonPath("$.id", Matchers.equalTo(5)))
				.andExpect(jsonPath("$.from", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.to", Matchers.equalTo(2)));
	}

	@Test
	public void delete_returnsStatusOk_IfDeleted() throws Exception {
		doNothing().when(relationshipService).delete(5L);

		mockMvc.perform(delete(RelationshipController.RELATIONSHIPS_API + "/5"))
				.andExpect(status().isOk());
	}

}

package com.example.orgchart.controllers;

import com.example.orgchart.domain.Employee;
import com.example.orgchart.domain.Relationship;
import com.example.orgchart.dto.EmployeeDto;
import com.example.orgchart.dto.EmployeeSearchDto;
import com.example.orgchart.mapper.EmployeeMapper;
import com.example.orgchart.mapper.RelationshipMapper;
import com.example.orgchart.service.EmployeeService;
import com.example.orgchart.service.RelationshipService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest extends AbstractControllerTest {

	@Spy
	private EmployeeMapper employeeMapper = new EmployeeMapper();

	@Spy
	private RelationshipMapper relationshipMapper = new RelationshipMapper();


	@Mock
	private EmployeeService employeeService;

	@Mock
	private RelationshipService relationshipService;

	@InjectMocks
	private EmployeeController employeeController;

	@Override
	Object getController() {
		return employeeController;
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		ReflectionTestUtils.setField(employeeMapper, "LEVEL_SEPARATION", 200);
	}

	@Test
	public void get_returnsAllEmployees_ifFound() throws Exception {
		final Employee employee = Employee.builder().id(1L).label("Tom Sawyer").canSign(true).level(2L).x(-180L).build();

		when(employeeService.findAll()).thenReturn(Collections.singletonList(employee));

		mockMvc.perform(get(EmployeeController.EMPLOYEES_API))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(contentIsJsonUtf8)
				.andExpect(jsonPath("$[0].id", Matchers.equalTo(1)))
				.andExpect(jsonPath("$[0].canSign", Matchers.equalTo(true)))
				.andExpect(jsonPath("$[0].label", Matchers.equalTo("Tom Sawyer")))
				.andExpect(jsonPath("$[0].level", Matchers.equalTo(2)))
				.andExpect(jsonPath("$[0].x", Matchers.equalTo(-180)))
				.andExpect(jsonPath("$[0].y", Matchers.equalTo(200)));
	}

	@Test
	public void get_searchEmployees_ifFound() throws Exception {
		final Employee employee = Employee.builder().id(2L).label("Bill Gates").canSign(true).level(1L).x(-100L).build();
		final EmployeeSearchDto searchDto = EmployeeSearchDto.builder().label("Bill Gates").canSign(true).build();

		when(employeeService.search(searchDto)).thenReturn(Collections.singletonList(employee));

		mockMvc.perform(get(EmployeeController.EMPLOYEES_API + "/search")
				.param("label", "Bill Gates")
				.param("canSign", "true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(contentIsJsonUtf8)
				.andExpect(jsonPath("$[0].id", Matchers.equalTo(2)))
				.andExpect(jsonPath("$[0].canSign", Matchers.equalTo(true)))
				.andExpect(jsonPath("$[0].label", Matchers.equalTo("Bill Gates")))
				.andExpect(jsonPath("$[0].level", Matchers.equalTo(1)))
				.andExpect(jsonPath("$[0].x", Matchers.equalTo(-100)))
				.andExpect(jsonPath("$[0].y", Matchers.equalTo(0)));
	}

	@Test
	public void save_returnSavedEmployee_ifSaved() throws Exception {
		final Employee employee = Employee.builder().id(3L).label("Allan Cooper").canSign(false).level(2L).x(200L).build();

		when(employeeService.save(employee)).thenReturn(employee);

		final EmployeeDto employeeDto = EmployeeDto.builder().id(3L).label("Allan Cooper").canSign(false).level(2L).x(200L).y(0L).build();
		mockMvc.perform(put(EmployeeController.EMPLOYEES_API)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(asJsonString(employeeDto)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(contentIsJsonUtf8)
				.andExpect(jsonPath("$.id", Matchers.equalTo(3)))
				.andExpect(jsonPath("$.canSign", Matchers.equalTo(false)))
				.andExpect(jsonPath("$.label", Matchers.equalTo("Allan Cooper")))
				.andExpect(jsonPath("$.level", Matchers.equalTo(2)))
				.andExpect(jsonPath("$.x", Matchers.equalTo(200)))
				.andExpect(jsonPath("$.y", Matchers.equalTo(200)));
	}

	@Test
	public void create_returnsEmployeeAndRelationship_ifCreated() throws Exception {
		final Employee employeeParent = Employee.builder().id(1L).label("Tom Sawyer").canSign(true).level(2L).x(-180L).build();
		final Employee employee = Employee.builder().label("Allan Cooper").canSign(false).level(2L).x(200L).build();
		final Employee employeeCreated = Employee.builder().id(3L).label("Allan Cooper").canSign(false).level(2L).x(200L).build();

		final Relationship relationship = Relationship.builder().id(5L).from(employeeParent).to(employeeCreated).build();

		when(employeeService.save(employee)).thenReturn(employeeCreated);
		when(relationshipService.create(1L, 3L)).thenReturn(relationship);


		final EmployeeDto employeeDto = EmployeeDto.builder().label("Allan Cooper").canSign(false).level(2L).x(200L).y(0L).build();
		mockMvc.perform(post(EmployeeController.EMPLOYEES_API + "/1")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(asJsonString(employeeDto)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(contentIsJsonUtf8)
				.andExpect(jsonPath("$.employee.id", Matchers.equalTo(3)))
				.andExpect(jsonPath("$.employee.canSign", Matchers.equalTo(false)))
				.andExpect(jsonPath("$.employee.label", Matchers.equalTo("Allan Cooper")))
				.andExpect(jsonPath("$.employee.level", Matchers.equalTo(2)))
				.andExpect(jsonPath("$.employee.x", Matchers.equalTo(200)))
				.andExpect(jsonPath("$.employee.y", Matchers.equalTo(200)))
				.andExpect(jsonPath("$.relationship.id", Matchers.equalTo(5)))
				.andExpect(jsonPath("$.relationship.from", Matchers.equalTo(1)))
				.andExpect(jsonPath("$.relationship.to", Matchers.equalTo(3)));
	}

	@Test
	public void delete_returnsStatusOk_IfDeleted() throws Exception {
		doNothing().when(employeeService).delete(1L, 3L);

		mockMvc.perform(delete(EmployeeController.EMPLOYEES_API + "/1/3"))
				.andExpect(status().isOk());
	}

}

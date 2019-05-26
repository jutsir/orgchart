package com.example.orgchart.controllers;

import com.example.orgchart.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

abstract class AbstractControllerTest {
	MockMvc mockMvc;
	ResultMatcher contentIsJsonUtf8 = content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(getController()).build();
	}

	String asJsonString(final Object obj) {
		return JsonUtil.asJsonString(obj);
	}

	abstract Object getController();

}

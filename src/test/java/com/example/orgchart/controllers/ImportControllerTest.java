package com.example.orgchart.controllers;

import com.example.orgchart.service.ImportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockBodyContent;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ImportControllerTest extends AbstractControllerTest {

	@Mock
	private ImportService importService;

	@InjectMocks
	private ImportController importController;

	@Override
	Object getController() {
		return importController;
	}

	@Test
	public void reload_returnsStatusOk_IfReloaded() throws Exception {
		doNothing().when(importService).load();
		doNothing().when(importService).clearDatabase();

		mockMvc.perform(get(ImportController.API_RELOAD))
				.andExpect(status().isCreated());
	}
}

package com.example.orgchart.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.neo4j.ogm.session.Session;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImportServiceTest {

	@Mock
	private Session session;

	@InjectMocks
	ImportService importService;

	@Before
	public void setUp() throws Exception {
		ReflectionTestUtils.setField(importService, "IMPORT_FILE_NAME", "orgchart.cql");
	}

	@Test
	public void clearDatabase() {
		importService.clearDatabase();
		verify(session).purgeDatabase();
	}

	@Test
	public void load() {
		importService.load();
		verify(session).query(anyString(), eq(Collections.EMPTY_MAP));
	}
}

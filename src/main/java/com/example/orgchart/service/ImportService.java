/*
 * Copyright [2011-2016] "Neo Technology"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */
package com.example.orgchart.service;

import com.example.orgchart.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Objects;

/**
 * Service for import data
 * Originally created by markangrish on 18/01/2017.
 */
@Slf4j
@Service
public class ImportService {

	private Session session;

	@Value("${IMPORT_FILE_NAME}")
	private String IMPORT_FILE_NAME;

	@Autowired
	public ImportService(Session session) {
		this.session = session;
	}

	/**
	 * Clear database.
	 */
	@Transactional
	public void clearDatabase() {
		session.purgeDatabase();
	}

	/**
	 * Load data from cql file.
	 */
	@Transactional
	public void load() {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(IMPORT_FILE_NAME))));
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(" ");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String cqlFile = sb.toString();
		session.query(cqlFile, Collections.EMPTY_MAP);
		log.debug("Imported: " + IMPORT_FILE_NAME + " with data: \n" + cqlFile);
	}
}

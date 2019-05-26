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
package com.example.orgchart.controllers;

import com.example.orgchart.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for import data operation
 */
@RestController
@RequiredArgsConstructor
public class ImportController {

	static final String API_RELOAD = "/api/reload";
	private final ImportService service;

	/**
	 * Load initial employee data to the database.
	 * @return 201 http status.
	 */
	@Transactional
	@RequestMapping(API_RELOAD)
	public ResponseEntity reload() {

		service.clearDatabase();
		service.load();

		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

}
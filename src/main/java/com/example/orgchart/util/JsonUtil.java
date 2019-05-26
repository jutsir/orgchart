package com.example.orgchart.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for operations with json objects
 */
public class JsonUtil {
	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.findAndRegisterModules().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

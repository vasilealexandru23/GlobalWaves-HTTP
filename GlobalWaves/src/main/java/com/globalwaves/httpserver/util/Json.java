package com.globalwaves.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

public class Json {

	private static ObjectMapper myObjectMapper = defaultObjectMapper();

	private static ObjectMapper defaultObjectMapper() {
		ObjectMapper om = new ObjectMapper();

		/* Makes parsing not crashing when missing a field. */
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return om;
	}

	public static JsonNode parse(final String jsonSrc) throws IOException {
		return myObjectMapper.readTree(jsonSrc);
	}

	public static <A> A fromJson(final JsonNode node, Class<A> theClass) throws JsonProcessingException {
		return myObjectMapper.treeToValue(node, theClass);
	}

	public static JsonNode toJson(final Object o) {
		return myObjectMapper.valueToTree(o);
	}

	private String stringify(final JsonNode node) throws JsonProcessingException {
		return generateJson(node, false);
	}

	private String stringifyPretty(final JsonNode node) throws JsonProcessingException {
		return generateJson(node, true);
	}

	private static String generateJson(final Object o, boolean pretty) throws JsonProcessingException {
		ObjectWriter objectWriter = myObjectMapper.writer();

		if (pretty) {
			objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
		}

		return objectWriter.writeValueAsString(o);
	}
}


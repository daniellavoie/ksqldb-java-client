package dev.daniellavoie.ksqldb.client;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonUtil {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules()
			.setSerializationInclusion(Include.NON_NULL);

	public static <T> T readValue(String content, Class<T> returnType) {
		try {
			return OBJECT_MAPPER.readValue(content, returnType);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T readValue(String content, TypeReference<T> returnType) {
		try {
			return OBJECT_MAPPER.readValue(content, returnType);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String writeValueAsString(Object value) {
		try {
			return OBJECT_MAPPER.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}

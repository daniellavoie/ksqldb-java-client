/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.daniellavoie.ksqldb.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an error returned by a ksqlDB server.
 * 
 * @author Daniel Lavoie
 * @since 0.1.0
 *
 */
public class KsqlDBServerError {
	private final String type;
	private final int errorCode;
	private final String message;
	private final List<String> stackTrace;
	private final String statementText;
	private final List<String> entities;

	@JsonCreator
	public KsqlDBServerError(@JsonProperty("@type") String type, @JsonProperty("error_code") int errorCode,
			@JsonProperty("message") String message, @JsonProperty("stackTrace") List<String> stackTrace,
			@JsonProperty("statementText") String statementText, @JsonProperty("entities") List<String> entities) {
		this.type = type;
		this.errorCode = errorCode;
		this.message = message;
		this.stackTrace = stackTrace;
		this.statementText = statementText;
		this.entities = entities;
	}

	public String getType() {
		return type;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getStackTrace() {
		return stackTrace;
	}

	public String getStatementText() {
		return statementText;
	}

	public List<String> getEntities() {
		return entities;
	}

	@Override
	public String toString() {
		return "KsqlDBServerError [type=" + type + ", errorCode=" + errorCode + ", message=" + message + ", stackTrace="
				+ stackTrace + ", statementText=" + statementText + ", entities=" + entities + "]";
	}
}

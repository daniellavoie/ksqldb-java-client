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

package dev.daniellavoie.ksqldb.client.api.ksql;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertiesResponse extends KsqlResponse {
	private final Map<String, String> properties;
	private final List<String> overwrittenProperties;
	private final List<String> defaultProperties;

	@JsonCreator
	public PropertiesResponse(@JsonProperty("@type") String type, @JsonProperty("error_code") Integer errorCode,
			@JsonProperty("statementText") String statementText, @JsonProperty("warnings") List<Warning> warnings,
			@JsonProperty("properties") Map<String, String> properties,
			@JsonProperty("overwrittenProperties") List<String> overwrittenProperties,
			@JsonProperty("defaultProperties") List<String> defaultProperties) {
		super(type, errorCode, statementText, warnings);

		this.properties = properties;
		this.overwrittenProperties = overwrittenProperties;
		this.defaultProperties = defaultProperties;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public List<String> getOverwrittenProperties() {
		return overwrittenProperties;
	}

	public List<String> getDefaultProperties() {
		return defaultProperties;
	}
}

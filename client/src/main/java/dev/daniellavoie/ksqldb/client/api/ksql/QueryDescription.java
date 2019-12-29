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

public class QueryDescription {
	private final String id;
	private final String state;
	private final String statementText;
	private final List<Field> fields;
	private final List<String> sources;
	private final List<String> sinks;
	private final String executionPlan;
	private final String topology;
	private final Map<String, String> overriddenProperties;

	@JsonCreator
	public QueryDescription(@JsonProperty("id") String id, @JsonProperty("state") String state,
			@JsonProperty("statementText") String statementText, @JsonProperty("fields") List<Field> fields,
			@JsonProperty("sources") List<String> sources, @JsonProperty("sinks") List<String> sinks,
			@JsonProperty("executionPlan") String executionPlan, @JsonProperty("topology") String topology,
			@JsonProperty("overriddenProperties") Map<String, String> overriddenProperties) {
		this.id = id;
		this.state = state;
		this.statementText = statementText;
		this.fields = fields;
		this.sources = sources;
		this.sinks = sinks;
		this.executionPlan = executionPlan;
		this.topology = topology;
		this.overriddenProperties = overriddenProperties;
	}

	public String getId() {
		return id;
	}

	public String getState() {
		return state;
	}

	public String getStatementText() {
		return statementText;
	}

	public List<Field> getFields() {
		return fields;
	}

	public List<String> getSources() {
		return sources;
	}

	public List<String> getSinks() {
		return sinks;
	}

	public String getExecutionPlan() {
		return executionPlan;
	}

	public String getTopology() {
		return topology;
	}

	public Map<String, String> getOverriddenProperties() {
		return overriddenProperties;
	}
}
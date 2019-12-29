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

package dev.daniellavoie.ksqldb.client.api.query;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Row field from a query response returned by a ksqlDB server.
 * 
 * @author Daniel Lavoie
 * @since 0.1.0
 *
 */
public class Row {
	private final List<Object> columns;

	@JsonCreator
	public Row(@JsonProperty("columns") List<Object> columns) {
		this.columns = columns;
	}

	public List<Object> getColumns() {
		return columns;
	}
}
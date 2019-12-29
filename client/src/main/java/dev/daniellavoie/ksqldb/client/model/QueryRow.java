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

package dev.daniellavoie.ksqldb.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.daniellavoie.ksqldb.client.api.query.Row;

/**
 * Represents an aggregation of the initial headers received from a query
 * response joined with a row from each response of the ksqlDB server.
 * 
 * @author Daniel Lavoie
 * @since 0.1.0
 * 
 */
public class QueryRow {
	private final Row row;

	@JsonCreator
	public QueryRow(@JsonProperty("row") Row row) {
		this.row = row;
	}

	public Row getRow() {
		return row;
	}
}

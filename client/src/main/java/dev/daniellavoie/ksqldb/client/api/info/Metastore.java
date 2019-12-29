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

package dev.daniellavoie.ksqldb.client.api.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Metastore returned by a ksqlDB server from a Health check query.
 * 
 * @author Daniel Lavoie
 * @since 0.1.0
 *
 */
public class Metastore {
	private final boolean healthy;

	@JsonCreator
	public Metastore(@JsonProperty("isHealthy") boolean healthy) {
		this.healthy = healthy;
	}

	public boolean isHealthy() {
		return healthy;
	}
}
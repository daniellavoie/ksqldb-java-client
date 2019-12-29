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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DescribeResponse extends KsqlResponse {
	public enum SourceType {
		STREAM, TABLE
	}

	private final SourceDescription sourceDescription;

	@JsonCreator
	public DescribeResponse(@JsonProperty("@type") String type, @JsonProperty("error_code") Integer errorCode,
			@JsonProperty("statementText") String statementText, @JsonProperty("warnings") List<Warning> warnings,
			@JsonProperty("sourceDescription") SourceDescription sourceDescription) {
		super(type, errorCode, statementText, warnings);

		this.sourceDescription = sourceDescription;
	}

	public SourceDescription getSourceDescription() {
		return sourceDescription;
	}
}

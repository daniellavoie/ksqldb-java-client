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

public abstract class KsqlResponse {
	private final String type;
	private final Integer errorCode;
	private final String statementText;
	private final List<Warning> warnings;

	public KsqlResponse(String type, Integer errorCode, String statementText, List<Warning> warnings) {
		this.type = type;
		this.errorCode = errorCode;
		this.statementText = statementText;
		this.warnings = warnings;
	}

	public String getType() {
		return type;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getStatementText() {
		return statementText;
	}

	public List<Warning> getWarnings() {
		return warnings;
	}


}

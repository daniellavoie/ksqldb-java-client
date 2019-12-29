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

import java.util.Map;

public class KsqlRequest {
	private final String ksql;
	private final Map<String, String> streamsProperties;
	private final Long commandSequenceNumber;

	public KsqlRequest(String ksql) {
		this(ksql, null, null);
	}

	public KsqlRequest(String ksql, Map<String, String> streamsProperties, Long commandSequenceNumber) {
		this.ksql = ksql;
		this.streamsProperties = streamsProperties;
		this.commandSequenceNumber = commandSequenceNumber;
	}

	public String getKsql() {
		return ksql;
	}

	public Map<String, String> getStreamsProperties() {
		return streamsProperties;
	}

	public Long getCommandSequenceNumber() {
		return commandSequenceNumber;
	}

}

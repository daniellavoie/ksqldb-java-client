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

public class CommandResponse extends KsqlResponse {
	private final String commandId;
	private final CommandStatus commandStatus;
	private final long commandSequenceNumber;

	@JsonCreator
	public CommandResponse(@JsonProperty("@type") String type, @JsonProperty("error_code") Integer errorCode,
			@JsonProperty("statementText") String statementText, @JsonProperty("warnings") List<Warning> warnings,
			@JsonProperty("commandId") String commandId, @JsonProperty("commandStatus") CommandStatus commandStatus,
			@JsonProperty("commandSequenceNumber") long commandSequenceNumber) {
		super(type, errorCode, statementText, warnings);

		this.commandId = commandId;
		this.commandStatus = commandStatus;
		this.commandSequenceNumber = commandSequenceNumber;
	}

	public String getCommandId() {
		return commandId;
	}

	public CommandStatus getCommandStatus() {
		return commandStatus;
	}

	public long getCommandSequenceNumber() {
		return commandSequenceNumber;
	}
}

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

import dev.daniellavoie.ksqldb.client.api.ksql.DescribeResponse.SourceType;

public class SourceDescription {
	private final String name;
	private final List<Query> readQueries;
	private final List<Query> writeQueries;
	private final List<Field> fields;
	private final SourceType type;
	private final String key;
	private final String timestamp;
	private final Format format;
	private final String topic;
	private final boolean extended;
	private final String statistics;
	private final String errorStats;
	private final int replication;
	private final int partitions;

	@JsonCreator
	public SourceDescription(@JsonProperty("name") String name, @JsonProperty("readQueries") List<Query> readQueries,
			@JsonProperty("writeQueries") List<Query> writeQueries, @JsonProperty("fields") List<Field> fields,
			@JsonProperty("type") SourceType type, @JsonProperty("key") String key,
			@JsonProperty("timestamp") String timestamp, @JsonProperty("format") Format format,
			@JsonProperty("topic") String topic, @JsonProperty("extended") boolean extended,
			@JsonProperty("statistics") String statistics, @JsonProperty("errorStats") String errorStats,
			@JsonProperty("replication") int replication, @JsonProperty("partitions") int partitions) {
		this.name = name;
		this.readQueries = readQueries;
		this.writeQueries = writeQueries;
		this.fields = fields;
		this.type = type;
		this.key = key;
		this.timestamp = timestamp;
		this.format = format;
		this.topic = topic;
		this.extended = extended;
		this.statistics = statistics;
		this.errorStats = errorStats;
		this.replication = replication;
		this.partitions = partitions;
	}

	public String getName() {
		return name;
	}

	public List<Query> getReadQueries() {
		return readQueries;
	}

	public List<Query> getWriteQueries() {
		return writeQueries;
	}

	public List<Field> getFields() {
		return fields;
	}

	public SourceType getType() {
		return type;
	}

	public String getKey() {
		return key;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public Format getFormat() {
		return format;
	}

	public String getTopic() {
		return topic;
	}

	public boolean isExtended() {
		return extended;
	}

	public String getStatistics() {
		return statistics;
	}

	public String getErrorStats() {
		return errorStats;
	}

	public int getReplication() {
		return replication;
	}

	public int getPartitions() {
		return partitions;
	}
}
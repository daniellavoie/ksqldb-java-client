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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Stream {
	public enum Type {
		STREAM
	}

	private final String name;
	private final String topic;
	private final Format format;
	private final Type type;

	@JsonCreator
	public Stream(@JsonProperty("name") String name, @JsonProperty("topic") String topic,
			@JsonProperty("format") Format format, @JsonProperty("type") Type type) {
		this.name = name;
		this.topic = topic;
		this.format = format;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getTopic() {
		return topic;
	}

	public Format getFormat() {
		return format;
	}

	public Type getType() {
		return type;
	}
}
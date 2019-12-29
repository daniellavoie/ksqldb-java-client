/*
 * Copyright 2012-2020 the original author or authors.
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

package dev.daniellavoie.ksqldb.client;

import java.util.List;
import java.util.stream.Collectors;

import dev.daniellavoie.ksqldb.client.api.ksql.CommandResponse;
import dev.daniellavoie.ksqldb.client.api.ksql.KsqlRequest;
import reactor.core.publisher.Mono;

/**
 * Offers utility functions to help managing ksqlDB streams and tables.
 * 
 * @author Daniel Lavoie
 * @since 0.1.0
 */
public class AdminUtil {
	private KsqlDBClient ksqlDBClient;

	AdminUtil(KsqlDBClient ksqlDBClient) {
		this.ksqlDBClient = ksqlDBClient;
	}

	/**
	 * Checks if any of the streams in ksqlDB already exists with the given name.
	 * Will create a new stream with the provided configuration it does not already
	 * exists.
	 * 
	 * @param streamName        name of the stream
	 * @param topicName         topic on which the stream is based
	 * @param valueFormat       value format for the stream
	 * @param columnDefinitions List of all the columns
	 * @return a {@link Mono} that emits true if the stream is created or false if
	 *         it's already present.
	 */
	public Mono<Boolean> createStreamIfMissing(String streamName, String topicName, ValueFormat valueFormat,
			List<ColumnDefinition> columnDefinitions) {

		return ksqlDBClient.streams(new KsqlRequest("SHOW STREAMS;")).flatMapIterable(response -> response.getStreams())
				.filter(stream -> stream.getName().equals(streamName))

				.map(stream -> false)

				.switchIfEmpty(createStreamFromTopic(streamName, topicName, valueFormat, columnDefinitions)
						.map(response -> true))

				.last();
	}

	/**
	 * Generates and executes a ksqlDB request to create a stream with the provided
	 * configuration.
	 * 
	 * @param streamName        name of the stream
	 * @param topicName         topic on which the stream is based
	 * @param valueFormat       value format for the stream
	 * @param columnDefinitions List of all the columns
	 * @return a {@link Mono} that emits a response for the ksqlDB statement.
	 */
	public Mono<CommandResponse> createStreamFromTopic(String streamName, String topicName, ValueFormat valueFormat,
			List<ColumnDefinition> columnDefinitions) {
		String fields = columnDefinitions.stream().map(entry -> entry.getName() + " " + entry.getDataType())
				.collect(Collectors.joining(", "));

		return ksqlDBClient.execute(new KsqlRequest("CREATE STREAM " + streamName + " (" + fields
				+ ") WITH (kafka_topic='" + topicName + "', value_format='" + valueFormat + "');")).last();
	}
}

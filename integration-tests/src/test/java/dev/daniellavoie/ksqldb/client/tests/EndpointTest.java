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

package dev.daniellavoie.ksqldb.client.tests;

import java.time.Duration;
import java.util.Arrays;

import org.apache.kafka.clients.admin.AdminClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;

import dev.daniellavoie.ksqldb.client.ColumnDefinition;
import dev.daniellavoie.ksqldb.client.DataType;
import dev.daniellavoie.ksqldb.client.KsqlDBClient;
import dev.daniellavoie.ksqldb.client.KsqlDBServerException;
import dev.daniellavoie.ksqldb.client.ValueFormat;
import dev.daniellavoie.ksqldb.client.api.ksql.CommandResponse;
import dev.daniellavoie.ksqldb.client.api.ksql.KsqlRequest;
import dev.daniellavoie.ksqldb.client.api.ksql.Query;
import dev.daniellavoie.ksqldb.client.tests.kafka.KafkaUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
public abstract class EndpointTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(EndpointTest.class);

	protected static String TOPIC_NAME = "kafka-java-client-transaction";
	protected static String TABLE_NAME = "KAFKA_JAVA_CLIENT_TRANSACTION_STATS";
	protected static String STREAM_NAME = "KAFKA_JAVA_CLIENT_TRANSACTION_STREAM";

	@Autowired
	protected KafkaProperties kafkaProperties;

	@Autowired
	private AdminClient adminClient;

	@Autowired
	protected KsqlDBClient ksqlDBClient;

	protected String lastQueryId;

	protected Flux<Query> getQueries() {
		return ksqlDBClient.queries(new KsqlRequest("SHOW QUERIES;"))
				.flatMapIterable(response -> response.getQueries());
	}

	private void cleanQueries() {
		getQueries()

				.flatMap(query -> ksqlDBClient.execute(new KsqlRequest("TERMINATE " + query.getId() + ";")))

				.blockLast();
	}

	@BeforeEach
	public void cleanTableAndStream() {
		cleanQueries();

		// Drop stream if it exists.
		try {
			CommandResponse response = ksqlDBClient
					.execute(new KsqlRequest("DROP STREAM IF EXISTS " + STREAM_NAME + ";")).blockFirst();

			Assertions.assertNotNull(response);
		} catch (KsqlDBServerException serverEx) {
			LOGGER.info("Failed to delete stream " + STREAM_NAME + ".", serverEx);
		}

		// Drop table if it exists.
		try {
			CommandResponse response = ksqlDBClient.execute(new KsqlRequest("DROP TABLE IF EXISTS " + TABLE_NAME + ";"))
					.blockFirst();

			Assertions.assertNotNull(response);
		} catch (KsqlDBServerException serverEx) {
			LOGGER.info("Failed to delete stream " + STREAM_NAME + ".", serverEx);
		}

		KafkaUtil.createTopicIfMissing(TOPIC_NAME, adminClient);

		ksqlDBClient.getAdminUtil()
				.createStreamIfMissing(STREAM_NAME, TOPIC_NAME, ValueFormat.JSON,
						Arrays.asList(new ColumnDefinition("account", DataType.STRING.toString()),
								new ColumnDefinition("creditCurrency", DataType.STRING.toString()),
								new ColumnDefinition("creditAmount", DataType.DOUBLE.toString()),
								new ColumnDefinition("debitCurrency", DataType.STRING.toString()),
								new ColumnDefinition("debitAmount", DataType.DOUBLE.toString()),
								new ColumnDefinition("timestamp", DataType.ARRAY + "<" + DataType.INTEGER + ">")))
				.block();

		execute("CREATE TABLE " + TABLE_NAME + " AS SELECT account, count(1) transactionCount FROM " + STREAM_NAME
				+ " GROUP BY  account;");

		lastQueryId = getQueries().blockFirst().getId();

	}

	protected CommandResponse execute(String ksqlStatement) {
		KsqlRequest ksqlTableRequest = new KsqlRequest(ksqlStatement);

		CommandResponse response = ksqlDBClient.execute(ksqlTableRequest).blockFirst();

		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getCommandStatus());
		Assertions.assertEquals("SUCCESS", response.getCommandStatus().getStatus());

		return response;
	}

	protected Mono<Void> awaitQueryToBeRunning(String queryId) {
		return Mono.create(sink -> {
			ksqlDBClient.explain(new KsqlRequest("EXPLAIN " + queryId + ";"))

					.doOnNext(explainResponse -> LOGGER.info(
							"Waiting for query {} to be in a running state. Current state : {}.", queryId,
							explainResponse.getQueryDescription().getState()))

					.filter(explainResponse -> explainResponse.getQueryDescription().getState().equals("RUNNING"))

					.doOnNext(explainResponse -> sink.success(explainResponse))

					.switchIfEmpty(Mono.error(() -> new RuntimeException(queryId + " is not running yet.")))

					.doOnError(throwable -> sink.error(throwable))

					.subscribe();
		}).retryBackoff(5, Duration.ofSeconds(1))

				.then();
	}
}

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

package dev.daniellavoie.ksqldb.client.tests.query;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import dev.daniellavoie.ksqldb.client.api.query.QueryRequest;
import dev.daniellavoie.ksqldb.client.model.QueryRow;
import dev.daniellavoie.ksqldb.client.tests.EndpointTest;
import dev.daniellavoie.ksqldb.client.tests.Transaction;
import dev.daniellavoie.ksqldb.client.tests.Transaction.Type;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class QueryTest extends EndpointTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryTest.class);

	private KafkaTemplate<String, Transaction> transactionTemplate;
	private Disposable disposable;

	@BeforeEach
	public void setup() {
		transactionTemplate = new KafkaTemplate<String, Transaction>(
				new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties()));
	}

	@AfterEach
	public void tearDown() {
		if (disposable != null) {
			disposable.dispose();
		}
	}

	@Test
	public void assertPullQuery() throws InterruptedException {
		awaitQueryToBeRunning(lastQueryId).block();

		List<QueryRow> results = ksqlDBClient
				.pullQuery(new QueryRequest("SELECT * FROM " + TABLE_NAME + " WHERE ROWKEY='1';"))

				.switchIfEmpty(Mono.error(() -> new RuntimeException("No result found yet")))

				.retryBackoff(5, Duration.ofSeconds(1))

				.collectList()

				.block();

		Assertions.assertEquals(1, results.size());
	}

	@Test
	public void assertPushQuery() throws InterruptedException {
		awaitQueryToBeRunning(lastQueryId).block();

		Flux<QueryRow> flux = Flux.<QueryRow>create(sink -> {
			disposable = ksqlDBClient
					.pushQuery(new QueryRequest("SELECT * FROM " + TABLE_NAME + " WHERE ROWKEY='1' EMIT CHANGES;"))

					.doOnNext(queryRow -> LOGGER.info("Received a query event."))

					.doOnNext(sink::next)

					.doOnError(sink::error)

					.doOnComplete(sink::complete)

					.subscribeOn(Schedulers.elastic())

					.publishOn(Schedulers.elastic())

					.subscribe();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				LOGGER.error("Sleep interrupted", e);
			}

			Flux.range(0, 50)

					.doOnNext(index -> LOGGER.info("Generating a transaction."))

					.map(index -> new Transaction(UUID.randomUUID().toString(), "1", Type.DEPOSIT, null, "USD",
							BigDecimal.ONE, "USD", LocalDateTime.now()))

					.flatMap(transaction -> Mono.fromFuture(
							transactionTemplate.send(TOPIC_NAME, transaction.getAccount(), transaction).completable()))

					.blockLast();

			transactionTemplate.flush();

			LOGGER.info("Sent the events to Kafka.");
		});

		QueryRow result = flux

				.blockFirst(Duration.ofSeconds(30));

		Assertions.assertEquals("1", result.getRow().getColumns().get(1));
		Assertions.assertEquals("1", result.getRow().getColumns().get(2));
		Assertions.assertEquals(50, result.getRow().getColumns().get(3));
	}

}

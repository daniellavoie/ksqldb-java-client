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

package dev.daniellavoie.ksqldb.client;

import dev.daniellavoie.ksqldb.client.api.info.HealthcheckResponse;
import dev.daniellavoie.ksqldb.client.api.info.InfoResponse;
import dev.daniellavoie.ksqldb.client.api.ksql.CommandResponse;
import dev.daniellavoie.ksqldb.client.api.ksql.DescribeResponse;
import dev.daniellavoie.ksqldb.client.api.ksql.ExplainResponse;
import dev.daniellavoie.ksqldb.client.api.ksql.KsqlRequest;
import dev.daniellavoie.ksqldb.client.api.ksql.PropertiesResponse;
import dev.daniellavoie.ksqldb.client.api.ksql.QueriesResponse;
import dev.daniellavoie.ksqldb.client.api.ksql.StreamsResponse;
import dev.daniellavoie.ksqldb.client.api.ksql.TablesResponse;
import dev.daniellavoie.ksqldb.client.api.query.QueryRequest;
import dev.daniellavoie.ksqldb.client.model.QueryRow;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface to interact with a ksqlDB Server. Provides access to all REST
 * endpoints offered by a ksqlDB server.
 * 
 * @author Daniel Lavoie
 * @since 0.1.0
 *
 */
public interface KsqlDBClient {
	static KsqlDBClient create(String ksqlDBUrl, String ksqlDBWebSocketUrl) {
		return new DefaultKsqlDBClient(ksqlDBUrl, ksqlDBWebSocketUrl);
	}

	/**
	 * Executes asynchronous DESCRIBE statement.
	 * 
	 * @param ksqlRequest a request containing one or multiple DESCRIBE statement
	 *                    seperated by a ";".
	 * @return A {@link Flux} that emits a response for every query in the
	 *         {@link KsqlRequest}.
	 */
	Flux<DescribeResponse> describe(KsqlRequest ksqlRequest);

	/**
	 * Executes asynchronous SHOW STREAMS statement.
	 * 
	 * @param ksqlRequest A request containing one or multiple SHOW STREAMS
	 *                    statement seperated by a ";".
	 * @return a {@link Flux} that emits a response for every statement in the
	 *         {@link KsqlRequest}.
	 */
	Flux<StreamsResponse> streams(KsqlRequest ksqlRequest);

	/**
	 * Executes asynchronous CREATE, DROP or TERMINATE statement.
	 * 
	 * @param ksqlRequest a request containing one or multiple CREATE, DROP or
	 *                    TERMINATE statement seperated by a ";".
	 * @return a {@link Flux} that emits a response for every statement in the
	 *         {@link KsqlRequest}.
	 */
	Flux<CommandResponse> execute(KsqlRequest ksqlRequest);

	/**
	 * Executes asynchronous EXPLAIN statement against a query.
	 * 
	 * @param ksqlRequest a request containing one or multiple EXPLAIN statement
	 *                    seperated by a ";".
	 * @return a {@link Flux} that emits a response for every statement in the
	 *         {@link KsqlRequest}.
	 */
	Flux<ExplainResponse> explain(KsqlRequest ksqlRequest);

	/**
	 * Provides access to the {@link AdminUtil} embedded with the client.
	 * 
	 * @return a {@link AdminUtil}
	 */
	AdminUtil getAdminUtil();

	/**
	 * Executes an asynchronous health check request to the ksqlDB server.
	 * 
	 * @return A {@link Mono} that emits an health check response when the request
	 *         completes.
	 */
	Mono<HealthcheckResponse> getHealthcheck();

	/**
	 * Executes an asynchronous info request to the ksqlDB server.
	 * 
	 * @return A {@link Mono} that emits an info response when the request
	 *         completes.
	 */
	Mono<InfoResponse> getInfo();

	/**
	 * Executes asynchronous SHOW PROPERTIES statement.
	 * 
	 * @param ksqlRequest a request containing one or multiple SHOW PROPERTIES
	 *                    statement seperated by a ";".
	 * @return A {@link Flux} that emits a response for every statement in the
	 *         {@link KsqlRequest}.
	 */
	Flux<PropertiesResponse> properties(KsqlRequest ksqlRequest);

	/**
	 * Executes asynchronous SHOW QUERIES statement.
	 * 
	 * @param ksqlRequest a request containing one or multiple SHOW QUERIES
	 *                    statement seperated by a ";".
	 * @return A {@link Flux} that emits a response for every statement in the
	 *         {@link KsqlRequest}.
	 */
	Flux<QueriesResponse> queries(KsqlRequest ksqlRequest);

	/**
	 * Executes an asynchronous SELECT statement against a TABLE. The {@link Flux}
	 * return by the pull query will eventually send a complete signal as the server
	 * will handle this query as a request reply. The submitted SELECT STATEMENT
	 * should not contain EMIT CHANGES. See {@link pushQuery} for a push query that
	 * never ends.
	 * 
	 * @param queryRequest a request containing a SELECT statement without EMIT
	 *                     CHANGES.
	 * @return a {@link Flux} that emits a {@link QueryRow} for every result from
	 *         the query. Eventually sends a complete signal.
	 */
	Flux<QueryRow> pullQuery(QueryRequest queryRequest);

	/**
	 * Executes an asynchronous SELECT statement against a TABLE. The {@link Flux}
	 * return by the push query will never send a complete signal as the server will
	 * handle this query as a push stream. The submitted SELECT STATEMENT should
	 * must contain EMIT CHANGES. See {@link pullQuery} for a pull query that acts a
	 * request reply.
	 * 
	 * @param queryRequest a request containing a SELECT statement with EMIT
	 *                     CHANGES.
	 * @return a {@link Flux} that emits a {@link QueryRow} for every result from
	 *         the query. Never sends a complete signal.
	 */
	Flux<QueryRow> pushQuery(QueryRequest queryRequest);

	/**
	 * Executes asynchronous SHOW TABLES statement.
	 * 
	 * @param ksqlRequest a request containing one or multiple SHOW TABLES statement
	 *                    seperated by a ";".
	 * @return A {@link Flux} that emits a response for every statement in the
	 *         {@link KsqlRequest}.
	 */
	Flux<TablesResponse> tables(KsqlRequest ksqlRequest);
}

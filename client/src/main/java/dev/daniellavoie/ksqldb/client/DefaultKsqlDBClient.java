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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

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
import dev.daniellavoie.ksqldb.client.api.query.QueryResponse;
import dev.daniellavoie.ksqldb.client.model.QueryRow;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Default implementation for {@link KsqlDBClient}. Leverages
 * {@link ReactorWebClient} as a HTTP Client.
 * 
 * @author Daniel Lavoie
 * @since 0.1.0
 *
 */
public class DefaultKsqlDBClient implements KsqlDBClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKsqlDBClient.class);

	private final WebClient webClient;
	private final AdminUtil adminUtil;

	public DefaultKsqlDBClient(String ksqlDBUrl, String ksqlDBWebSocketUrl) {
		webClient = new ReactorWebClient(ksqlDBUrl, ksqlDBWebSocketUrl);
		adminUtil = new AdminUtil(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<DescribeResponse> describe(KsqlRequest ksqlRequest) {
		return webClient.post("/ksql", ksqlRequest, new TypeReference<DescribeResponse[]>() {
		}).flatMapMany(responses -> Flux.fromArray(responses));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<CommandResponse> execute(KsqlRequest ksqlRequest) {
		return webClient.post("/ksql", ksqlRequest, new TypeReference<CommandResponse[]>() {
		}).flatMapMany(responses -> Flux.fromArray(responses));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<ExplainResponse> explain(KsqlRequest ksqlRequest) {
		return webClient.post("/ksql", ksqlRequest, new TypeReference<ExplainResponse[]>() {
		}).flatMapMany(responses -> Flux.fromArray(responses));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdminUtil getAdminUtil() {
		return adminUtil;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<HealthcheckResponse> getHealthcheck() {
		return webClient.get("/healthcheck", HealthcheckResponse.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<InfoResponse> getInfo() {
		return webClient.get("/info", InfoResponse.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<PropertiesResponse> properties(KsqlRequest ksqlRequest) {
		return webClient.post("/ksql", ksqlRequest, new TypeReference<PropertiesResponse[]>() {
		}).flatMapMany(responses -> Flux.fromArray(responses));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<QueryRow> pullQuery(QueryRequest queryRequest) {
		Flux<QueryResponse> responseFlux = webClient
				.postForMany("/query", queryRequest, new TypeReference<QueryResponse[]>() {
				}).flatMap(responses -> Flux.fromArray(responses));

		return responseFlux.flatMap(new QueryResponseMapper());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<QueryRow> pushQuery(QueryRequest queryRequest) {
		Map<String, String> params = new HashMap<>();

		params.put("request", JsonUtil.writeValueAsString(queryRequest));

		return webClient.getWithWebSocket("/ws/query", params).flatMap(new WebSocketQueryResponseMapper());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<QueriesResponse> queries(KsqlRequest ksqlRequest) {
		return webClient.post("/ksql", ksqlRequest, new TypeReference<QueriesResponse[]>() {
		}).flatMapMany(responses -> Flux.fromArray(responses));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<StreamsResponse> streams(KsqlRequest ksqlRequest) {
		return webClient.post("/ksql", ksqlRequest, new TypeReference<StreamsResponse[]>() {
		}).flatMapMany(responses -> Flux.fromArray(responses));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<TablesResponse> tables(KsqlRequest ksqlRequest) {
		return webClient.post("/ksql", ksqlRequest, new TypeReference<TablesResponse[]>() {
		}).flatMapMany(responses -> Flux.fromArray(responses));
	}

	private class QueryResponseMapper implements Function<QueryResponse, Mono<QueryRow>> {
		private Map<String, String> header;

		@Override
		public Mono<QueryRow> apply(QueryResponse queryResponse) {
			if (queryResponse.getErrorMessage() != null) {
				return Mono.error(new RuntimeException(queryResponse.getErrorMessage()));
			}

			if (header == null) {
				LOGGER.trace("Processing query headers.");

				header = queryResponse.getHeader();
				return Mono.empty();
			}
			LOGGER.trace("Processing query row.");

			return Mono.just(new QueryRow(queryResponse.getRow()));
		}
	}

	private class WebSocketQueryResponseMapper implements Function<String, Mono<QueryRow>> {
		private boolean headerSkipped;

		@Override
		public Mono<QueryRow> apply(String value) {
			LOGGER.trace("Processing websocket payload {}.", value);

			if (!headerSkipped) {
				headerSkipped = true;
				return Mono.empty();
			} else {
				return Mono.just(JsonUtil.readValue(value, QueryRow.class));
			}
		}
	}

}

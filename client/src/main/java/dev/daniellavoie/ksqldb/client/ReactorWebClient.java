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

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;

/**
 * Implementation of {@link WebClient} based on Reactor Netty. Contains specific
 * handling for the /query endpoint of ksqlDB with EMIT CHANGES enabled.
 * 
 * @author Daniel Lavoie
 * 
 * @since 0.1.0
 *
 */
public class ReactorWebClient implements WebClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReactorWebClient.class);

	private String baseWebSocketUrl;

	private static final ObjectMapper OBJECTMAPPER = new ObjectMapper().findAndRegisterModules();
	private HttpClient client;
	private HttpClient webSocketClient;

	public ReactorWebClient(String baseUrl, String baseWebSocketUrl) {
		this.baseWebSocketUrl = baseWebSocketUrl;
		client = HttpClient.create().baseUrl(baseUrl)
				.headers(headerBuilder -> headerBuilder.add("Content-Type", "application/vnd.ksql.v1+json"));

		webSocketClient = HttpClient.create().baseUrl(baseWebSocketUrl);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Mono<T> get(String url, Class<T> returnType) {
		return client.get().uri(url).responseContent().aggregate().asString()
				.map(value -> readValue(value, returnType));
	}

	@Override
	public Flux<String> getWithWebSocket(String url, Map<String, String> params) {
		String parameters = params.entrySet().stream()

				.map(entry -> URLEncoderUtil.encode(entry.getKey()) + "=" + URLEncoderUtil.encode(entry.getValue()))

				.collect(Collectors.joining("&"));

		return webSocketClient.websocket().uri(baseWebSocketUrl + url + "?" + parameters)

				.handle((inbound, outbound) -> inbound.receive().asString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Mono<T> post(String url, Object body, TypeReference<T> returnType) {
		return client.post().uri(url)

				.send(ByteBufFlux.fromString(Mono.just(writeValue(body))))

				.responseContent().aggregate().asString().map(value -> readValue(value, returnType));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Contains specific handing for the /query endpoint of ksqlDB. The server
	 * responds periodically with line breaks. This method skip those HTTP chunks
	 * and will only emits events when a proper JSON structure is received from the
	 * server within an HTTP chunk.
	 * </p>
	 */
	@Override
	public <T> Flux<T> postForMany(String url, Object body, TypeReference<T> returnType) {
		return client.post().uri(url).send(ByteBufFlux.fromString(Mono.just(writeValue(body))))

				.responseContent().asString().map(value -> readStreamValue(value, returnType))

				.filter(Optional::isPresent)

				.map(Optional::get);
	}

	private <T> T readValue(String value, TypeReference<T> returnType) {
		try {
			return OBJECTMAPPER.readValue(value, returnType);
		} catch (IOException e) {
			try {
				throw new KsqlDBServerException(OBJECTMAPPER.readValue(value, KsqlDBServerError.class));
			} catch (IOException e2) {
				LOGGER.error("Failed to read {}.", value);
				throw new RuntimeException(e);
			}
		}
	}

	private <T> T readValue(String value, Class<T> returnType) {
		try {
			return OBJECTMAPPER.readValue(value, returnType);
		} catch (IOException e) {
			try {
				throw new KsqlDBServerException(OBJECTMAPPER.readValue(value, KsqlDBServerError.class));
			} catch (IOException e2) {
				LOGGER.error("Failed to read {}.", value);
				throw new RuntimeException(e);
			}
		}
	}

	private <T> Optional<T> readStreamValue(String value, TypeReference<T> returnType) {
		LOGGER.trace("Received payload {}.", value);

		try {
			if (value.equals(",\n") || value.equals("\n")) {
				return Optional.empty();
			}

			if (value.startsWith("[") && !value.endsWith("]")) {
				value = value.substring(1);
			}

			if (!value.startsWith("[") && value.endsWith("]")) {
				value = value.substring(0, value.length());
			}

			if (value.endsWith(",")) {
				value = value.substring(0, value.length());
			}

			return Optional.of(OBJECTMAPPER.readValue(value, returnType));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String writeValue(Object value) {
		try {
			return OBJECTMAPPER.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}

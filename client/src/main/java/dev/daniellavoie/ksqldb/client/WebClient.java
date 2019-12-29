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

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * Reactive interface to interact with a rest endpoint.
 * 
 * @author Daniel Lavoie
 * @since 0.1.0
 *
 */
public interface WebClient {
	/**
	 * Executes a GET HTTP request that emits a single response.
	 * 
	 * @param <T>        Typed object matching the response body.
	 * @param url        URL for the GET request
	 * @param returnType Class instance of the type to be emitted.
	 * @return Deserialized response content
	 */
	<T> Mono<T> get(String url, Class<T> returnType);
	
	Flux<String> getWithWebSocket(String url, Map<String, String> params);

	/**
	 * Executes a POST HTTP request that emits a single response.
	 * 
	 * @param <T>        A typed object matching the response body.
	 * @param url        URL for the POST request
	 * @param body       Object to be serialized as the POST request body
	 * @param returnType Class instance of the type to be emitted.
	 * @return A {@link Mono} that emits an event when
	 */
	<T> Mono<T> post(String url, Object body, TypeReference<T> returnType);

	/**
	 * Executes a POST HTTP request that emits multiple typed events until a final
	 * completion signal is sent.
	 * 
	 * @param <T>        A typed object matching the response body that are emitted
	 *                   until completion.
	 * @param url        URL for the POST request
	 * @param body       Object to be serialized as the POST request body
	 * @param returnType Class instance of the type to be emitted.
	 * @return A {@link Flux} that emits an event for each HTTP chunk received by
	 *         the client.
	 */
	<T> Flux<T> postForMany(String url, Object body, TypeReference<T> returnType);

}

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

/**
 * Exception that can propagate {@link KsqlDBServerError} returned from a ksqlDB
 * server.
 * 
 * @author Daniel Lavoie
 * @since 0.1.0
 *
 */
public class KsqlDBServerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final KsqlDBServerError error;

	public KsqlDBServerException(KsqlDBServerError error) {
		super(error.toString());

		this.error = error;
	}

	public KsqlDBServerError getError() {
		return error;
	}

	@Override
	public String toString() {
		return "KsqlDBServerException [error=" + error + "]";
	}
}

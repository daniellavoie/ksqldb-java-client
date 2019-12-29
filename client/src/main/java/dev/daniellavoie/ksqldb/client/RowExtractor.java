package dev.daniellavoie.ksqldb.client;

import java.util.Optional;

import dev.daniellavoie.ksqldb.client.api.query.Row;

public class RowExtractor implements ValueExtractor<Row> {

	@Override
	public Optional<Row> extractValue(String value) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}

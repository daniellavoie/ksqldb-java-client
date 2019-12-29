package dev.daniellavoie.ksqldb.client;

import java.util.Optional;

public interface ValueExtractor<T> {
	Optional<T> extractValue(String value);
}

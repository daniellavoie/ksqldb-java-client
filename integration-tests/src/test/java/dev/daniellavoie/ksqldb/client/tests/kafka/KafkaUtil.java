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

package dev.daniellavoie.ksqldb.client.tests.kafka;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class KafkaUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaUtil.class);

	public static void createTopicIfMissing(String topicName, AdminClient adminClient) {
		try {
			if (!adminClient.listTopics().names().get().stream()
					.filter(existingTopic -> existingTopic.equals(topicName)).findAny().isPresent()) {
				LOGGER.info("Creating topic {}.", topicName);

				NewTopic topic = new NewTopic(topicName, 1, (short)1);

				adminClient.createTopics(Arrays.asList(topic)).all().get();
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}

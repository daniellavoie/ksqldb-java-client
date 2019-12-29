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

package dev.daniellavoie.ksqldb.client.tests.info;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.daniellavoie.ksqldb.client.KsqlDBClient;
import dev.daniellavoie.ksqldb.client.api.info.HealthcheckResponse;
import dev.daniellavoie.ksqldb.client.api.info.InfoResponse;

@SpringBootTest
public class InfoTests {
	@Autowired
	private KsqlDBClient ksqlDBClient;

	@Test
	public void assertInfo() {
		InfoResponse infoResponse = ksqlDBClient.getInfo().block();

		Assertions.assertNotNull(infoResponse);
		Assertions.assertNotNull(infoResponse.getKsqlServerInfo());
		Assertions.assertNotNull(infoResponse.getKsqlServerInfo().getVersion());
		Assertions.assertNotNull(infoResponse.getKsqlServerInfo().getKsqlServiceId());
		Assertions.assertNotNull(infoResponse.getKsqlServerInfo().getKafkaClusterId());
	}

	@Test
	public void assertHealthcheck() {
		HealthcheckResponse healthcheckResponse = ksqlDBClient.getHealthcheck().block();

		Assertions.assertNotNull(healthcheckResponse);
		Assertions.assertTrue(healthcheckResponse.isHealthy());
		Assertions.assertNotNull(healthcheckResponse.getDetails());
		
		Assertions.assertNotNull(healthcheckResponse.getDetails().getKafka());
		Assertions.assertTrue(healthcheckResponse.getDetails().getKafka().isHealthy());
		
		Assertions.assertNotNull(healthcheckResponse.getDetails().getMetastore());
		Assertions.assertTrue(healthcheckResponse.getDetails().getMetastore().isHealthy());
	}

}

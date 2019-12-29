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

package dev.daniellavoie.ksqldb.client.tests;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {
	public enum Type {
		DEPOSIT, EXCHANGE, WIDTHDRAW
	}

	private final String guid;
	private final String account;
	private final Type type;
	private final BigDecimal debitAmount;
	private final String debitCurrency;
	private final BigDecimal creditAmount;
	private final String creditCurrency;
	private final LocalDateTime timestamp;

	@JsonCreator
	public Transaction(@JsonProperty("guid") String guid, @JsonProperty("account") String account,
			@JsonProperty("type") Type type, @JsonProperty("debitAmount") BigDecimal debitAmount,
			@JsonProperty("debitCurrency") String debitCurrency, @JsonProperty("creditAmount") BigDecimal creditAmount,
			@JsonProperty("creditCurrency") String creditCurrency, @JsonProperty("timestamp") LocalDateTime timestamp) {
		this.guid = guid;
		this.account = account;
		this.type = type;
		this.debitAmount = debitAmount;
		this.debitCurrency = debitCurrency;
		this.creditAmount = creditAmount;
		this.creditCurrency = creditCurrency;
		this.timestamp = timestamp;
	}

	public String getGuid() {
		return guid;
	}

	public String getAccount() {
		return account;
	}

	public Type getType() {
		return type;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

	public String getDebitCurrency() {
		return debitCurrency;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public String getCreditCurrency() {
		return creditCurrency;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "Transaction [guid=" + guid + ", account=" + account + ", type=" + type + ", debitAmount=" + debitAmount
				+ ", debitCurrency=" + debitCurrency + ", creditAmount=" + creditAmount + ", creditCurrency="
				+ creditCurrency + ", timestamp=" + timestamp + "]";
	}
}

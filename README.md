# ksqlDB Java Client

This library is a Java wrapper to the ksqlDB Rest API. It offers a reactive API to interact with ksqlDB. 

This project is in an early state but currently offers supports of all the ksqlDB public APIs.

It will soon be available on Maven Central.

## Version compatibility

| ksqlDB Java Client version | ksqlDB | Confluent Platform |
|---|---|---|
| 0.2.x | 0.6.x | 5.4.x |

## Usage

### Dependencies

In pom.xml, add the following repository and dependencies.

```xml
  <repositories>
    ...
    <repository>
      <id>oss-sonatype-snapshot</id>  
      <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
  </repositories>

  <dependencies>
    ...
    <dependency>
      <groupId>dev.daniellavoie.ksqldb</groupId>
      <artifactId>ksqldb-java-client</artifactId>
      <version>0.2.0-SNAPSHOT</version>
    </dependency>
  </dependencies>
```

### Consuming a Push Query

```java
  KsqlDBClient.create("http://localhost:8088")
  
    .pushQuery(new QueryRequest("SELECT * FROM MY_TABLE WHERE ROWKEY='1' EMIT CHANGES;"))

    .doOnNext(queryRow -> System.out.println("Received a new row : " + queryRow + "."))

    .doOnError(throwable -> throwable.printStackTrace())

    .subscribe();
```

### Consuming a Pull Query

```java
  KsqlDBClient.create("http://localhost:8088")
    
    .pullQuery(new QueryRequest("SELECT * FROM MY_TABLE WHERE ROWKEY='1';"))

    .doOnNext(queryRow -> System.out.println("Received a new row : " + queryRow + "."))

    .doOnError(throwable -> throwable.printStackTrace())

    .doOnComplete(() -> System.out.println("Request completed.")))
    
    .subscribe();
```

## Working with the reactive API

This Java Client relies heavily on [Project Reactor](https://projectreactor.io/) to offer a reactive API for all operations related to ksqlDB.

The operators from Reactor are similar to the ones available within Kafka Streams. 

A reactive hands on workshop is available online [here](https://tech.io/playgrounds/929/reactive-programming-with-reactor-3/Intro) and is a good primer to learn reactive programming with Java.

## Supported API

This client library supports all APIs offered by ksqlDB. More documentation will be provided in a near future. The `KsqlDBClient` class offers methods to interract with all REST Endpoints of ksqlDB documented [here](https://docs.ksqldb.io/en/latest/developer-guide/api/).

## Upcoming improvement

* Support Object binding with a higher level API.

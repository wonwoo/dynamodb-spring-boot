/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.wonwoo.dynamodb.autoconfigure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;

/**
 * @author wonwoo
 */
public class DynamoDbCreateTable implements CreateTable {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private Long readCapacityUnits = 10L;
  private Long writeCapacityUnits = 10L;

  private final AmazonDynamoDB amazonDynamoDB;

  public DynamoDbCreateTable(AmazonDynamoDB amazonDynamoDB) {
    this.amazonDynamoDB = amazonDynamoDB;
  }

  @Override
  public boolean isTable(String tableName) {
    ListTablesResult tables = amazonDynamoDB.listTables();
    List<String> tableNames = tables.getTableNames();
    return tableNames.size() != 0 && tableNames.stream().anyMatch(s -> s.equals(tableName));
  }

  @Override
  public CreateTableResult createTable(String tableName, String hashKeyName) {
    List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
    attributeDefinitions.add(new AttributeDefinition(hashKeyName, ScalarAttributeType.S));
    List<KeySchemaElement> ks = new ArrayList<>();
    ks.add(new KeySchemaElement(hashKeyName, KeyType.HASH));
    ProvisionedThroughput provisionedthroughput =
        new ProvisionedThroughput(this.readCapacityUnits, this.writeCapacityUnits);
    CreateTableRequest request =
        new CreateTableRequest()
            .withTableName(tableName)
            .withAttributeDefinitions(attributeDefinitions)
            .withKeySchema(ks)
            .withProvisionedThroughput(provisionedthroughput);

    return amazonDynamoDB.createTable(request);
  }

  private TableStatus tableStatus(String tableName) {
    DescribeTableRequest request = new DescribeTableRequest();
    request.setTableName(tableName);
    try {
      DescribeTableResult result = amazonDynamoDB.describeTable(request);
      TableStatus tableStatus = TableStatus.fromValue(result.getTable().getTableStatus());
      logger.debug("table status {} ", tableStatus);
      return tableStatus;
    } catch (ResourceNotFoundException e) {
      logger.debug("ResourceNotFound is TableName {}", tableName);
      return null;
    } catch (AmazonClientException e) {
      logger.error("Unknown error ", e);
      throw new IllegalStateException("Unknown Exception", e);
    }
  }

  @Override
  public boolean waitTableExists(String tableName, long secondsPolls, long timeout) {
    long sleepTime = TimeUnit.SECONDS.toMillis(timeout);

    while (!leaseTableExists(tableName)) {
      if (sleepTime <= 0) {
        return false;
      }
      long timeToSleepMillis = Math.min(TimeUnit.SECONDS.toMillis(secondsPolls), sleepTime);
      sleepTime -= sleep(timeToSleepMillis);
    }

    return true;
  }

  private boolean leaseTableExists(String tableName) {
    return TableStatus.ACTIVE == tableStatus(tableName);
  }

  private long sleep(long timeToSleepMillis) {
    long startTime = System.currentTimeMillis();

    try {
      Thread.sleep(timeToSleepMillis);
    } catch (InterruptedException e) {
      logger.info("Interrupted while sleeping");
    }

    return System.currentTimeMillis() - startTime;
  }

  public void setReadCapacityUnits(Long readCapacityUnits) {
    this.readCapacityUnits = readCapacityUnits;
  }

  public void setWriteCapacityUnits(Long writeCapacityUnits) {
    this.writeCapacityUnits = writeCapacityUnits;
  }

}

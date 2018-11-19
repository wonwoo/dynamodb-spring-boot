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

import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

/**
 * @author wonwoo
 */
public class DynamoDbCreateTable implements CreateTable {

  private Long readCapacityUnits = 10L;
  private Long writeCapacityUnits = 10L;

  private final AmazonDynamoDB amazonDynamoDB;

  private final DynamoDBMapper dynamoDBMapper;

  public DynamoDbCreateTable(final AmazonDynamoDB amazonDynamoDB) {
    this.amazonDynamoDB = amazonDynamoDB;
    this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
  }

  @Override
  public boolean isTable(final String tableName) {
    final ListTablesResult tables = amazonDynamoDB.listTables();
    final List<String> tableNames = tables.getTableNames();
    return tableNames.stream().anyMatch(s -> s.equals(tableName));
  }

  @Override
  public CreateTableResult createTable(final Class<?> type) {
    final ProvisionedThroughput provisionedthroughput =
      new ProvisionedThroughput(this.readCapacityUnits, this.writeCapacityUnits);
    final CreateTableRequest request = dynamoDBMapper.generateCreateTableRequest(type).withProvisionedThroughput(provisionedthroughput);
    if (request.getGlobalSecondaryIndexes() != null) {
      // have to set provisioned throughput of gsi as well
      for (final GlobalSecondaryIndex gsi : request.getGlobalSecondaryIndexes()) {
        gsi.setProvisionedThroughput(provisionedthroughput);
      }
    }
    return amazonDynamoDB.createTable(request);
  }

  @Override
  public void waitTableActive(final String tableName, final int timeout, final int interval) {
    try {
        TableUtils.waitUntilActive(amazonDynamoDB, tableName, timeout, interval);
    } catch (final InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
    }
  }

  public void setReadCapacityUnits(final Long readCapacityUnits) {
    this.readCapacityUnits = readCapacityUnits;
  }

  public void setWriteCapacityUnits(final Long writeCapacityUnits) {
    this.writeCapacityUnits = writeCapacityUnits;
  }

}

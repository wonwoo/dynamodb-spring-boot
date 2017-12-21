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

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * @author wonwoo
 */
@RunWith(MockitoJUnitRunner.class)
public class DynamoDbCreateTableTests {

  @Mock
  private AmazonDynamoDB amazonDynamoDB;

  private DynamoDbCreateTable dynamoDbCreateTable;

  @Before
  public void setup() {
    this.dynamoDbCreateTable = new DynamoDbCreateTable(this.amazonDynamoDB);
  }


  @Test
  public void isFoundTable() {
    ListTablesResult listTablesResult = new ListTablesResult();
    listTablesResult.setTableNames(Collections.singleton("persons"));
    given(amazonDynamoDB.listTables()).willReturn(listTablesResult);
    assertThat(dynamoDbCreateTable.isTable("persons")).isTrue();
  }

  @Test
  public void isNotFoundTable() {
    ListTablesResult listTablesResult = new ListTablesResult();
    listTablesResult.setTableNames(Collections.singleton("persons"));
    given(amazonDynamoDB.listTables()).willReturn(listTablesResult);
    assertThat(dynamoDbCreateTable.isTable("foo")).isFalse();
  }

  @Test
  public void createTable() {
    CreateTableResult createTableResult = new CreateTableResult();
    TableDescription tableDescription = new TableDescription();
    tableDescription.setTableStatus(TableStatus.CREATING);
    createTableResult.setTableDescription(tableDescription);
    given(amazonDynamoDB.createTable(any(CreateTableRequest.class)))
        .willReturn(createTableResult);
    CreateTableResult table =
        dynamoDbCreateTable.createTable("test", "id");
    assertThat(table.getTableDescription().getTableStatus()).isEqualTo("CREATING");


  }

  @Test
  public void waitTableExistsTrueTest() {
    given(this.amazonDynamoDB.describeTable(any(DescribeTableRequest.class)))
        .willAnswer(invocation -> describeTableResult(TableStatus.CREATING))
        .willAnswer(invocation -> describeTableResult(TableStatus.CREATING))
        .willReturn(describeTableResult(TableStatus.ACTIVE));
    boolean exists = dynamoDbCreateTable.waitTableExists("test", 5, 30);
    assertThat(exists).isTrue();
  }


  @Test
  public void waitTableResourceNotFoundExceptionTest() {
    given(this.amazonDynamoDB.describeTable(any(DescribeTableRequest.class)))
        .willThrow(new ResourceNotFoundException("table Not Found"))
        .willReturn(describeTableResult(TableStatus.ACTIVE));
    boolean exists = dynamoDbCreateTable.waitTableExists("test", 5, 10);
    assertThat(exists).isTrue();
  }


  @Test(expected = IllegalStateException.class)
  public void waitTableIllegalStateExceptionTest() {
    given(this.amazonDynamoDB.describeTable(any(DescribeTableRequest.class)))
        .willAnswer(invocation -> describeTableResult(TableStatus.CREATING))
        .willThrow(new AmazonClientException("error"));
    dynamoDbCreateTable.waitTableExists("test", 5, 10);
  }

  @Test
  public void waitTableExistsFalseTest() {
    given(this.amazonDynamoDB.describeTable(any(DescribeTableRequest.class)))
        .willAnswer(invocation -> describeTableResult(TableStatus.CREATING))
        .willAnswer(invocation -> describeTableResult(TableStatus.CREATING))
        .willAnswer(invocation -> describeTableResult(TableStatus.CREATING))
        .willReturn(describeTableResult(TableStatus.ACTIVE));
    boolean exists = dynamoDbCreateTable.waitTableExists("test", 5, 10);
    assertThat(exists).isFalse();
  }

  private DescribeTableResult describeTableResult(TableStatus tableStatus) {
    DescribeTableResult describeTableResult = new DescribeTableResult();
    TableDescription tableDescription = new TableDescription();
    tableDescription.setTableStatus(tableStatus);
    describeTableResult.setTable(tableDescription);
    return describeTableResult;
  }
}
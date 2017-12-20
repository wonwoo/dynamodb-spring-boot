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

package com.github.wonwoo.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.TableStatus;
import com.github.wonwoo.dynamodb.autoconfigure.CreateTable;
import com.github.wonwoo.dynamodb.autoconfigure.DynamoDbMapping;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBPersistentEntityImpl;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBPersistentProperty;
import org.springframework.data.util.TypeInformation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * @author wonwoo
 */
@RunWith(MockitoJUnitRunner.class)
public class DynamoDbMappingTests {

  private DynamoDBMappingContext context;

  @Mock
  private CreateTable createTable;


  private DynamoDbMapping dynamoDbMapping;

  @Before
  public void setup() {
    context = new DynamoDBMappingContext();
    HashSet<Class<?>> initialEntitySet = new HashSet<>();
    initialEntitySet.add(Person.class);
    context.setInitialEntitySet(initialEntitySet);
    context.afterPropertiesSet();
    this.dynamoDbMapping = new DynamoDbMapping(createTable, context);
    this.dynamoDbMapping.setSecondsBetweenPolls(10);
    this.dynamoDbMapping.setTimeoutSeconds(30);
  }

  @Test
  public void getPersistentEntities() {
    Collection<DynamoDBPersistentEntityImpl<?>> entities = this.dynamoDbMapping.getPersistentEntities();
    assertThat(entities).hasSize(1);
  }

  @Test
  public void getPersistentEntity() {
    DynamoDBPersistentEntityImpl<?> entity = this.dynamoDbMapping.getPersistentEntity(Person.class);
    DynamoDBPersistentProperty idProperty = entity.getIdProperty();
    assertThat(idProperty.getActualType()).isEqualTo(String.class);
  }

  @Test
  public void getIdProperty() {
    DynamoDBPersistentProperty idProperty = this.dynamoDbMapping.getIdProperty(Person.class);
    assertThat(idProperty.getActualType()).isEqualTo(String.class);
  }

  @Test
  public void getTypeInformation() {
    TypeInformation<?> typeInformation = this.dynamoDbMapping.getTypeInformation(Person.class);
    assertThat(typeInformation.getType()).isEqualTo(Person.class);
  }

  @Test
  public void createTable() {
    CreateTableResult value = new CreateTableResult();
    TableDescription tableDescription = new TableDescription();
    tableDescription.setItemCount(100L);
    tableDescription.setTableStatus(TableStatus.CREATING);
    value.setTableDescription(tableDescription);
    ListTablesResult listTablesResult = new ListTablesResult();
    listTablesResult.setTableNames(Arrays.asList("foo", "bar"));

    given(createTable.isTable(any())).willReturn(false);
    given(createTable.createTable(any(), any())).willReturn(value);
    given(createTable.waitTableExists(any(), anyLong(), anyLong())).willReturn(true);
    List<CreateTableResult> table = this.dynamoDbMapping.createTable();
    assertThat(table).hasSize(1);
    assertThat(table.iterator().next().getTableDescription()).isEqualTo(tableDescription);
    verify(createTable).waitTableExists("persons", 10, 30);
  }

  @DynamoDBTable(tableName = "persons")
  private static class Person {
    @DynamoDBHashKey
    private String id;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }
  }

}
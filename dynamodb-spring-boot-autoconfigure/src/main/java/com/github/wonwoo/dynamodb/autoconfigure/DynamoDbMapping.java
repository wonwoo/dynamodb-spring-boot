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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBPersistentEntity;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBPersistentEntityImpl;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBPersistentProperty;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.util.TypeInformation;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.*;

/**
 * @author wonwoo
 */
public class DynamoDbMapping {

  private final AmazonDynamoDB amazonDynamoDB;
  private final DynamoDBMappingContext context;
  private Long readCapacityUnits = 10L;
  private Long writeCapacityUnits = 10L;

  public DynamoDbMapping(AmazonDynamoDB amazonDynamoDB, DynamoDBMappingContext context) {
    this.amazonDynamoDB = amazonDynamoDB;
    this.context = context;
  }

  public Collection<DynamoDBPersistentEntityImpl<?>> getPersistentEntities() {
    return context.getPersistentEntities();
  }

  public DynamoDBPersistentEntityImpl<?> getPersistentEntity(Class<?> type) {
    return context.getPersistentEntity(type);
  }

  public DynamoDBPersistentProperty getIdProperty(Class<?> type) {
    return context.getPersistentEntity(type).getIdProperty();
  }

  public TypeInformation<?> getTypeInformation(Class<?> type) {
    return context.getPersistentEntity(type).getTypeInformation();
  }


  public List<CreateTableResult> createTable() {
    List<CreateTableResult> results = new ArrayList<>();
    for (DynamoDBPersistentEntity<?> entity : context.getPersistentEntities()) {
      DynamoDBPersistentProperty idProperty = entity.getIdProperty();
      DynamoDBTable table = findMergedAnnotation(entity.getTypeInformation().getType(), DynamoDBTable.class);
      if (!isTable(table.tableName())) {
        results.add(createTable(table.tableName(), idProperty.getName()));
      }
    }
    return results;
  }

  private boolean isTable(String tableName) {
    ListTablesResult tables = amazonDynamoDB.listTables();
    List<String> tableNames = tables.getTableNames();
    return tableNames.size() != 0 && tableNames.stream().anyMatch(s -> s.equals(tableName));
  }

  private CreateTableResult createTable(String tableName, String hashKeyName) {
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

  private <A extends Annotation> A findMergedAnnotation(AnnotatedElement element, Class<A> annotationType) {
    return AnnotatedElementUtils.findMergedAnnotation(element, annotationType);
  }

  public void setReadCapacityUnits(Long readCapacityUnits) {
    this.readCapacityUnits = readCapacityUnits;
  }

  public void setWriteCapacityUnits(Long writeCapacityUnits) {
    this.writeCapacityUnits = writeCapacityUnits;
  }
}

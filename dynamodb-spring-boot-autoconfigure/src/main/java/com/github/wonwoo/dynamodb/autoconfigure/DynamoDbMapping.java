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

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;

/**
 * @author wonwoo
 */
public class DynamoDbMapping {

  private final CreateTable createTable;
  private final DynamoDBMappingContext context;

  private static final int DEFAULT_SECONDS_BETWEEN_POLLS = 5;
  private static final int DEFAULT_TIMEOUT_SECONDS = 30;

  private int secondsBetweenPolls = DEFAULT_SECONDS_BETWEEN_POLLS;
  private int timeoutSeconds = DEFAULT_TIMEOUT_SECONDS;

  public DynamoDbMapping(final CreateTable createTable, final DynamoDBMappingContext context) {
    this.createTable = createTable;
    this.context = context;
  }

  public Collection<DynamoDBPersistentEntityImpl<?>> getPersistentEntities() {
    return context.getPersistentEntities();
  }

  public DynamoDBPersistentEntityImpl<?> getPersistentEntity(final Class<?> type) {
    final DynamoDBPersistentEntityImpl<?> persistentEntity = context.getPersistentEntity(type);
    if(persistentEntity == null) {
      throw new NullPointerException("persistentEntity is null");
    }
    return persistentEntity;
  }

  public DynamoDBPersistentProperty getIdProperty(final Class<?> type) {
    return getPersistentEntity(type).getIdProperty();
  }

  public TypeInformation<?> getTypeInformation(final Class<?> type) {
    return getPersistentEntity(type).getTypeInformation();
  }

  public List<CreateTableResult> createTable() {
    final List<CreateTableResult> results = new ArrayList<>();
    for (final DynamoDBPersistentEntity<?> entity : context.getPersistentEntities()) {
      final DynamoDBPersistentProperty idProperty = entity.getIdProperty();
      if(idProperty == null) {
        throw new NullPointerException("entity property Id is null");
      }
      final DynamoDBTable table = findMergedAnnotation(entity.getTypeInformation().getType(), DynamoDBTable.class);
      if (!createTable.isTable(table.tableName())) {
        final CreateTableResult createTableTable = createTable.createTable(entity.getTypeInformation().getType());
        createTable.waitTableActive(table.tableName(), this.timeoutSeconds, this.secondsBetweenPolls);
        results.add(createTableTable);
      }
    }
    return results;
  }

  private <A extends Annotation> A findMergedAnnotation(final AnnotatedElement element, final Class<A> annotationType) {
    return AnnotatedElementUtils.findMergedAnnotation(element, annotationType);
  }

  public void setSecondsBetweenPolls(final int secondsBetweenPolls) {
    this.secondsBetweenPolls = secondsBetweenPolls;
  }

  public void setTimeoutSeconds(final int timeoutSeconds) {
    this.timeoutSeconds = timeoutSeconds;
  }
}

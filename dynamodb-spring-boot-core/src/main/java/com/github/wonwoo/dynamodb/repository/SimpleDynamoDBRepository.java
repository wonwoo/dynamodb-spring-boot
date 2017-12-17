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

package com.github.wonwoo.dynamodb.repository;

import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.socialsignin.spring.data.dynamodb.repository.support.DynamoDBEntityInformation;
import org.socialsignin.spring.data.dynamodb.repository.support.EnableScanPermissions;
import org.socialsignin.spring.data.dynamodb.repository.support.SimpleDynamoDBPagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author wonwoo
 */
public class SimpleDynamoDBRepository<T, ID extends Serializable>
    extends SimpleDynamoDBPagingAndSortingRepository<T, ID>
    implements DynamoDBRepository<T, ID> {

  public SimpleDynamoDBRepository(
      DynamoDBEntityInformation<T, ID> entityInformation,
      DynamoDBOperations dynamoDBOperations,
      EnableScanPermissions enableScanPermissions) {
    super(entityInformation, dynamoDBOperations, enableScanPermissions);
  }

  @Override
  public <S extends T> List<S> save(Iterable<S> entities) {
    List<S> result = StreamSupport.stream(entities.spliterator(), false).collect(Collectors.toList());
    dynamoDBOperations.batchSave(entities);
    return result;
  }
}
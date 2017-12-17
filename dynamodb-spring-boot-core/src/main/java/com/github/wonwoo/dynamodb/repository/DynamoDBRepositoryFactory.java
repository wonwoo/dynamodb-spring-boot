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
import org.springframework.data.repository.core.RepositoryMetadata;

import java.io.Serializable;

/**
 * @author wonwoo
 */
public class DynamoDBRepositoryFactory extends org.socialsignin.spring.data.dynamodb.repository.support.DynamoDBRepositoryFactory {

  private final DynamoDBOperations dynamoDBOperations;

  public DynamoDBRepositoryFactory(DynamoDBOperations dynamoDBOperations) {
    super(dynamoDBOperations);
    this.dynamoDBOperations = dynamoDBOperations;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  protected <T, ID extends Serializable> DynamoDBRepository<?, ?> getDynamoDBRepository(
      RepositoryMetadata metadata) {
    return new SimpleDynamoDBRepository(getEntityInformation(metadata.getDomainType()),
        dynamoDBOperations, getEnableScanPermissions(metadata));
  }
}
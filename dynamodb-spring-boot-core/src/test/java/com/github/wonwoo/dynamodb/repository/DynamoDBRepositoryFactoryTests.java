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

import org.junit.Test;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author wonwoo
 */
public class DynamoDBRepositoryFactoryTests {

  @Test
  public void getDynamoDBRepository() {
    DynamoDBOperations openOperations = mock(DynamoDBOperations.class);
    DynamoDBRepositoryFactory factory = new DynamoDBRepositoryFactory(openOperations);
    RepositoryMetadata repositoryMetadata = new DefaultRepositoryMetadata(PersonRepository.class);
    DynamoDBRepository<?, ?> dynamoDBRepository = factory.getDynamoDBRepository(repositoryMetadata);
    assertThat(dynamoDBRepository).isNotNull();
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNotFoundDynamoDBRepository() {
    new DefaultRepositoryMetadata(EmptyRepository.class);
  }
}
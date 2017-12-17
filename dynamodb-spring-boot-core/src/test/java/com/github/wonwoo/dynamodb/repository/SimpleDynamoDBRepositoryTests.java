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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

/**
 * @author wonwoo
 */
public class SimpleDynamoDBRepositoryTests {

  @Test
  @SuppressWarnings("unchecked")
  public void saveTest() {
    DynamoDBOperations openOperations = mock(DynamoDBOperations.class);
    doNothing().when(openOperations).batchSave(anyList());
    DynamoDBRepositoryFactory factory = new DynamoDBRepositoryFactory(openOperations);
    RepositoryMetadata repositoryMetadata = new DefaultRepositoryMetadata(PersonRepository.class);
    DynamoDBRepository<Person, String> dynamoDBRepository =
        (DynamoDBRepository<Person, String>) factory.getDynamoDBRepository(repositoryMetadata);
    List<Person> person = dynamoDBRepository.saveAll(Arrays.asList(new Person("id1"), new Person("id2")));
    assertThat(person.iterator().next()).isEqualTo(new Person("id1"));
  }

}
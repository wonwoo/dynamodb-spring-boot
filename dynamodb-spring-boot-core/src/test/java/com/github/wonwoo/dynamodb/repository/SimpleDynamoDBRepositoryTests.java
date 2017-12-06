package com.github.wonwoo.dynamodb.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class SimpleDynamoDBRepositoryTests {

  @Test
  @SuppressWarnings("unchecked")
  public void saveTest() {
    DynamoDBOperations openOperations = mock(DynamoDBOperations.class);
    doNothing().when(openOperations).batchSave(anyListOf(Person.class));
    DynamoDBRepositoryFactory factory = new DynamoDBRepositoryFactory(openOperations);
    RepositoryMetadata repositoryMetadata = new DefaultRepositoryMetadata(PersonRepository.class);
    DynamoDBRepository<Person, String> dynamoDBRepository =
        (DynamoDBRepository<Person, String>) factory.getDynamoDBRepository(repositoryMetadata);
    List<Person> person = dynamoDBRepository.save(Arrays.asList(new Person("id1"), new Person("id2")));
    assertThat(person.iterator().next()).isEqualTo(new Person("id1"));
  }

}
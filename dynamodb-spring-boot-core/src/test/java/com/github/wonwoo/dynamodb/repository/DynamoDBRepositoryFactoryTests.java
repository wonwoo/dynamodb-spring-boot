package com.github.wonwoo.dynamodb.repository;

import org.junit.Test;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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
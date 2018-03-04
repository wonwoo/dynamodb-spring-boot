package com.github.wonwoo.dynamodb.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.socialsignin.spring.data.dynamodb.exception.BatchWriteException;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SimpleDynamoDBRepositoryTests {

  @Rule
  public final ExpectedException exception = ExpectedException.none();

  @Test
  @SuppressWarnings("unchecked")
  public void saveAllTest() {
    DynamoDBOperations openOperations = mock(DynamoDBOperations.class);
    given(openOperations.batchSave(anyList())).willReturn(Collections.emptyList());
    DynamoDBRepositoryFactory factory = new DynamoDBRepositoryFactory(openOperations);
    RepositoryMetadata repositoryMetadata = new DefaultRepositoryMetadata(PersonRepository.class);
    DynamoDBRepository<Person, String> dynamoDBRepository =
        (DynamoDBRepository<Person, String>) factory.getDynamoDBRepository(repositoryMetadata);
    List<Person> person = dynamoDBRepository.saveAll(Arrays.asList(new Person("id1"), new Person("id2")));
    Iterator<Person> iterator = person.iterator();
    assertThat(iterator.next()).isEqualTo(new Person("id1"));
    assertThat(iterator.next()).isEqualTo(new Person("id2"));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void saveAllFailedTest() {
    exception.expect(BatchWriteException.class);
    DynamoDBOperations openOperations = mock(DynamoDBOperations.class);
    DynamoDBMapper.FailedBatch failedBatch = new DynamoDBMapper.FailedBatch();
    failedBatch.setException(new NullPointerException());
    given(openOperations.batchSave(anyList())).willReturn(Collections.singletonList(failedBatch));
    DynamoDBRepositoryFactory factory = new DynamoDBRepositoryFactory(openOperations);
    RepositoryMetadata repositoryMetadata = new DefaultRepositoryMetadata(PersonRepository.class);
    DynamoDBRepository<Person, String> dynamoDBRepository =
        (DynamoDBRepository<Person, String>) factory.getDynamoDBRepository(repositoryMetadata);
    dynamoDBRepository.saveAll(Arrays.asList(new Person("id1"), new Person("id2")));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void saveAllIllegalArgumentExceptionTest() {
    exception.expect(IllegalArgumentException.class);
    DynamoDBOperations openOperations = mock(DynamoDBOperations.class);
    DynamoDBRepositoryFactory factory = new DynamoDBRepositoryFactory(openOperations);
    RepositoryMetadata repositoryMetadata = new DefaultRepositoryMetadata(PersonRepository.class);
    DynamoDBRepository<Person, String> dynamoDBRepository =
        (DynamoDBRepository<Person, String>) factory.getDynamoDBRepository(repositoryMetadata);
    dynamoDBRepository.saveAll(null);
  }

}

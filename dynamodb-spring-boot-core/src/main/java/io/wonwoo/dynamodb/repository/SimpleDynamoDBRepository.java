package io.wonwoo.dynamodb.repository;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.socialsignin.spring.data.dynamodb.repository.support.DynamoDBEntityInformation;
import org.socialsignin.spring.data.dynamodb.repository.support.EnableScanPermissions;
import org.socialsignin.spring.data.dynamodb.repository.support.SimpleDynamoDBPagingAndSortingRepository;

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
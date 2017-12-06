package com.github.wonwoo.dynamodb.repository;

import java.io.Serializable;

import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.springframework.data.repository.core.RepositoryMetadata;

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
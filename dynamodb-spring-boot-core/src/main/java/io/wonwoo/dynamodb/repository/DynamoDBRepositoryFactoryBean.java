package io.wonwoo.dynamodb.repository;

import java.io.Serializable;

import org.socialsignin.spring.data.dynamodb.core.DynamoDBOperations;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

public class DynamoDBRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
    extends RepositoryFactoryBeanSupport<T, S, ID> {

  private DynamoDBMapperConfig dynamoDBMapperConfig;

  private AmazonDynamoDB amazonDynamoDB;

  private DynamoDBOperations dynamoDBOperations;

  public DynamoDBRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
    super(repositoryInterface);
  }

  public void setAmazonDynamoDB(AmazonDynamoDB amazonDynamoDB) {
    this.amazonDynamoDB = amazonDynamoDB;
    setMappingContext(new DynamoDBMappingContext());
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory() {
    if (dynamoDBOperations == null) {
      dynamoDBOperations = new DynamoDBTemplate(amazonDynamoDB, dynamoDBMapperConfig);
    }
    return new DynamoDBRepositoryFactory(dynamoDBOperations);
  }

  public void setDynamoDBMapperConfig(DynamoDBMapperConfig dynamoDBMapperConfig) {
    this.dynamoDBMapperConfig = dynamoDBMapperConfig;
  }

  public void setDynamoDBOperations(DynamoDBOperations dynamoDBOperations) {
    this.dynamoDBOperations = dynamoDBOperations;
    setMappingContext(new DynamoDBMappingContext());

  }
}
package io.wonwoo.dynamodb;

import org.socialsignin.spring.data.dynamodb.repository.config.DynamoDBRepositoryConfigExtension;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

class DynamoRepositoriesAutoConfigureRegistrar extends AbstractRepositoryConfigurationSourceSupport {

  @Override
  protected Class<? extends Annotation> getAnnotation() {
    return EnableDynamoDBRepositories.class;
  }

  @Override
  protected Class<?> getConfiguration() {
    return EnableDynamoRepositoriesConfiguration.class;
  }

  @Override
  protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
    return new DynamoDBRepositoryConfigExtension();
  }


  @EnableDynamoDBRepositories
  private static class EnableDynamoRepositoriesConfiguration {

  }
}
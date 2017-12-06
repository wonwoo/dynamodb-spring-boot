package com.github.wonwoo.dynamodb;

import java.lang.annotation.Annotation;

import org.socialsignin.spring.data.dynamodb.repository.config.DynamoDBRepositoryConfigExtension;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import com.github.wonwoo.dynamodb.repository.DynamoDBRepositoryFactoryBean;

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

  @EnableDynamoDBRepositories(repositoryFactoryBeanClass = DynamoDBRepositoryFactoryBean.class)
  private static class EnableDynamoRepositoriesConfiguration {

  }
}
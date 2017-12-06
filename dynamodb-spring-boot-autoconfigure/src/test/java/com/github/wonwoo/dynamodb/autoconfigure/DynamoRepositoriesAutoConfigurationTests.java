package com.github.wonwoo.dynamodb.autoconfigure;

import com.github.wonwoo.dynamodb.DynamoAutoConfiguration;
import com.github.wonwoo.dynamodb.DynamoDataAutoConfiguration;
import com.github.wonwoo.dynamodb.DynamoRepositoriesAutoConfiguration;
import com.github.wonwoo.dynamodb.autoconfigure.person.Person;
import com.github.wonwoo.dynamodb.autoconfigure.person.PersonRepository;
import com.github.wonwoo.dynamodb.empty.EmptyDataPackage;
import com.github.wonwoo.dynamodb.TestAutoConfigurationPackage;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;


import static org.assertj.core.api.Assertions.assertThat;

public class DynamoRepositoriesAutoConfigurationTests {

  private AnnotationConfigApplicationContext context;

  @After
  public void close() {
    if (this.context != null) {
      this.context.close();
    }
  }

  @Test
  public void defaultRepository() throws Exception {
    load(DefaultConfiguration.class,
        "spring.data.dynamodb.access-key=test", "spring.data.dynamodb.secret-key=test");
    Assertions.assertThat(this.context.getBeansOfType(PersonRepository.class)).hasSize(1);
  }

  @Test
  public void disabledRepositories() {
    load(DefaultConfiguration.class,
        "spring.data.dynamodb.access-key=test", "spring.data.dynamodb.secret-key=test",
        "spring.data.dynamodb.repositories.enabled=none");
    assertThat(this.context.getBeansOfType(PersonRepository.class)).hasSize(0);
  }

  @Test
  public void noRepositoryAvailable() throws Exception {
    load(NoRepositoryConfiguration.class,
        "spring.data.dynamodb.access-key=test", "spring.data.dynamodb.secret-key=test");
    assertThat(this.context.getBeansOfType(PersonRepository.class)).hasSize(0);
  }

  @Configuration
  @TestAutoConfigurationPackage(Person.class)
  static class DefaultConfiguration {

  }

  @Configuration
  @TestAutoConfigurationPackage(Person.class)
  static class DynamoNotAvailableConfiguration {

  }


  @Configuration
  @TestAutoConfigurationPackage(EmptyDataPackage.class)
  static class NoRepositoryConfiguration {

  }

  private void load(Class<?> config, String... environment) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    EnvironmentTestUtils.addEnvironment(context, environment);
    if (config != null) {
      context.register(config);
    }
    context.register(PropertyPlaceholderAutoConfiguration.class,
        DynamoAutoConfiguration.class, DynamoDataAutoConfiguration.class,
        DynamoRepositoriesAutoConfiguration.class);
    context.refresh();
    this.context = context;
  }
}
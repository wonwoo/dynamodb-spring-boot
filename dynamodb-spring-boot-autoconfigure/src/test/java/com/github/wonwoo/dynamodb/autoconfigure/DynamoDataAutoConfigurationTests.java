package com.github.wonwoo.dynamodb.autoconfigure;

import java.util.Set;

import org.junit.After;
import org.junit.Test;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.util.ReflectionTestUtils;

import com.github.wonwoo.dynamodb.DynamoAutoConfiguration;
import com.github.wonwoo.dynamodb.DynamoDataAutoConfiguration;
import com.github.wonwoo.dynamodb.autoconfigure.person.Person;

import static org.assertj.core.api.Assertions.assertThat;

public class DynamoDataAutoConfigurationTests {

  private AnnotationConfigApplicationContext context;

  @After
  public void close() {
    if (this.context != null) {
      this.context.close();
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void entitySetInitialEntity() throws Exception {
    load(EntityScanConfig.class,
        "spring.data.dynamodb.access-key=test", "spring.data.dynamodb.secret-key=test");
    DynamoDBMappingContext mappingContext = this.context
        .getBean(DynamoDBMappingContext.class);
    Set<Class<?>> initialEntitySet = (Set<Class<?>>) ReflectionTestUtils
        .getField(mappingContext, "initialEntitySet");
    assertThat(initialEntitySet).containsOnly(Person.class);
  }


  private void load(Class<?> config, String... environment) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    EnvironmentTestUtils.addEnvironment(context, environment);
    if (config != null) {
      context.register(config);
    }
    context.register(PropertyPlaceholderAutoConfiguration.class,
        ValidationAutoConfiguration.class, DynamoAutoConfiguration.class,
        DynamoDataAutoConfiguration.class);
    context.refresh();
    this.context = context;
  }

  @Configuration
  @EntityScan("com.github.wonwoo.dynamodb.autoconfigure.person")
  static class EntityScanConfig {

  }

}
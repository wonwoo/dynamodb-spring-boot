package io.wonwoo.dynamodb.autoconfigure;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import io.wonwoo.dynamodb.DynamoAutoConfiguration;
import io.wonwoo.dynamodb.DynamoDataAutoConfiguration;
import io.wonwoo.dynamodb.DynamoRepositoriesAutoConfiguration;
import io.wonwoo.dynamodb.autoconfigure.person.PersonRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class DynamoAutoConfigurationTests {

  private AnnotationConfigApplicationContext context;

  @After
  public void close() {
    if (this.context != null) {
      this.context.close();
    }
  }

  @Test
  public void notFoundDynamoDbAutoConfig() {
    load(DynamoAutoConfiguration.class);
    assertThat(this.context.getBeansOfType(AWSCredentialsProvider.class)).hasSize(0);
    assertThat(this.context.getBeansOfType(AmazonDynamoDB.class)).hasSize(0);
  }

  @Test
  public void dynamoDbAutoConfig() {
    load(DynamoAutoConfiguration.class,
        "spring.data.dynamodb.access-key=test", "spring.data.dynamodb.secret-key=test");
    assertThat(this.context.getBeansOfType(AWSCredentialsProvider.class)).hasSize(1);
    assertThat(this.context.getBeansOfType(AmazonDynamoDB.class)).hasSize(1);
  }

  private void load(Class<?> config, String... environment) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    EnvironmentTestUtils.addEnvironment(context, environment);
    if (config != null) {
      context.register(config);
    }
    context.refresh();
    this.context = context;
  }
}
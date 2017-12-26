/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.wonwoo.dynamodb.autoconfigure;


import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author wonwoo
 */
public class DynamoAutoConfigurationTests {

  private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

  @Test
  public void notFoundDynamoDbAutoConfig() {
    contextRunner.withConfiguration(AutoConfigurations.of(DynamoAutoConfiguration.class))
        .run(context -> {
          assertThat(context).doesNotHaveBean(AWSCredentialsProvider.class);
          assertThat(context).doesNotHaveBean(AmazonDynamoDB.class);
        });
  }

  @Test
  public void dynamoDbAutoConfig() {
    contextRunner.withConfiguration(AutoConfigurations.of(DynamoAutoConfiguration.class))
        .withPropertyValues("spring.data.dynamodb.access-key=test-access-key", "spring.data.dynamodb.secret-key=test-secret-key",
            "spring.data.dynamodb.readCapacityUnits=100", "spring.data.dynamodb.writeCapacityUnits=200")
        .run(context -> {
          assertThat(context).hasSingleBean(AWSCredentialsProvider.class);
          assertThat(context).hasSingleBean(AmazonDynamoDB.class);
        });
  }


  @Test
  public void dynamoDbCamelCaseAutoConfig() {
    contextRunner.withConfiguration(AutoConfigurations.of(DynamoAutoConfiguration.class))
       .withPropertyValues("spring.data.dynamodb.accessKey=testAccessKey", "spring.data.dynamodb.secretKey=testSecretKey",
           "spring.data.dynamodb.readCapacityUnits=100", "spring.data.dynamodb.writeCapacityUnits=200")
       .run(context -> {
           assertThat(context).hasSingleBean(AWSCredentialsProvider.class);
           assertThat(context).hasSingleBean(AmazonDynamoDB.class);
       });
  }

  @Test
  public void dynamoDbUnderScoreAutoConfig() {
    contextRunner.withConfiguration(AutoConfigurations.of(DynamoAutoConfiguration.class))
      .withPropertyValues("spring.data.dynamodb.access_key=test_access_key", "spring.data.dynamodb.secret_key=test_secret_key",
         "spring.data.dynamodb.readCapacityUnits=100", "spring.data.dynamodb.writeCapacityUnits=200")
      .run(context -> {
          assertThat(context).hasSingleBean(AWSCredentialsProvider.class);
          assertThat(context).hasSingleBean(AmazonDynamoDB.class);
          });
    }
}
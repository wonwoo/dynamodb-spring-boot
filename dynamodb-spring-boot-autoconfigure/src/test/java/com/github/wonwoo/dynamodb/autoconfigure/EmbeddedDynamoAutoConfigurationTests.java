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

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.junit.Test;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author wonwoo
 */
public class EmbeddedDynamoAutoConfigurationTests {

  private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
      .withConfiguration(AutoConfigurations.of(EmbeddedDynamoAutoConfiguration.class));

  @Test
  public void dynamoLocalDbAutoConfig() {

    contextRunner.withUserConfiguration(DynamoDataAutoConfiguration.class)
        .run(context -> {
          assertThat(context).hasSingleBean(AmazonDynamoDB.class);
          assertThat(context).hasSingleBean(DynamoDbCreateTableBeanPostProcessor.class);
          assertThat(context).hasSingleBean(DynamoDBTemplate.class);
        });
  }

  @Test
  public void dynamoLocalDbNotFoundDataAutoConfig() {
    contextRunner
        .run(context -> {
          assertThat(context).hasSingleBean(AmazonDynamoDB.class);
          assertThat(context).hasSingleBean(DynamoDbCreateTableBeanPostProcessor.class);
          assertThat(context).doesNotHaveBean(DynamoDBTemplate.class);
        });
  }
}
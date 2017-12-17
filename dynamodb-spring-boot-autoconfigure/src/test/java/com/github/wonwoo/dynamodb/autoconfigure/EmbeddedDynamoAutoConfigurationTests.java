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
import org.junit.After;
import org.junit.Test;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author wonwoo
 */
public class EmbeddedDynamoAutoConfigurationTests {

  private AnnotationConfigApplicationContext context;

  @After
  public void close() {
    if (this.context != null) {
      this.context.close();
    }
  }

  @Test
  public void dynamoLocalDbAutoConfig() {
    load(DynamoDataAutoConfiguration.class);
    assertThat(this.context.getBeansOfType(AmazonDynamoDB.class)).hasSize(1);
    assertThat(this.context.getBeansOfType(DynamoDbCreateTableBeanPostProcessor.class)).hasSize(1);
    assertThat(this.context.getBeansOfType(DynamoDBTemplate.class)).hasSize(1);
  }

  @Test
  public void dynamoLocalDbNotFoundDataAutoConfig() {
    load(null);
    assertThat(this.context.getBeansOfType(AmazonDynamoDB.class)).hasSize(1);
    assertThat(this.context.getBeansOfType(DynamoDbCreateTableBeanPostProcessor.class)).hasSize(1);
    assertThat(this.context.getBeansOfType(DynamoDBTemplate.class)).hasSize(0);
  }

  private void load(Class<?> config, String... environment) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    EnvironmentTestUtils.addEnvironment(context, environment);
    if (config != null) {
      context.register(config);
    }
    context.register(EmbeddedDynamoAutoConfiguration.class);
    context.refresh();
    this.context = context;
  }
}
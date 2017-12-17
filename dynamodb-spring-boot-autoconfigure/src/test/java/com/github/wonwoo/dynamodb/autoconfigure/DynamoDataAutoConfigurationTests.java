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

import com.github.wonwoo.dynamodb.autoconfigure.person.Person;
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

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author wonwoo
 */
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
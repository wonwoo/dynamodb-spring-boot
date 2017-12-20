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

package com.github.wonwoo.dynamodb.test.autoconfigure;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.properties.PropertyMapping;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.BootstrapWith;

import java.lang.annotation.*;

/**
 * @author wonwoo
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(DynamoTypeExcludeFilter.class)
@AutoConfigureCache
@AutoConfigureDynamo
@ImportAutoConfiguration
public @interface DynamoTest {

  @PropertyMapping("spring.data.dynamodb.ddl.enabled")
  boolean createDdl() default true;

  boolean useDefaultFilters() default true;

  ComponentScan.Filter[] includeFilters() default {};

  ComponentScan.Filter[] excludeFilters() default {};

  @AliasFor(annotation = ImportAutoConfiguration.class, attribute = "exclude")
  Class<?>[] excludeAutoConfiguration() default {};
}

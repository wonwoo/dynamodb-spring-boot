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

import com.amazonaws.regions.Regions;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author wonwoo
 */
public class DynamoPropertiesTests {

  @Test
  public void defaultProperties() {
    DynamoProperties properties = new DynamoProperties();
    properties.setAccessKey("foo");
    properties.setSecretKey("bar");
    assertThat(properties.getAccessKey()).isEqualTo("foo");
    assertThat(properties.getSecretKey()).isEqualTo("bar");
    assertThat(properties.getRegions()).isEqualTo(Regions.AP_NORTHEAST_2);
    assertThat(properties.getWriteCapacityUnits()).isEqualTo(10L);
    assertThat(properties.getReadCapacityUnits()).isEqualTo(10L);
  }

  @Test
  public void configProperties() {
    DynamoProperties properties = new DynamoProperties();
    properties.setAccessKey("foo");
    properties.setSecretKey("bar");
    properties.setRegions(Regions.AP_NORTHEAST_1);
    properties.setReadCapacityUnits(200L);
    properties.setWriteCapacityUnits(500L);
    assertThat(properties.getAccessKey()).isEqualTo("foo");
    assertThat(properties.getSecretKey()).isEqualTo("bar");
    assertThat(properties.getRegions()).isEqualTo(Regions.AP_NORTHEAST_1);
    assertThat(properties.getReadCapacityUnits()).isEqualTo(200L);
    assertThat(properties.getWriteCapacityUnits()).isEqualTo(500L);
  }

  @Test
  public void hashTest() {
    Set<DynamoProperties> properties = new HashSet<>();
    DynamoProperties properties1 = new DynamoProperties();
    properties1.setAccessKey("foo");
    properties1.setSecretKey("bar");
    properties.add(properties1);
    DynamoProperties properties2 = new DynamoProperties();
    properties2.setAccessKey("foo");
    properties2.setSecretKey("bar");
    assertThat(properties.iterator().next()).isEqualTo(properties2);
  }

}
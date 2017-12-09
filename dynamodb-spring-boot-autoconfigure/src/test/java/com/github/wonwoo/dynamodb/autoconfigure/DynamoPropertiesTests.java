package com.github.wonwoo.dynamodb.autoconfigure;

import com.amazonaws.regions.Regions;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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
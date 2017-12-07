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

package com.github.wonwoo.dynamodb;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * @author wonwoo
 */
@Configuration
@ConditionalOnClass(AmazonDynamoDB.class)
@Conditional(DynamoAutoConfiguration.AwsDynamoCondition.class)
@EnableConfigurationProperties(DynamoProperties.class)
public class DynamoAutoConfiguration {

  private final DynamoProperties properties;

  public DynamoAutoConfiguration(DynamoProperties properties) {
    this.properties = properties;
  }

  @Bean
  @ConditionalOnMissingBean(name = "awsDynamoCredentialsProvider")
  public AWSCredentialsProvider awsDynamoCredentialsProvider() {
    return new AWSStaticCredentialsProvider(new BasicAWSCredentials(
        this.properties.getAccessKey(), this.properties.getSecretKey()));
  }

  @Bean
  @ConditionalOnMissingBean
  public AmazonDynamoDB amazonDynamoDB(AWSCredentialsProvider awsDynamoCredentialsProvider) {
    return AmazonDynamoDBClient
        .builder()
        .withCredentials(awsDynamoCredentialsProvider)
        .withRegion(properties.getRegions())
        .build();
  }

  protected static class AwsDynamoCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context,
                                            AnnotatedTypeMetadata metadata) {
      PropertyResolver resolver = new RelaxedPropertyResolver(
          context.getEnvironment(), "spring.data.dynamodb.");
      String accessKey = resolver.getProperty("accessKey");
      String secretKey = resolver.getProperty("secretKey");
      if (StringUtils.hasLength(accessKey) && StringUtils.hasLength(secretKey)) {
        return ConditionOutcome.match("found accessKey and secretKey property");
      }
      return ConditionOutcome.noMatch("not found accessKey and secretKey property");
    }
  }
}
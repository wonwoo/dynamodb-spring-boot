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
import com.github.wonwoo.dynamodb.repository.DynamoDBRepositoryFactoryBean;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.config.DynamoDBRepositoryConfigExtension;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wonwoo
 */
@Configuration
@ConditionalOnClass({AmazonDynamoDB.class, DynamoDBCrudRepository.class})
@ConditionalOnMissingBean({DynamoDBRepositoryFactoryBean.class,
    DynamoDBRepositoryConfigExtension.class})
@ConditionalOnProperty(prefix = "spring.data.dynamodb.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(DynamoRepositoriesAutoConfigureRegistrar.class)
@AutoConfigureAfter(DynamoDataAutoConfiguration.class)
public class DynamoRepositoriesAutoConfiguration {
}
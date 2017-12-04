package io.wonwoo.dynamodb;

import io.wonwoo.dynamodb.repository.DynamoDBRepositoryFactoryBean;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.config.DynamoDBRepositoryConfigExtension;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

@Configuration
@ConditionalOnClass({ AmazonDynamoDB.class, DynamoDBCrudRepository.class })
@ConditionalOnMissingBean({DynamoDBRepositoryFactoryBean.class,
    DynamoDBRepositoryConfigExtension.class})
@ConditionalOnProperty(prefix = "spring.data.dynamodb.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(DynamoRepositoriesAutoConfigureRegistrar.class)
@AutoConfigureAfter(DynamoDataAutoConfiguration.class)
public class DynamoRepositoriesAutoConfiguration {
}
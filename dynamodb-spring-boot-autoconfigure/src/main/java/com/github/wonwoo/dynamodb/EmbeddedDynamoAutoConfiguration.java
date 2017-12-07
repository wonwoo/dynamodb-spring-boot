package com.github.wonwoo.dynamodb;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.almworks.sqlite4java.SQLite;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;

@Configuration
@ConditionalOnClass({DynamoDBEmbedded.class, SQLite.class})
@AutoConfigureBefore(DynamoAutoConfiguration.class)
public class EmbeddedDynamoAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public AmazonDynamoDB amazonDynamoDB() {
        return DynamoDBEmbedded.create().amazonDynamoDB();
    }

    @Bean
    public DynamoDbCreateTableBeanPostProcessor dynamoDbCreateTableBeanPostProcessor() {
        return new DynamoDbCreateTableBeanPostProcessor();
    }
}

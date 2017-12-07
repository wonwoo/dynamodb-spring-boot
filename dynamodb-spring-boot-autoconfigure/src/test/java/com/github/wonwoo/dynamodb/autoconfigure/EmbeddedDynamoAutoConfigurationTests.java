package com.github.wonwoo.dynamodb.autoconfigure;

import org.junit.After;
import org.junit.Test;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.github.wonwoo.dynamodb.DynamoDataAutoConfiguration;
import com.github.wonwoo.dynamodb.DynamoDbCreateTableBeanPostProcessor;
import com.github.wonwoo.dynamodb.EmbeddedDynamoAutoConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

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
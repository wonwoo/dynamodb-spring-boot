package com.github.wonwoo.dynamodb;

import java.util.ArrayList;
import java.util.List;

import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBPersistentEntityImpl;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBPersistentProperty;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.*;

@Configuration
@ConditionalOnClass(DynamoDBEmbedded.class)
@AutoConfigureBefore(DynamoAutoConfiguration.class)
public class EmbeddedDynamoAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public AmazonDynamoDB amazonDynamoDB(DynamoDBMappingContext context) {
        AmazonDynamoDB amazonDynamoDB = DynamoDBEmbedded.create().amazonDynamoDB();
        for(DynamoDBPersistentEntityImpl<?> entity: context.getPersistentEntities()) {
            DynamoDBPersistentProperty idProperty = entity.getIdProperty();
            DynamoDBTable table = entity.getTypeInformation().getType().getAnnotation(DynamoDBTable.class);
            createTable(amazonDynamoDB, table.tableName(), idProperty.getName());
        }
        return amazonDynamoDB;
    }

    private void createTable(AmazonDynamoDB ddb, String tableName, String hashKeyName) {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition(hashKeyName, ScalarAttributeType.S));
        List<KeySchemaElement> ks = new ArrayList<>();
        ks.add(new KeySchemaElement(hashKeyName, KeyType.HASH));
        ProvisionedThroughput provisionedthroughput = new ProvisionedThroughput(1000L, 1000L);
        CreateTableRequest request =
                new CreateTableRequest()
                        .withTableName(tableName)
                        .withAttributeDefinitions(attributeDefinitions)
                        .withKeySchema(ks)
                        .withProvisionedThroughput(provisionedthroughput);

        ddb.createTable(request);
    }
}

package com.github.wonwoo.dynamodb;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBPersistentEntity;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBPersistentEntityImpl;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBPersistentProperty;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.util.TypeInformation;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.*;

public class DynamoDbMapping {

    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDBMappingContext context;

    public DynamoDbMapping(AmazonDynamoDB amazonDynamoDB, DynamoDBMappingContext context) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.context = context;
    }

    public Collection<DynamoDBPersistentEntityImpl<?>> getPersistentEntities() {
        return context.getPersistentEntities();
    }

    public DynamoDBPersistentEntityImpl<?> getPersistentEntity(Class<?> type) {
        return context.getPersistentEntity(type);
    }

    public DynamoDBPersistentProperty getIdProperty(Class<?> type) {
        return context.getPersistentEntity(type).getIdProperty();
    }

    public TypeInformation<?> getTypeInformation(Class<?> type) {
        return context.getPersistentEntity(type).getTypeInformation();
    }


    public List<CreateTableResult> createTable() {
        List<CreateTableResult> results = new ArrayList<>();
        for (DynamoDBPersistentEntity<?> entity : context.getPersistentEntities()) {
            DynamoDBPersistentProperty idProperty = entity.getIdProperty();
            DynamoDBTable table = findMergedAnnotation(entity.getTypeInformation().getType(), DynamoDBTable.class);
            results.add(createTable(table.tableName(), idProperty.getName()));
        }
        return results;
    }

    private CreateTableResult createTable(String tableName, String hashKeyName) {
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

        return amazonDynamoDB.createTable(request);
    }

    private  <A extends Annotation> A findMergedAnnotation(AnnotatedElement element, Class<A> annotationType) {
        return AnnotatedElementUtils.findMergedAnnotation(element, annotationType);
    }
}

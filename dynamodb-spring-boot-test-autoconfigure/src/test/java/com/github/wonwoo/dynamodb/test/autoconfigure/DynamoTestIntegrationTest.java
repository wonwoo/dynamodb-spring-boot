package com.github.wonwoo.dynamodb.test.autoconfigure;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;

import static org.assertj.core.api.Assertions.assertThat;

@DynamoTest
@RunWith(SpringRunner.class)
public class DynamoTestIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DynamoDBTemplate dynamoDBTemplate;

    @Test
    public void saveTest() {
        personRepository.save(Arrays.asList(new Person("wonwoo"), new Person("keven")));
        List<Person> person = personRepository.findAll();
        assertThat(person.iterator().next().getName()).isEqualTo("wonwoo");
    }

    @Test
    public void dynamoTemplateTest() {
        dynamoDBTemplate.save(new Person("wonwoo"));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<Person> persons = dynamoDBTemplate.scan(Person.class, scanExpression);
        assertThat(persons.iterator().next().getName()).isEqualTo("wonwoo");
    }
}

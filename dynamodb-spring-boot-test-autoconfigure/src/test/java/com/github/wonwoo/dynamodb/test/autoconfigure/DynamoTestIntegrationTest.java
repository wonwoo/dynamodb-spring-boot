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

package com.github.wonwoo.dynamodb.test.autoconfigure;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author wonwoo
 */
@DynamoTest
@RunWith(SpringRunner.class)
public class DynamoTestIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DynamoDBTemplate dynamoDBTemplate;

    @Test
    public void saveTest() {
        personRepository.saveAll(Collections.singletonList(new Person("wonwoo")));
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

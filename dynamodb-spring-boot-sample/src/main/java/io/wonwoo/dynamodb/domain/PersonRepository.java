package io.wonwoo.dynamodb.domain;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

import io.wonwoo.dynamodb.repository.DynamoDBRepository;

@EnableScan
public interface PersonRepository extends DynamoDBRepository<Person, String> {
}

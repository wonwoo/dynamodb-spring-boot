package com.github.wonwoo.dynamodb.autoconfigure.person;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {
}
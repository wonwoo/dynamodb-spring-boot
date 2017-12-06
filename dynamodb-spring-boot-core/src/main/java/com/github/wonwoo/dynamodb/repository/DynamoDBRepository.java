package com.github.wonwoo.dynamodb.repository;

import java.io.Serializable;
import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DynamoDBRepository<T, ID extends Serializable> extends DynamoDBCrudRepository<T, ID> {

  @Override
  List<T> findAll();

  @Override
  List<T> findAll(Iterable<ID> ids);

  @Override
  <S extends T> List<S> save(Iterable<S> entites);
}
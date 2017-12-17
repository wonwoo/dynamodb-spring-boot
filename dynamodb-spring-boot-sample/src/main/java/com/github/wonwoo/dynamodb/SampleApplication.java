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

package com.github.wonwoo.dynamodb;

import com.github.wonwoo.dynamodb.domain.Person;
import com.github.wonwoo.dynamodb.domain.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

/**
 * @author wonwoo
 */
@SpringBootApplication
public class SampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(SampleApplication.class, args);
  }

  @Bean
  CommandLineRunner commandLineRunner(PersonRepository personRepository) {
    return args -> {
      personRepository.saveAll(Arrays.asList(
          new Person("kevin"),
          new Person("josh long"))
      );
      personRepository.findAll(PageRequest.of(0, 3, null))
          .forEach(System.out::println);
    };
  }
}

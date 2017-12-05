package io.wonwoo.dynamodb;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.wonwoo.dynamodb.domain.Person;
import io.wonwoo.dynamodb.domain.PersonRepository;

@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(PersonRepository personRepository) {
        return args -> {
            personRepository.save(Arrays.asList(
                    new Person("kevin"),
                    new Person("josh long"))
            );
            personRepository.findAll()
                    .forEach(System.out::println);
        };
    }
}

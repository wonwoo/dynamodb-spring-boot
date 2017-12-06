package com.github.wonwoo.dynamodb.test.autoconfigure;

import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    public String getMessage() {
        return "Hello World";
    }
}
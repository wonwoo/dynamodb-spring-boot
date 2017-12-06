package com.github.wonwoo.dynamodb.test.autoconfigure;

import java.lang.annotation.*;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration
public @interface AutoConfigureDynamo {
}

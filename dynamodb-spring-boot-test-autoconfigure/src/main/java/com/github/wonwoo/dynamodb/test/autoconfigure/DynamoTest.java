package com.github.wonwoo.dynamodb.test.autoconfigure;

import java.lang.annotation.*;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.BootstrapWith;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(DynamoTypeExcludeFilter.class)
@AutoConfigureCache
@AutoConfigureDynamo
@ImportAutoConfiguration
public @interface DynamoTest {

    boolean useDefaultFilters() default true;


    ComponentScan.Filter[] includeFilters() default {};


    ComponentScan.Filter[] excludeFilters() default {};


    @AliasFor(annotation = ImportAutoConfiguration.class, attribute = "exclude")
    Class<?>[] excludeAutoConfiguration() default {};

}

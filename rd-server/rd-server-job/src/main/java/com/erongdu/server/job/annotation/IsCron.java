package com.erongdu.server.job.annotation;


import javax.validation.Constraint;
import javax.validation.Payload;

import com.erongdu.server.job.validator.CronValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author erongdu.com
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CronValidator.class)
public @interface IsCron {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
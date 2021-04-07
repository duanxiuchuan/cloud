package com.erongdu.server.job.validator;

import org.quartz.CronExpression;

import com.erongdu.server.job.annotation.IsCron;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验是否为合法的 Cron表达式
 *
 * @author erongdu.com
 */
public class CronValidator implements ConstraintValidator<IsCron, String> {

    @Override
    public void initialize(IsCron isCron) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return CronExpression.isValidExpression(value);
        } catch (Exception e) {
            return false;
        }
    }
}
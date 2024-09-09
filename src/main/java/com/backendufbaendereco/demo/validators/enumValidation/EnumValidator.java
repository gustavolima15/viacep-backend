package com.backendufbaendereco.demo.validators.enumValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EnumValidator implements ConstraintValidator<EnumValue, Object> {
    private List<String> acceptedValues;
    private  boolean isIgnoreCase;
    @Override
    public void initialize(EnumValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.isIgnoreCase = constraintAnnotation.ignoreCase();

        acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .map(value -> value.toLowerCase())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        String valueFormatated = isIgnoreCase ? value.toString().toLowerCase() : value.toString();
        return acceptedValues.contains(valueFormatated);
    }
}

package com.ktx.core.utils;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ValidatorUtils {

	private final SmartValidator validator;

	/**
	 * validate value
	 * @param target value will be validated
	 * @throws IllegalArgumentException if value is invalid
	 */
	public void validate(Object target) {
		Errors errors = new BeanPropertyBindingResult(target, target.getClass().getName());
        this.validator.validate(target, errors);
        
        if (!errors.hasErrors()) return;
        String errorMessage = errors.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        throw new IllegalArgumentException(errorMessage);
	}
}

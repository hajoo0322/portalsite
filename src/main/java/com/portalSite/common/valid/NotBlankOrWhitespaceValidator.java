package com.portalSite.common.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankOrWhitespaceValidator implements ConstraintValidator<NotBlankOrWhitespace, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null 은 허용, 빈 문자열이나 공백만 있는 문자열은 허용하지 않음
        return value == null || !value.trim().isEmpty();
    }
}

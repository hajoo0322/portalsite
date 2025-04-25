package com.portalSite.common.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoDuplicateGradeOrderValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicateGradeOrder {
    String message() default "등급 순서에 중복이 존재합니다.";
    Class<?>[] groups() default {};
    Class<?extends Payload>[] payload() default {};
}

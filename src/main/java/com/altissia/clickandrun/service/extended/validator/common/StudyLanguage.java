package com.altissia.clickandrun.service.extended.validator.common;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = StudyLanguageValidator.class)
public @interface StudyLanguage {

    String message() default "{clickandrun.error.validation.field.studyLanguage}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

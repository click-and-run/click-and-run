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
@Constraint(validatedBy = MembershipValidator.class)
public @interface Membership {

    String message() default "{clickandrun.error.validation.field.membership}";

    Class<?>[] groups() default {};

    // todo define severity as a payload
    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum> value();
}

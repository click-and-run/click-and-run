package com.altissia.clickandrun.service.extended.validator.common;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MembershipValidator implements ConstraintValidator<Membership, String> {
    private List<String> members;

    @Override
    public void initialize(Membership membership) {
        this.members = Arrays.stream(membership.value().getEnumConstants())
            .map(Enum::name)
            .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return members.contains(s);
    }
}

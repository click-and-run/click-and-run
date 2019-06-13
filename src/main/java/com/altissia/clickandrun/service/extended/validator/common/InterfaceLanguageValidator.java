package com.altissia.clickandrun.service.extended.validator.common;

import com.altissia.clickandrun.domain.enumeration.Language;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class InterfaceLanguageValidator implements ConstraintValidator<InterfaceLanguage, String> {

    @Override
    public void initialize(InterfaceLanguage interfaceLanguage) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(Language.values())
            .filter(Language::isInterfaceLanguage)
            .anyMatch(language -> language.name().equals(s));
    }
}

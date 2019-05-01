package com.altissia.clickandrun.service.extended.validator.common;

import com.altissia.clickandrun.domain.enumeration.Language;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class StudyLanguageValidator implements ConstraintValidator<StudyLanguage, String> {

    @Override
    public void initialize(StudyLanguage studyLanguage) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(Language.values())
            .filter(Language::isStudyLanguage)
            .anyMatch(language -> language.name().equals(s));
    }
}

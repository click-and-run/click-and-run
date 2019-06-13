package com.altissia.clickandrun.service.extended.validator.common;

import com.altissia.clickandrun.domain.enumeration.Language;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.ServiceRow;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class StudyLanguageValidatorTest {
    
    private ServiceRow serviceRow = new ServiceRow();
    private Validator v;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        ValidatorFactory beanValidatorFactory = Validation.buildDefaultValidatorFactory();
        v = beanValidatorFactory.getValidator();

        field("row", 1);
        field("login", "ahorgnies@altissia.org");
        field("service", "COURSE");
        field("studyLanguage", "EN_GB");
        field("duration", 5);
    }

    private void field(String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = serviceRow.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(serviceRow, value);
    }

    private void isStudyLanguageViolation(Set<ConstraintViolation<ServiceRow>> violations) {
        assertThat(violations, hasSize(1));

        ConstraintViolation<ServiceRow> v = new ArrayList<>(violations).get(0);

        assertThat(v.getPropertyPath().toString(), is("studyLanguage"));
        assertThat(v.getConstraintDescriptor().getAnnotation(), instanceOf(StudyLanguage.class));
    }

    @Test
    public void testNull() throws IllegalAccessException, NoSuchFieldException {
        field("studyLanguage", null);
        Set<ConstraintViolation<ServiceRow>> violations = v.validate(serviceRow);

        isStudyLanguageViolation(violations);
    }

    @Test
    public void testBlank() throws IllegalAccessException, NoSuchFieldException {
        field("studyLanguage", "");

        Set<ConstraintViolation<ServiceRow>> violations = v.validate(serviceRow);

        isStudyLanguageViolation(violations);
    }

    @Test
    public void testGarbage() throws IllegalAccessException, NoSuchFieldException {
        field("studyLanguage", "NOT_A_STUDY_LANGUAGE");
        Set<ConstraintViolation<ServiceRow>> violations = v.validate(serviceRow);

        isStudyLanguageViolation(violations);
    }

    @Test
    public void testNotStudyLanguage() throws IllegalAccessException, NoSuchFieldException {
        field("studyLanguage", Language.IT_IT.name());
        Set<ConstraintViolation<ServiceRow>> violations = v.validate(serviceRow);

        isStudyLanguageViolation(violations);
    }

    @Test
    public void testNominal() throws NoSuchFieldException, IllegalAccessException {
        field("studyLanguage", Language.FR_FR.name());
        Set<ConstraintViolation<ServiceRow>> violations = v.validate(serviceRow);

        assertThat(violations, hasSize(0));
    }
}

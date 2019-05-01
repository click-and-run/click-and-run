package com.altissia.clickandrun.web.rest.extended.validator.common;

import com.altissia.clickandrun.domain.enumeration.Service;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.ServiceRow;
import com.altissia.clickandrun.service.extended.validator.common.Membership;
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

public class MemberShipValidatorTest {

    private ServiceRow serviceRow = new ServiceRow();
    private Validator beanValidator;
    private Field service;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        ValidatorFactory beanValidatorFactory = Validation.buildDefaultValidatorFactory();
        beanValidator = beanValidatorFactory.getValidator();

        Field row = serviceRow.getClass().getDeclaredField("row");
        row.setAccessible(true);
        row.set(serviceRow, 1);
        Field login = serviceRow.getClass().getDeclaredField("login");
        login.setAccessible(true);
        login.set(serviceRow, "ahorgnies@altissia.org");
        service = serviceRow.getClass().getDeclaredField("service");
        service.setAccessible(true);
        service.set(serviceRow, "COURSE");
        Field studyLanguage = serviceRow.getClass().getDeclaredField("studyLanguage");
        studyLanguage.setAccessible(true);
        studyLanguage.set(serviceRow, "EN_GB");
        Field duration = serviceRow.getClass().getDeclaredField("duration");
        duration.setAccessible(true);
        duration.set(serviceRow, 5);

    }

    @Test
    public void testNull() throws IllegalAccessException {
        service.set(serviceRow, null);
        Set<ConstraintViolation<ServiceRow>> violations = beanValidator.validate(serviceRow);

        assertThat(violations, hasSize(1));

        ConstraintViolation<ServiceRow> v = new ArrayList<>(violations).get(0);

        assertThat(v.getPropertyPath().toString(), is("service"));
        assertThat(v.getConstraintDescriptor().getAnnotation(), instanceOf(Membership.class));
    }

    @Test
    public void testBlank() throws IllegalAccessException {
        service.set(serviceRow, "");
        Set<ConstraintViolation<ServiceRow>> violations = beanValidator.validate(serviceRow);

        assertThat(violations, hasSize(1));

        ConstraintViolation<ServiceRow> v = new ArrayList<>(violations).get(0);

        assertThat(v.getPropertyPath().toString(), is("service"));
        assertThat(v.getConstraintDescriptor().getAnnotation(), instanceOf(Membership.class));
    }

    @Test
    public void testWrong() throws IllegalAccessException {
        service.set(serviceRow, "NOT_A_SERVICE");
        Set<ConstraintViolation<ServiceRow>> violations = beanValidator.validate(serviceRow);

        assertThat(violations, hasSize(1));

        ConstraintViolation<ServiceRow> v = new ArrayList<>(violations).get(0);

        assertThat(v.getPropertyPath().toString(), is("service"));
        assertThat(v.getConstraintDescriptor().getAnnotation(), instanceOf(Membership.class));
    }

    @Test
    public void testNominal() throws IllegalAccessException {
        service.set(serviceRow, Service.COURSE.name());
        Set<ConstraintViolation<ServiceRow>> violations = beanValidator.validate(serviceRow);

        assertThat(violations, hasSize(0));
    }
}

package com.altissia.clickandrun.service.extended.validator.common;

import com.altissia.clickandrun.domain.enumeration.Service;
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

public class MemberShipValidatorTest {

    private ServiceRow serviceRow = new ServiceRow();
    private Validator beanValidator;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        ValidatorFactory beanValidatorFactory = Validation.buildDefaultValidatorFactory();
        beanValidator = beanValidatorFactory.getValidator();

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

    private void isMembershipViolation(Set<ConstraintViolation<ServiceRow>> violations) {
        assertThat(violations, hasSize(1));

        ConstraintViolation<ServiceRow> v = new ArrayList<>(violations).get(0);

        assertThat(v.getPropertyPath().toString(), is("service"));
        assertThat(v.getConstraintDescriptor().getAnnotation(), instanceOf(Membership.class));
    }

    @Test
    public void testNull() throws IllegalAccessException, NoSuchFieldException {
        field("service", null);
        Set<ConstraintViolation<ServiceRow>> violations = beanValidator.validate(serviceRow);

        isMembershipViolation(violations);
    }

    @Test
    public void testBlank() throws IllegalAccessException, NoSuchFieldException {
        field("service", "");
        Set<ConstraintViolation<ServiceRow>> violations = beanValidator.validate(serviceRow);

        isMembershipViolation(violations);
    }

    @Test
    public void testWrong() throws IllegalAccessException, NoSuchFieldException {
        field("service", "NOT_A_SERVICE");
        Set<ConstraintViolation<ServiceRow>> violations = beanValidator.validate(serviceRow);

        isMembershipViolation(violations);
    }

    @Test
    public void testNominal() throws IllegalAccessException, NoSuchFieldException {
        field("service", Service.COURSE.name());
        Set<ConstraintViolation<ServiceRow>> violations = beanValidator.validate(serviceRow);

        assertThat(violations, hasSize(0));
    }
}

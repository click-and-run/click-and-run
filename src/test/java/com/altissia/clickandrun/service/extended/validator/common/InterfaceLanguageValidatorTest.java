package com.altissia.clickandrun.service.extended.validator.common;

import com.altissia.clickandrun.domain.enumeration.Language;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrantRow;
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

public class InterfaceLanguageValidatorTest {

    private RegistrantRow registrantRow = new RegistrantRow();
    private Validator v;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        v = validatorFactory.getValidator();

        field("firstName", "Adrien");
        field("lastName", "Horgnies");
        field("login", "ahorgnies@altissia.org");
        field("interfaceLanguage", "EN_GB");
    }

    private void field(String name, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = registrantRow.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(registrantRow, value);
    }

    private void isInterfaceLanguageViolation(Set<ConstraintViolation<RegistrantRow>> violations) {
        assertThat(violations, hasSize(1));

        ConstraintViolation<RegistrantRow> v = new ArrayList<>(violations).get(0);

        assertThat(v.getPropertyPath().toString(), is("interfaceLanguage"));
        assertThat(v.getConstraintDescriptor().getAnnotation(), instanceOf(InterfaceLanguage.class));
    }

    @Test
    public void testNull() throws IllegalAccessException, NoSuchFieldException {
        field("interfaceLanguage", null);
        Set<ConstraintViolation<RegistrantRow>> violations = v.validate(registrantRow);

        isInterfaceLanguageViolation(violations);
    }

    @Test
    public void testBlank() throws IllegalAccessException, NoSuchFieldException {
        field("interfaceLanguage", "");

        Set<ConstraintViolation<RegistrantRow>> violations = v.validate(registrantRow);

        isInterfaceLanguageViolation(violations);
    }

    @Test
    public void testWrong() throws IllegalAccessException, NoSuchFieldException {
        field("interfaceLanguage", "NOT_AN_INTERFACE_LANGUAGE");
        Set<ConstraintViolation<RegistrantRow>> violations = v.validate(registrantRow);

        isInterfaceLanguageViolation(violations);
    }

    @Test
    public void testNominal() throws IllegalAccessException, NoSuchFieldException {
        field("interfaceLanguage", Language.EN_GB.name());
        Set<ConstraintViolation<RegistrantRow>> violations = v.validate(registrantRow);

        assertThat(violations, hasSize(0));
    }
}

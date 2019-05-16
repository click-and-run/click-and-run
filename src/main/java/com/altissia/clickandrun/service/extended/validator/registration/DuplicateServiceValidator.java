package com.altissia.clickandrun.service.extended.validator.registration;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWorkbook;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.ServiceRow;
import com.altissia.clickandrun.domain.spreadsheet.validation.FieldValidation;
import com.altissia.clickandrun.service.extended.validator.common.DuplicateRowValidator;
import com.altissia.clickandrun.service.extended.validator.common.Severity;
import org.springframework.stereotype.Component;

@Component
public class DuplicateServiceValidator extends DuplicateRowValidator<ServiceRow> {

    protected DuplicateServiceValidator() {
        super(Severity.ERROR);
    }

    @Override
    public FieldValidation getFieldValidation(ServiceRow row) {
        return new FieldValidation("login", row.getLogin(), "com.altissia.constraints.service.duplicate");
    }

    @Override
    public boolean isApplicableTo(Sheet<? extends Row> sheet) {
        return sheet instanceof RegistrationWorkbook.ServicesSheet;
    }
}

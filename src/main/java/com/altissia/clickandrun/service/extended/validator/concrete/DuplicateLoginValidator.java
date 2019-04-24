package com.altissia.clickandrun.service.extended.validator.concrete;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrantRow;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWB;
import com.altissia.clickandrun.domain.spreadsheet.validation.FieldValidation;
import com.altissia.clickandrun.service.extended.validator.common.DuplicateCellValidator;
import com.altissia.clickandrun.service.extended.validator.common.Severity;
import org.springframework.stereotype.Component;

@Component
public class DuplicateLoginValidator extends DuplicateCellValidator<RegistrantRow> {

    public DuplicateLoginValidator() {
        super(Severity.ERROR);
    }

    @Override
    public boolean isApplicableTo(Sheet<? extends Row> sheet) {
        return sheet instanceof RegistrationWB.RegistrantsSheet;
    }

    @Override
    public String getCellValue(RegistrantRow row) {
        return row.getLogin();
    }

    @Override
    public FieldValidation getFieldValidation(RegistrantRow row) {
        return new FieldValidation("login", row.getLogin(), "com.altissia.constraints.login.duplicate");
    }
}

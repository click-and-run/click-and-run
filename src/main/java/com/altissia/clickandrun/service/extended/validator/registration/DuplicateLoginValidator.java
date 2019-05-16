package com.altissia.clickandrun.service.extended.validator.registration;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrantRow;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWorkbook;
import com.altissia.clickandrun.domain.spreadsheet.validation.RowValidation;
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
        return sheet instanceof RegistrationWorkbook.RegistrantsSheet;
    }

    @Override
    public String getCellValue(RegistrantRow row) {
        return row.getLogin();
    }

    @Override
    public RowValidation getRowValidation(RegistrantRow row) {
        return new RowValidation(row.getRow(), "login", row.getLogin(), "com.altissia.constraints.login.duplicate");
    }
}

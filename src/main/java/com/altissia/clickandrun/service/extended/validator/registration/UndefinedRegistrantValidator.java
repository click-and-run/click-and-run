package com.altissia.clickandrun.service.extended.validator.registration;

import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrantRow;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWB;
import com.altissia.clickandrun.domain.spreadsheet.validation.FieldValidation;
import com.altissia.clickandrun.domain.spreadsheet.validation.RowValidation;
import com.altissia.clickandrun.service.extended.validator.WorkbookValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class UndefinedRegistrantValidator extends WorkbookValidator {
    @Override
    public boolean isApplicableTo(Workbook definition) {
        return definition instanceof RegistrationWB;
    }

    @Override
    public long validate(Workbook workbook) {
        //noinspection unchecked
        List<RegistrantRow> registrantRows = (List<RegistrantRow>) workbook.getSheetRows(RegistrationWB.SHEET_REGISTRANTS);

        Set<String> definedLogins = registrantRows.stream()
            .map(RegistrantRow::getLogin)
            .collect(Collectors.toSet());

        RegistrationWB.ServicesSheet servicesSheet = (RegistrationWB.ServicesSheet) workbook.getSheet(RegistrationWB.SHEET_SERVICES);

        AtomicLong undefined = new AtomicLong(0);
        servicesSheet.getRows().stream()
            .filter(serviceRow -> !definedLogins.contains(serviceRow.getLogin()))
            .forEach(serviceRow -> {
                undefined.getAndIncrement();
                servicesSheet.addRowError(new RowValidation(serviceRow.getRow(), new FieldValidation("login", serviceRow.getLogin(), "com.altissia.constraints.registrant.undefined")));
            });

        return undefined.get();
    }
}

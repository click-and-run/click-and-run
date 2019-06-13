package com.altissia.clickandrun.service.extended.validator.registration;

import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrantRow;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWorkbook;
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
        return definition instanceof RegistrationWorkbook;
    }

    @Override
    public long validate(Workbook workbook) {
        List<RegistrantRow> registrantRows = workbook.getSheetRows(RegistrationWorkbook.SHEET_REGISTRANTS);

        Set<String> definedLogins = registrantRows.stream()
            .map(RegistrantRow::getLogin)
            .collect(Collectors.toSet());

        RegistrationWorkbook.ServicesSheet servicesSheet = (RegistrationWorkbook.ServicesSheet) workbook.getSheet(RegistrationWorkbook.SHEET_SERVICES);

        AtomicLong undefined = new AtomicLong(0);
        servicesSheet.getRows().stream()
            .filter(serviceRow -> !definedLogins.contains(serviceRow.getLogin()))
            .forEach(serviceRow -> {
                undefined.getAndIncrement();
                servicesSheet.addRowError(new RowValidation(serviceRow.getRow(), "login", serviceRow.getLogin(), "com.altissia.constraints.registrant.undefined"));
            });

        return undefined.get();
    }
}

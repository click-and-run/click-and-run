package com.altissia.clickandrun.service.extended.validator.registration;

import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWorkbook;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.ServiceRow;
import com.altissia.clickandrun.domain.spreadsheet.validation.RowValidation;
import com.altissia.clickandrun.service.extended.validator.WorkbookValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class UnusedRegistrantValidator extends WorkbookValidator {
    @Override
    public boolean isApplicableTo(Workbook definition) {
        return definition instanceof RegistrationWorkbook;
    }

    @Override
    public long validate(Workbook workbook) {
        List<ServiceRow> serviceRows = workbook.getSheetRows(RegistrationWorkbook.SHEET_SERVICES);

        Set<String> usedLogins = serviceRows.stream().map(ServiceRow::getLogin).collect(Collectors.toSet());

        RegistrationWorkbook.RegistrantsSheet registrantsSheet = (RegistrationWorkbook.RegistrantsSheet) workbook.getSheet(RegistrationWorkbook.SHEET_REGISTRANTS);

        AtomicLong unused = new AtomicLong(0);
        registrantsSheet.getRows().stream()
            .filter(registrantRow -> !usedLogins.contains(registrantRow.getLogin()))
            .forEach(registrantRow -> {
                unused.getAndIncrement();
                registrantsSheet.addRowError(new RowValidation(registrantRow.getRow(), "login", registrantRow.getLogin(), "com.altissia.constraints.registrant.unused"));
            });

        return unused.get();
    }
}

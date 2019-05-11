package com.altissia.clickandrun.service.extended.validator.registration;

import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWB;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.ServiceRow;
import com.altissia.clickandrun.domain.spreadsheet.validation.FieldValidation;
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
        return definition instanceof RegistrationWB;
    }

    @Override
    public long validate(Workbook workbook) {
        //noinspection unchecked
        List<ServiceRow> serviceRows = (List<ServiceRow>) workbook.getSheetRows(RegistrationWB.SHEET_SERVICES);

        Set<String> usedLogins = serviceRows.stream().map(ServiceRow::getLogin).collect(Collectors.toSet());

        RegistrationWB.RegistrantsSheet registrantsSheet = (RegistrationWB.RegistrantsSheet) workbook.getSheet(RegistrationWB.SHEET_REGISTRANTS);

        AtomicLong unused = new AtomicLong(0);
        registrantsSheet.getRows().stream()
            .filter(registrantRow -> !usedLogins.contains(registrantRow.getLogin()))
            .forEach(registrantRow -> {
                unused.getAndIncrement();
                registrantsSheet.addRowError(new RowValidation(registrantRow.getRow(), new FieldValidation("login", registrantRow.getLogin(), "com.altissia.constraints.registrant.unused")));
            });

        return unused.get();
    }
}

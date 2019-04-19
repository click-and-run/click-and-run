package com.altissia.clickandrun.service.extended.validator.common;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.validation.FieldValidation;
import com.altissia.clickandrun.domain.spreadsheet.validation.RowValidation;
import com.altissia.clickandrun.service.extended.validator.SheetValidator;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Abstract validation class to validate that a record is not defined twice in the same sheet.
 * This validator rely on the equals method of the {@link Row} object.
 */
public abstract class DuplicateRowValidator extends SheetValidator {

    private Severity severity;

    protected DuplicateRowValidator(Severity severity) {
        this.severity = severity;
    }

    @Override
    public long validate(Sheet<? extends Row> sheet) {

        AtomicLong existing = new AtomicLong(0);

        sheet.getRows().stream()
            .filter(i -> Collections.frequency(sheet.getRows(), i) > 1)
            .forEach(row -> {
                existing.getAndIncrement();
                if (severity == Severity.ERROR) {
                    sheet.addRowError(new RowValidation(
                        row.getRow(), this.getFieldValidation(row)
                    ));
                } else {
                    sheet.addRowWarning(new RowValidation(
                        row.getRow(), this.getFieldValidation(row)
                    ));
                }

            });

        return existing.get();
    }

    public abstract FieldValidation getFieldValidation(Row row);
}

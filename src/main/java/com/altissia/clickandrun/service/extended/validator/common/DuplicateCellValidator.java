package com.altissia.clickandrun.service.extended.validator.common;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.validation.FieldValidation;
import com.altissia.clickandrun.domain.spreadsheet.validation.RowValidation;
import com.altissia.clickandrun.service.extended.validator.SheetValidator;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Validate that a cell is not defined twice in the same workbook
 */
@Component
public abstract class DuplicateCellValidator<T extends Row> extends SheetValidator<T> {

    private Severity severity;

    protected DuplicateCellValidator(Severity severity) {
        this.severity = severity;
    }

    @Override
    public long validate(Sheet<T> sheet) {

        List<String> cellsValue = sheet.getRows().stream()
            .map(this::getCellValue)
            .collect(Collectors.toList());

        AtomicLong existing = new AtomicLong(0);
        sheet.getRows().stream()
            .filter(row -> Collections.frequency(cellsValue, getCellValue(row)) > 1)
            .forEach(row -> {
                existing.getAndIncrement();
                if (this.severity.equals(Severity.ERROR)) {
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

    public abstract String getCellValue(T row);

    public abstract FieldValidation getFieldValidation(T row);
}

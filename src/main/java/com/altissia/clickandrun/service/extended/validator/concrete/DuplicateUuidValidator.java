package com.altissia.clickandrun.service.extended.validator.concrete;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.concrete.LAQuestionRow;
import com.altissia.clickandrun.domain.spreadsheet.concrete.LAQuestionWB;
import com.altissia.clickandrun.domain.spreadsheet.validation.FieldValidation;
import com.altissia.clickandrun.service.extended.validator.common.DuplicateCellValidator;
import com.altissia.clickandrun.service.extended.validator.common.Severity;
import org.springframework.stereotype.Component;

@Component
public class DuplicateUuidValidator extends DuplicateCellValidator<LAQuestionRow> {

    public DuplicateUuidValidator() {
        super(Severity.ERROR);
    }

    @Override
    public boolean isApplicableTo(Sheet<? extends Row> sheet) {
        return sheet instanceof LAQuestionWB.QuestionsSheet;
    }

    @Override
    public String getCellValue(LAQuestionRow row) {
        return row.getUuid();
    }

    @Override
    public FieldValidation getFieldValidation(LAQuestionRow row) {
        return new FieldValidation("name", row.getUuid(), "com.altissia.constraints.name.duplicate");
    }
}

package com.altissia.clickandrun.service.extended.processor;

import com.altissia.clickandrun.domain.spreadsheet.validation.SheetValidation;

import java.util.HashMap;
import java.util.Map;

public class ProcessorResult {

    private Map<String, SheetValidation> validations;

    public ProcessorResult() {
        validations = new HashMap<>();
    }

    public ProcessorResult(Map<String, SheetValidation> validations) {
        this.validations = validations;
    }

    public Map<String, SheetValidation> getValidations() {
        return validations;
    }

    @Override
    public String toString() {
        return "ProcessorResult{" +
            "validations=" + validations +
            '}';
    }
}


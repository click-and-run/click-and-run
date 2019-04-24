package com.altissia.clickandrun.domain.spreadsheet;


import com.altissia.clickandrun.domain.spreadsheet.validation.FieldValidation;
import com.altissia.clickandrun.domain.spreadsheet.validation.RowValidation;
import com.altissia.clickandrun.domain.spreadsheet.validation.SheetValidation;
import com.poiji.annotation.ExcelCellName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class defining a single sheet within a workbook
 *
 * @param <T> the class representing the actual model of a row within this sheet
 */
public abstract class Sheet<T extends Row> {

    /**
     * The sheet name
     */
    private String name;

    /**
     * The sheet model describing one row
     */
    private Class<T> model;

    /**
     * The rows
     */
    private List<T> rows;


    /**
     * The validity of this sheet
     */
    private SheetValidation validity;

    protected Sheet(String name, Class<T> model) {
        this.name = name;
        this.model = model;
        this.rows = new ArrayList<>();
        this.validity = new SheetValidation();
    }

    public String getName() {
        return name;
    }

    public Class<T> getModel() {
        return model;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<? extends Row> rows) {
        this.rows = (List<T>) rows;
    }

    public SheetValidation getValidity() {
        return this.validity;
    }

    /**
     * Get all the header based on the annotated field in the related model
     *
     * @return a list of headers name
     */
    public List<String> getHeaders() {
        List<String> headers = new ArrayList<>();

        // Retrieve all expected header fields
        for (Field field : this.getModel().getDeclaredFields()) {
            ExcelCellName excelCellName = field.getAnnotation(ExcelCellName.class);
            if (excelCellName != null) {
                headers.add(excelCellName.value());
            }
        }

        return headers;
    }

    /**
     * Add an error to the header constraint violations
     *
     * @param error the actual error
     */
    public void addHeaderError(FieldValidation error) {
        this.validity.addHeaderError(error);
    }

    /**
     * Add an error to the row constraint violations
     *
     * @param error the actual error
     */
    public void addRowError(RowValidation error) {
        this.validity.addError(error);
    }

    /**
     * Add a warning to the row constraint violations
     *
     * @param warning the actual warning
     */
    public void addRowWarning(RowValidation warning) {
        this.validity.addWarning(warning);
    }

    /**
     * @return true if there is no error
     */
    public boolean isValid() {
        return validity.isValid();
    }

    public boolean hasError() {
        return !this.validity.getError().isEmpty();
    }

    public boolean hasWarning() {
        return !this.validity.getWarning().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sheet)) return false;
        Sheet sheet = (Sheet) o;
        return Objects.equals(name, sheet.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Sheet{" +
            "name='" + name + '\'' +
            ", rows=" + rows +
            ", validity=" + validity +
            '}';
    }
}

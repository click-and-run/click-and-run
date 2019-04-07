package com.altissia.clickandrun.domain.spreadsheet;


import com.altissia.clickandrun.domain.spreadsheet.validation.FieldValidation;
import com.altissia.clickandrun.domain.spreadsheet.validation.RowValidation;
import com.altissia.clickandrun.domain.spreadsheet.validation.SheetValidation;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class defining a single sheet within a workbook
 * @param <T> the class representing the actual model of a row within this sheet
 */
public abstract class Sheet<T extends Row> {

	/**
	 * The sheet name
	 */
	private String name;

    /**
     * The rows
     */
    private List<T> rows;


    /**
	 * The validity of this sheet
	 */
	private SheetValidation validity;

	protected Sheet(String name) {
		this.name = name;
        this.rows = new ArrayList<>();
		this.validity = new SheetValidation();
	}

	public String getName() {
		return name;
	}

	public Class<T> getModel() throws NoSuchFieldException {
        Field rows = Sheet.class.getDeclaredField("rows");
        ParameterizedType genericType = (ParameterizedType) rows.getGenericType();
        return (Class<T>) genericType.getActualTypeArguments()[0];
	}

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public SheetValidation getValidity() {
		return this.validity;
	}

	/**
	 * Add an error to the header constraint violations
	 * @param error the actual error
	 */
	public void addHeaderError(FieldValidation error) {
		this.validity.addHeaderError(error);
	}

	/**
	 * Add an error to the row constraint violations
	 * @param error the actual error
	 */
	public void addRowError(RowValidation error) {
		this.validity.addError(error);
	}

	/**
	 * Add a warning to the row constraint violations
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
		return ! this.validity.getError().isEmpty();
	}

	public boolean hasWarning() {
		return ! this.validity.getWarning().isEmpty();
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

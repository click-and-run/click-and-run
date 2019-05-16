package com.altissia.clickandrun.domain.spreadsheet;


import com.altissia.clickandrun.domain.spreadsheet.validation.SheetValidation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An  workbook.
 * Contains the workbook definition and the actual data of the workbook.
 */
public abstract class Workbook {

    protected boolean ignoringSuperfluousHeaders;

    protected Map<String, Sheet<? extends Row>> sheets;

	public Workbook() {
        this.sheets = new TreeMap<>();
	}

	/**
	 * Get the rows of a given sheet
	 * @param sheetName the name of the sheet
	 * @return the model related to the sheet
	 */
	@SuppressWarnings("unchecked")
	public <T extends Row> List<T> getSheetRows(String sheetName) {
        return (List<T>) this.sheets.get(sheetName).getRows();
	}

	public Collection<Sheet<? extends Row>> getSheets() {
        return sheets.values();
    }

	public Map<String, SheetValidation> getValidations() {
        return this.sheets.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().getValidity()
            ));
	}

	public SheetValidation getValidations(String sheetName) {
		return sheets.get(sheetName).getValidity();
	}

	public Boolean isValid() {
        return this.sheets.values().stream().allMatch(Sheet::isValid);
	}

	public boolean hasError() {
        return this.sheets.values().stream().anyMatch(Sheet::hasError);
	}

	public boolean hasWarning() {
        return this.sheets.values().stream().anyMatch(Sheet::hasWarning);
	}

    public boolean isIgnoringSuperfluousHeaders() {
        return ignoringSuperfluousHeaders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workbook)) return false;
        Workbook workbook = (Workbook) o;
        return sheets.equals(workbook.sheets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sheets);
    }

    @Override
    public String toString() {
        return "Workbook{" +
            "sheets=" + sheets +
            '}';
    }
}

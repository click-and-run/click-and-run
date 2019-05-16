package com.altissia.clickandrun.domain.spreadsheet.validation;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Sheet validation, describe a set of constraint violation for a all the rows of a sheet.
 * Constraints are divided in 3 sectors: header, row errors, row warnings
 */
public class SheetValidation {

	private Set<HeaderValidation> headers;

	private Set<RowValidation> error;

	private Set<RowValidation> warning;

	public SheetValidation() {
		this.headers = new TreeSet<>();
		this.error = new TreeSet<>();
		this.warning = new TreeSet<>();
	}

	public Set<HeaderValidation> getHeaders() {
		return headers;
	}

	public void addHeaderError(HeaderValidation violation) {
		this.headers.add(violation);
	}

	public Set<RowValidation> getError() {
		return error;
	}

	public void addError(RowValidation error) {
		this.error.add(error);
	}

	public Set<RowValidation> getWarning() {
		return warning;
	}

	public void addWarning(RowValidation warning) {
		this.warning.add(warning);
	}

	/**
	 * Quickly assert if error are present in the sheet.
	 * @return true if no error are found in the header nor in the workbook body
	 */
	public boolean isValid() {
		return headers.isEmpty() && error.isEmpty();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SheetValidation)) return false;
		SheetValidation that = (SheetValidation) o;
		return Objects.equals(headers, that.headers) &&
				Objects.equals(error, that.error) &&
				Objects.equals(warning, that.warning);
	}

	@Override
	public int hashCode() {
		return Objects.hash(headers, error, warning);
	}

	@Override
	public String toString() {
		return "SheetValidation{" +
				"headers=" + headers +
				", error=" + error +
				", warning=" + warning +
				'}';
	}
}

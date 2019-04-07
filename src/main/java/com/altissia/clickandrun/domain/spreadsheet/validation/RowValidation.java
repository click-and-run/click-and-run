package com.altissia.clickandrun.domain.spreadsheet.validation;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Row validation, describe a set of constraint violation for a specific row.
 */
public class RowValidation implements Comparable<RowValidation> {

	private int row;

	private Set<FieldValidation> violations;

	public RowValidation(int row, FieldValidation constraint) {
		this.row = row;
		this.violations = new TreeSet<>();
		this.violations.add(constraint);
	}

	public RowValidation(int row, List<FieldValidation> violations) {
		this.row = row;
		this.violations = new TreeSet<>(violations);
	}

	public int getRow() {
		return row;
	}

	public Set<FieldValidation> getViolations() {
		return violations;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RowValidation)) return false;
		RowValidation that = (RowValidation) o;
		return row == that.row &&
				Objects.equals(violations, that.violations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, violations);
	}

	@Override
	public String toString() {
		return "RowValidation{" +
				"row=" + row +
				", violations=" + violations +
				'}';
	}

	/**
	 * Compare by row number
	 * @param other other bean to compare to
	 * @return -1, 0 or 1
	 */
	@Override
	public int compareTo(RowValidation other){
		return Integer.compare(this.getRow(), other.getRow());
	}
}

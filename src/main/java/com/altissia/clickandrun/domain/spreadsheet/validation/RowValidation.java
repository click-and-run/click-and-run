package com.altissia.clickandrun.domain.spreadsheet.validation;

import javax.validation.ConstraintViolation;
import java.util.Objects;

/**
 * Row validation, describe a set of constraint violation for a specific row.
 */
public class RowValidation extends FieldValidation implements Comparable<RowValidation> {

    private int row;

    public RowValidation(int row, String field, String value, String violation) {
        super(field, value, violation);
        this.row = row;
    }

    public RowValidation(ConstraintViolation<?> violation, int row) {
        super(violation);
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RowValidation)) return false;
        if (!super.equals(o)) return false;
        RowValidation that = (RowValidation) o;
        return getRow() == that.getRow();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRow());
    }

    @Override
    public String toString() {
        return "RowValidation{" +
            "row=" + row +
            "field='" + getField() + '\'' +
            ", value='" + getValue() + '\'' +
            ", violation='" + getViolation() + '\'' +
            '}';
    }

    /**
     * Compare by row number
     *
     * @param other other bean to compare to
     * @return -1, 0 or 1
     */
    @Override
    public int compareTo(RowValidation other) {
        int compare = Integer.compare(this.getRow(), other.getRow());
        if (compare == 0) {
            compare = super.compareTo(other);
        }
        return compare;
    }
}

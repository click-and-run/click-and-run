package com.altissia.clickandrun.domain.spreadsheet.validation;

import javax.validation.ConstraintViolation;
import java.util.Objects;

public class HeaderValidation extends FieldValidation {

    private int column;

    public HeaderValidation(int column, ConstraintViolation<?> violation) {
        super(violation);
        this.column = column;
    }

    public HeaderValidation(int column, String field, String value, String violation) {
        super(field, value, violation);
        this.column = column;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeaderValidation)) return false;
        if (!super.equals(o)) return false;
        HeaderValidation that = (HeaderValidation) o;
        return column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), column);
    }

    @Override
    public String toString() {
        return "HeaderValidation{" +
            "column=" + column +
            ", field='" + super.getField() + '\'' +
            ", value='" + super.getValue() + '\'' +
            ", violation='" + super.getViolation() + '\'' +
            '}';
    }

    @Override
    public int compareTo(FieldValidation other) {
        int compare = 0;
        if (other instanceof HeaderValidation) {
            HeaderValidation otherHeader = (HeaderValidation) other;
            compare = Integer.compare(this.column, otherHeader.column);
        }
        if (compare == 0) {
            compare = super.compareTo(other);
        }
        return compare;
    }
}

package com.altissia.clickandrun.domain.spreadsheet.validation;

import javax.validation.ConstraintViolation;
import java.util.Comparator;
import java.util.Objects;

/**
 * Field validation, describe a constraint violation for a specific field.
 * It is used for both header fields and default row fields.
 */
public class FieldValidation {

    private static Comparator<String> comparator = Comparator.nullsFirst(String::compareTo);

	private String field;

	private String value;

	private String violation;

	public FieldValidation(ConstraintViolation<?> violation) {
		this.field = violation.getPropertyPath().toString();
		this.value = violation.getInvalidValue() == null ? "null" : violation.getInvalidValue().toString();
		this.violation = violation.getMessageTemplate();
		// Remove "{}"
		this.violation = this.violation.substring(1, this.violation.length()-1);
	}

	public FieldValidation(String field, String value, String violation) {
		this.field = field;
		this.value = value;
		this.violation = violation;
	}

	public String getField() {
		return field;
	}

	public String getValue() {
		return value;
	}

	public String getViolation() {
		return violation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FieldValidation)) return false;
		FieldValidation that = (FieldValidation) o;
		return Objects.equals(field, that.field) &&
				Objects.equals(value, that.value) &&
				Objects.equals(violation, that.violation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(field, value, violation);
	}

	@Override
	public String toString() {
		return "FieldValidation{" +
				"field='" + field + '\'' +
				", value='" + value + '\'' +
				", violation='" + violation + '\'' +
				'}';
	}

	/**
	 * Compare by field name and violation
	 * @param other other bean to compare to
	 * @return -1, 0 or 1
	 */
	public int compareTo(FieldValidation other) {
        int compare = comparator.compare(this.getField(), other.getField());
        if (compare == 0) {
            compare = comparator.compare(this.getViolation(), other.getViolation());
        }
        if (compare == 0) {
            compare = comparator.compare(this.getValue(), other.getValue());
        }
		return compare;
	}
}

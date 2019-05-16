package com.altissia.clickandrun.service.extended.validator;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface to define a post-validator for business / functional validation in a specific sheet
 */
public abstract class SheetValidator<T extends Row> {

	protected static final Logger log = LoggerFactory.getLogger(SheetValidator.class);

	/**
	 * Assert if this validator is suitable for a particular sheet
	 * @param sheet the sheet to check
	 * @return true if this validator is suitable for the provided sheet
	 */
	public abstract boolean isApplicableTo(Sheet<? extends Row> sheet);

	/**
	 * Actual validation process
	 * @param sheet the sheet to add error / warning to
	 * @return the number of error/warning issued by this validator
	 */
	public abstract long validate(Sheet<T> sheet);

}

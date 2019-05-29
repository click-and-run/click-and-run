package com.altissia.clickandrun.service.extended.validator;

import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Interface to define a post-validator for business / functional validation in the whole workbook
 */
public abstract class WorkbookValidator {

	protected static final Logger log = LoggerFactory.getLogger(WorkbookValidator.class);
	
	/**
	 * Assert if this validator is suitable for a particular workbook
	 * @param definition the definition to check
	 * @return true if this validator is suitable for the provided workbook
	 */
	public abstract boolean isApplicableTo(Workbook definition);
	
	/**
	 * Actual validation process
	 * @param workbook the workbook to add error / warning to
	 * @return the number of error/warning issued by this validator
	 */
	public abstract long validate(Workbook workbook);

}

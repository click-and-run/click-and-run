package com.altissia.clickandrun.domain.spreadsheet;

public abstract class Row {

	// Note:
	// For a strange reason, this annotation is not properly handled
	// by Poiji for the first sheet.
	// Re-implement it systematically in the row model implementation instead.
	//@ExcelRow
	//private int row;

	/**
	 * @return the row index (which is zero-based)
	 */
	public abstract int getRow();
}

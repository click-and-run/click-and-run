package com.altissia.clickandrun.service.extended.processor;

import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Processor<T extends ProcessorResult, V extends ProcessorOptions> {

    protected static final Logger log = LoggerFactory.getLogger(Processor.class);

    /**
     * Assert if this processor is suitable for a particular workbook
     * @param workbook the workbook to process
     * @return true if this processor is suitable for the provided workbook
     */
    public abstract boolean isApplicableTo(Workbook workbook);

    public abstract T process(Workbook workbook, V options);

}

package com.altissia.clickandrun.domain.spreadsheet.concrete;

import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.Workbook;

public class LAQuestionWB extends Workbook {

    public static final String SHEET_QUESTIONS = "QUESTIONS";

    public LAQuestionWB() {
        this.sheets.put(SHEET_QUESTIONS, new QuestionsSheet());
    }

    private class QuestionsSheet extends Sheet {
        QuestionsSheet() {
            super(SHEET_QUESTIONS, LAQuestionRow.class);
        }
    }
}

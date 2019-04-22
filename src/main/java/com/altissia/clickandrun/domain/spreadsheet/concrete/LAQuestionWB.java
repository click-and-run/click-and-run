package com.altissia.clickandrun.domain.spreadsheet.concrete;

import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.Workbook;

public class LAQuestionWB extends Workbook {

    public static final String SHEET_QUESTIONS = "questions";

    public LAQuestionWB() {
        this.sheets.put(SHEET_QUESTIONS, new QuestionsSheet());
    }

    public class QuestionsSheet extends Sheet<LAQuestionRow> {
        QuestionsSheet() {
            super(SHEET_QUESTIONS, LAQuestionRow.class);
        }
    }
}

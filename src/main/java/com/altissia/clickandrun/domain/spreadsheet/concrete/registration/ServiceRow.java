package com.altissia.clickandrun.domain.spreadsheet.concrete.registration;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class ServiceRow extends Row {

    @ExcelRow
    private int row;

    @ExcelCellName("Email")
    @Email
    @NotNull
    private String login;

    // todo Test membership of ServiceEnum https://github.com/click-and-run/click-and-run/issues/4
    @ExcelCellName("License")
    @NotNull
    private String service;

    // todo Test membership of LanguageEnum with type = study https://github.com/click-and-run/click-and-run/issues/4
    @ExcelCellName("Language")
    @NotNull
    private String studyLanguage;

    @ExcelCellName("Duration")
    @Min(0)
    @Max(365)
    private int duration;

    public int getRow() {
        return row;
    }

    public String getLogin() {
        return login;
    }

    public String getService() {
        return service;
    }

    public String getStudyLanguage() {
        return studyLanguage;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceRow)) return false;
        ServiceRow that = (ServiceRow) o;
        return getLogin().equals(that.getLogin()) &&
            getService().equals(that.getService()) &&
            getStudyLanguage().equals(that.getStudyLanguage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin(), getService(), getStudyLanguage());
    }

    @Override
    public String toString() {
        return "ServiceRow{" +
            "row=" + row +
            ", login='" + login + '\'' +
            ", service='" + service + '\'' +
            ", studyLanguage='" + studyLanguage + '\'' +
            ", duration=" + duration +
            '}';
    }
}

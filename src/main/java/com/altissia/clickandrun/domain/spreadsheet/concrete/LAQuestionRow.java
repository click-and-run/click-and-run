package com.altissia.clickandrun.domain.spreadsheet.concrete;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class LAQuestionRow extends Row {

    @ExcelRow
    private int row;

    @ExcelCellName("name")
    @Pattern(regexp = "\\w+(?: \\w+)*")
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LAQuestionRow)) return false;
        LAQuestionRow that = (LAQuestionRow) o;
        return getRow() == that.getRow() &&
            name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), name);
    }

    @Override
    public String toString() {
        return "LAQuestionRow{" +
            "row=" + getRow() +
            ",name='" + name + "\'" +
            "}";
    }

    @Override
    public int getRow() {
        return row;
    }
}

package com.altissia.clickandrun.domain.spreadsheet.concrete.registration;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.altissia.clickandrun.service.extended.validator.common.InterfaceLanguage;
import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class RegistrantRow extends Row {

    @ExcelRow
    private int row;

    @ExcelCellName("First Name")
    @Length(max = 50)
    @Pattern(regexp = "\\p{Lu}\\p{Ll}+")
    private String firstName;

    @ExcelCellName("Last Name")
    @NotNull
    @Length(min = 2, max = 100)
    @Pattern(regexp = "\\p{Lu}\\p{Ll}+")
    private String lastName;

    @ExcelCellName("Email")
    @Email
    @NotNull
    private String login;

    @ExcelCellName("Interface Language")
    @InterfaceLanguage
    private String interfaceLanguage;

    @Override
    public int getRow() {
        return row;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLogin() {
        return login;
    }

    public String getInterfaceLanguage() {
        return interfaceLanguage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistrantRow)) return false;
        RegistrantRow that = (RegistrantRow) o;
        return Objects.equals(getFirstName(), that.getFirstName()) &&
            getLastName().equals(that.getLastName()) &&
            getLogin().equals(that.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getLogin());
    }

    @Override
    public String toString() {
        return "RegistrantRow{" +
            "row=" + row +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", login='" + login + '\'' +
            ", interfaceLanguage='" + interfaceLanguage + '\'' +
            '}';
    }
}

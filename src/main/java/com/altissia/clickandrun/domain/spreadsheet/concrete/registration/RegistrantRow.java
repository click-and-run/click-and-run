package com.altissia.clickandrun.domain.spreadsheet.concrete.registration;

import com.altissia.clickandrun.domain.spreadsheet.Row;
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

    // todo fancy regex forbid special characters or sequential non letter
    // todo max length (careful as it is optional)
    @ExcelCellName("First Name")
    @Length(min = 2, max = 50)
    @Pattern(regexp = "\\p{Lu}\\p{Ll}+")
    private String firstName;

    // todo min and max length
    // todo fancy regex forbid special characters or sequential non letter
    @ExcelCellName("Last Name")
    @NotNull
    @Length(min = 2, max = 100)
    private String lastName;

    @ExcelCellName("Email")
    @Email
    @NotNull
    private String login;

    // todo Test membership of LanguageEnum with type = interface
    @ExcelCellName("Interface Language")
    @NotNull
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
}

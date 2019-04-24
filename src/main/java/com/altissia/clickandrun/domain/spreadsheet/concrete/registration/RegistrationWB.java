package com.altissia.clickandrun.domain.spreadsheet.concrete.registration;

import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.Workbook;

public class RegistrationWB extends Workbook {

    public static final String SHEET_REGISTRANTS = "registrants";
    public static final String SHEET_SERVICES = "services";

    public RegistrationWB() {
        this.sheets.put(SHEET_REGISTRANTS, new RegistrantsSheet());
        this.sheets.put(SHEET_SERVICES, new ServicesSheet());
    }

    public class RegistrantsSheet extends Sheet<RegistrantRow> {
        RegistrantsSheet() {
            super(SHEET_REGISTRANTS, RegistrantRow.class);
        }
    }

    public class ServicesSheet extends Sheet<ServiceRow> {
        ServicesSheet() {
            super(SHEET_SERVICES, ServiceRow.class);
        }
    }
}

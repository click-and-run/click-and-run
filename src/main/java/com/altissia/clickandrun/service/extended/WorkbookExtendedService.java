package com.altissia.clickandrun.service.extended;

import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import com.altissia.clickandrun.domain.spreadsheet.validation.FieldValidation;
import com.google.common.collect.Sets;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class WorkbookExtendedService {

    private static final Logger log = LoggerFactory.getLogger(WorkbookExtendedService.class);

    public WorkbookExtendedService() {

    }

    public Workbook validateWorkbook(MultipartFile file, Workbook workbook) {

        log.debug("Validating workbook headers");

        XSSFWorkbook poiWorkbook = this.openWorkbook(file);

        workbook.getSheets().forEach(sheet -> {
            List<String> expectedHeaders = sheet.getHeaders();
            log.debug("Validating header for sheet {}: Expected headers: {}", sheet.getName(), expectedHeaders);

            // Read all cell of the first row and check if they match an expected header
            XSSFSheet poiSheet = poiWorkbook.getSheet(sheet.getName());
            XSSFRow header = poiSheet.getRow(0);

            if (header == null || header.getPhysicalNumberOfCells() == 0) {
                sheet.addHeaderError(new FieldValidation("header", "", "com.altissia.constraints.header.empty"));
            } else {
                Set<String> headerStringCellValues = StreamSupport.stream(header.spliterator(), false).map(Cell::getStringCellValue).collect(Collectors.toSet());

                if (! workbook.isIgnoringSuperfluousHeaders()) {
                    Sets.SetView<String> invalids = Sets.difference(headerStringCellValues, Sets.newHashSet(expectedHeaders));
                    invalids.forEach(h -> sheet.addHeaderError(new FieldValidation("header", h, "com.altissia.constraints.header.invalid")));
                }

                Sets.SetView<String> omissions = Sets.difference(Sets.newHashSet(expectedHeaders), headerStringCellValues);
                omissions.forEach(h -> sheet.addHeaderError(new FieldValidation("header", h, "com.altissia.constraints.header.missing")));
            }
        });

        try {
            poiWorkbook.close();
        } catch (IOException e) {
            log.error("Unable to close workbook", e);
        }

        return workbook;
    }

    private XSSFWorkbook openWorkbook(MultipartFile file) {
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            log.error("Unable to read file", e);
            throw new IllegalArgumentException("clickandrun.error.validation.file.unreadable");
        } catch (POIXMLException e) {
            log.error("Unable to read XLSX", e);
            throw new IllegalArgumentException("clickandrun.error.validation.file.wrongFormat");
        }

        return workbook;
    }
}

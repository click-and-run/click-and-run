package com.altissia.clickandrun.service.extended;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import com.altissia.clickandrun.domain.spreadsheet.validation.FieldValidation;
import com.altissia.clickandrun.domain.spreadsheet.validation.RowValidation;
import com.altissia.clickandrun.service.extended.validator.SheetValidator;
import com.google.common.collect.Sets;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class WorkbookExtendedService {

    private static final Logger log = LoggerFactory.getLogger(WorkbookExtendedService.class);

    private final List<SheetValidator> sheetValidators;

    public WorkbookExtendedService(List<SheetValidator> sheetValidators) {

        this.sheetValidators = sheetValidators;
    }

    public Workbook validateWorkbook(MultipartFile file, Workbook workbook) {

        log.debug("Validating workbook: {}", file.getOriginalFilename());

        validateHeaders(file, workbook);

        readWorkbook(file, workbook);


        workbook.getSheets().forEach(sheet -> {
            if (!sheet.isValid()) {
                log.error("Headers are not valid for sheet {}, skipping row and sheet validation", sheet.getName());
                return;
            }

            validateRows(sheet);

            validateSheet(sheet);
        });

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

    private Workbook readWorkbook(MultipartFile file, Workbook definition) {

        log.debug("Reading workbook {} content", file.getOriginalFilename());

        definition.getSheets().forEach(sheet -> {
            try {
                log.debug("Reading sheet {}", sheet.getName());

                PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings()
                    .sheetName(sheet.getName())
                    .build();

                List<? extends Row> rows = Poiji.fromExcel(file.getInputStream(), PoijiExcelType.XLSX, sheet.getModel(), options);

                log.debug("{} rows found", rows.size());
                if (log.isTraceEnabled()) {
                    rows.forEach(row -> log.trace("Row {}: {}", row.getRow(), row));
                }

                sheet.setRows(rows);
            } catch (IOException e) {
                log.error("Unable to read file", e);
                throw new IllegalArgumentException("clickandrun.error.validation.file.unreadable");
            }
        });

        return definition;
    }

    private Workbook validateHeaders(MultipartFile file, Workbook workbook) {

        log.debug("Validating workbook headers");

        XSSFWorkbook poiWorkbook = this.openWorkbook(file);

        workbook.getSheets().forEach(sheet -> {
            List<String> expectedHeaders = sheet.getHeaders();
            log.debug("Validating header for sheet {}: Expected headers: {}", sheet.getName(), expectedHeaders);

            // Read all cell of the first row and check if they match an expected header
            XSSFSheet poiSheet = poiWorkbook.getSheet(sheet.getName());
            if (poiSheet == null) {
                sheet.addHeaderError(new FieldValidation("header", "", "com.altissia.constraints.sheet.missing"));
                return;
            }

            XSSFRow header = poiSheet.getRow(0);

            if (header == null || header.getPhysicalNumberOfCells() == 0) {
                sheet.addHeaderError(new FieldValidation("header", "", "com.altissia.constraints.header.empty"));
            } else {
                Set<String> headerStringCellValues = StreamSupport.stream(header.spliterator(), false).map(Cell::getStringCellValue).collect(Collectors.toSet());

                if (!workbook.isIgnoringSuperfluousHeaders()) {
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

    private <T extends Row> void validateRows(Sheet<T> sheet) {
        log.debug("Validating {} rows of sheet {}", sheet.getRows().size(), sheet.getName());

        // Get validator
        ValidatorFactory beanValidatorFactory = Validation.buildDefaultValidatorFactory();
        Validator beanValidator = beanValidatorFactory.getValidator();

        long beanValidationErrors = sheet.getRows().stream()
            .mapToLong(row -> {
                List<FieldValidation> rowViolations = beanValidator.validate(row)
                    .stream()
                    .map(FieldValidation::new)
                    .collect(Collectors.toList());

                if (!rowViolations.isEmpty()) {
                    log.debug("{} error found during row {} validation", rowViolations.size(), row.getRow());
                    sheet.addRowError(new RowValidation(row.getRow(), rowViolations));
                }

                return rowViolations.size();
            })
            .sum();

        if (beanValidationErrors > 0) {
            log.debug("{} issues found during bean-validation", beanValidationErrors);
        } else {
            log.debug("No issues found during bean-validation");
        }
    }

    private <T extends Row> void validateSheet(Sheet<T> sheet) {
        log.debug("Post validation of sheet {}", sheet.getName());

        if (log.isTraceEnabled()) {
            log.trace("Applying suitable validator out of:");
            this.sheetValidators.forEach(v -> log.trace(" * {}", v.getClass().getSimpleName()));
        }

        long postValidationIssues = this.sheetValidators.stream()
            .filter(validator -> validator.isApplicableTo(sheet))
            .mapToLong(postValidator -> {

                log.debug("Validating sheet using {}", postValidator.getClass().getSimpleName());

                long issues = postValidator.validate(sheet);

                if (issues > 0) {
                    log.debug("{} error found during post-validation using {}", issues, postValidator.getClass().getSimpleName());
                } else {
                    log.debug("No error found during post-validation using {}", postValidator.getClass().getSimpleName());
                }

                return issues;
            })
            .sum();

        if (postValidationIssues > 0) {
            log.debug("{} issues found during post-validation", postValidationIssues);
        } else {
            log.debug("No issues found during post-validation");
        }
    }
}

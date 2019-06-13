package com.altissia.clickandrun.service.extended;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import com.altissia.clickandrun.domain.spreadsheet.validation.HeaderValidation;
import com.altissia.clickandrun.domain.spreadsheet.validation.RowValidation;
import com.altissia.clickandrun.service.extended.processor.Processor;
import com.altissia.clickandrun.service.extended.processor.ProcessorOptions;
import com.altissia.clickandrun.service.extended.processor.ProcessorResult;
import com.altissia.clickandrun.service.extended.validator.SheetValidator;
import com.altissia.clickandrun.service.extended.validator.WorkbookValidator;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class WorkbookExtendedService {

    private static final Logger log = LoggerFactory.getLogger(WorkbookExtendedService.class);

    private final List<SheetValidator> sheetValidators;

    private final List<WorkbookValidator> workbookValidators;

    private final List<Processor> processors;

    public WorkbookExtendedService(List<SheetValidator> sheetValidators, List<WorkbookValidator> workbookValidators, List<Processor> processors) {
        this.sheetValidators = sheetValidators;
        this.workbookValidators = workbookValidators;
        this.processors = processors;
    }

    public Workbook validateWorkbook(MultipartFile file, Workbook workbook) {

        log.debug("Validating workbook: {}", file.getOriginalFilename());

        validateHeaders(file, workbook);

        readWorkbook(file, workbook);

        workbook.getSheets().forEach(sheet -> {
            if (!sheet.isHeaderValid()) {
                log.error("Headers are not valid for sheet {}, skipping row and sheet validation", sheet.getName());
                return;
            }

            validateRows(sheet);

            validateSheet(sheet);
        });

        if (workbook.getSheets().stream().allMatch(Sheet::isHeaderValid)) {
            validateWorkbook(workbook);
        }

        return workbook;
    }

    public ProcessorResult processWorkbook(MultipartFile file, Workbook workbook) {
        return this.processWorkbook(file, workbook, new ProcessorOptions());
    }

    public ProcessorResult processWorkbook(MultipartFile file, Workbook workbook, ProcessorOptions processorOptions) {
        this.validateWorkbook(file, workbook);

        // If no *error* has been found then process (maybe with warnings)
        if (workbook.isValid()) {

            if (log.isDebugEnabled()) {
                log.debug("Applying suitable processor out of:");
                this.processors.forEach(p -> log.debug(" * {}", p.getClass().getSimpleName()));
            }

            Processor processor = this.processors.stream()
                .filter(p -> p.isApplicableTo(workbook))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No processor found for workbook " + workbook.getClass().getSimpleName()));

            log.debug("Processor found: {}", processor.getClass().getSimpleName());

            return processor.process(workbook, processorOptions);
        }

        log.error("Skipping processor execution due to error found in the workbook");
        if (log.isDebugEnabled()) {
            workbook.getValidations().forEach((sheet, validations) -> {
                log.debug("Validation detail for sheet {}", sheet);
                log.debug(" * {} header error found ", validations.getHeaders().size());
                log.debug(" * {} error found ", validations.getErrors().size());
                log.debug(" * {} warning found ", validations.getWarnings().size());
            });
        }

        throw new IllegalArgumentException("com.altissia.clickandrun.workbook.invalid");
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
                sheet.addHeaderError(new HeaderValidation(-1, "sheet", "", "com.altissia.constraints.sheet.missing"));
                return;
            }

            XSSFRow headerRow = poiSheet.getRow(0);

            Map<String, Boolean> headersFound = expectedHeaders.stream().collect(Collectors.toMap(Function.identity(), x -> false));

            if (headerRow != null && headerRow.getPhysicalNumberOfCells() > 0) {
                for (Cell cell : headerRow) {
                    String value = cell.getStringCellValue();
                    int column = cell.getColumnIndex();

                    // '\u00A0' is non-breaking space, useful because Excel replace multiple space with alternating space and non-breaking space
                    if (!workbook.isIgnoringSuperfluousHeaders() && StringUtils.isBlank(value.replace('\u00A0', ' '))) {
                        sheet.addHeaderError(new HeaderValidation(column, "header", value, "com.altissia.constraints.header.blank"));
                    } else if (!workbook.isIgnoringSuperfluousHeaders() && !expectedHeaders.contains(value)) {
                        sheet.addHeaderError(new HeaderValidation(column, "header", value, "com.altissia.constraints.header.invalid"));
                    } else if (headersFound.containsKey(value) && headersFound.get(value)) {
                        sheet.addHeaderError(new HeaderValidation(column, "header", value, "com.altissia.constraints.header.duplicate"));
                    } else {
                        headersFound.put(value, true);
                    }
                }

            }

            headersFound.forEach((header, found) -> {
                if (!found) {
                    sheet.addHeaderError(new HeaderValidation(-1, "header", header, "com.altissia.constraints.header.missing"));
                }
            });
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
                Set<ConstraintViolation<T>> violations = beanValidator.validate(row);

                if (!violations.isEmpty()) {
                    log.debug("{} error found during row {} validation", violations.size(), row.getRow());
                    violations.forEach(violation -> sheet.addRowError(new RowValidation(violation, row.getRow())));
                }

                return violations.size();
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
            log.trace("Applying suitable sheet validator out of:");
            this.sheetValidators.forEach(v -> log.trace(" * {}", v.getClass().getSimpleName()));
        }

        long postValidationIssues = this.sheetValidators.stream()
            .filter(validator -> validator.isApplicableTo(sheet))
            .mapToLong(postValidator -> {

                log.debug("Validating sheet using {}", postValidator.getClass().getSimpleName());

                long issues = postValidator.validate(sheet);

                if (issues > 0) {
                    log.debug("{} error found during sheet post-validation using {}", issues, postValidator.getClass().getSimpleName());
                } else {
                    log.debug("No error found during sheet post-validation using {}", postValidator.getClass().getSimpleName());
                }

                return issues;
            })
            .sum();

        if (postValidationIssues > 0) {
            log.debug("{} issues found during sheet post-validation", postValidationIssues);
        } else {
            log.debug("No issues found during sheet post-validation");
        }
    }

    private void validateWorkbook(Workbook workbook) {
        log.debug("Post validation of workbook {}", workbook.getClass().getSimpleName());

        if (log.isTraceEnabled()) {
            log.trace("Applying suitable workbook validator out of:");
            this.workbookValidators.forEach(v -> log.trace(" * {}", v.getClass().getSimpleName()));
        }

        long postValidationIssues = this.workbookValidators.stream()
            .filter(validator -> validator.isApplicableTo(workbook))
            .mapToLong(postValidator -> {

                log.debug("Validating workbook using {}", postValidator.getClass().getSimpleName());

                long issues = postValidator.validate(workbook);

                if (issues > 0) {
                    log.debug("{} error found during workbook post-validation using {}", issues, postValidator.getClass().getSimpleName());
                } else {
                    log.debug("No error found during workbook post-validation using {}", postValidator.getClass().getSimpleName());
                }

                return issues;
            }).sum();

        if (postValidationIssues > 0) {
            log.debug("{} issues found during workbook post-validation", postValidationIssues);
        } else {
            log.debug("No issues found during workbook post-validation");
        }
    }
}

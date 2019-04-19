package com.altissia.clickandrun.web.rest.extended;

import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import com.altissia.clickandrun.domain.spreadsheet.concrete.LAQuestionWB;
import com.altissia.clickandrun.domain.spreadsheet.validation.SheetValidation;
import com.altissia.clickandrun.service.extended.WorkbookExtendedService;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/la-questions")
public class LAQuestionResource {

    private final Logger log = LoggerFactory.getLogger(LAQuestionResource.class);

    private final WorkbookExtendedService workbookExtendedService;

    public LAQuestionResource(WorkbookExtendedService workbookExtendedService) {
        this.workbookExtendedService = workbookExtendedService;
    }

    @Timed
    @PostMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, SheetValidation>> validateFile(@RequestParam(name = "file") MultipartFile file) {
        log.debug("REST request to /la-questions/validate with {}", file.getOriginalFilename());

        Workbook workbook = workbookExtendedService.validateWorkbook(file, new LAQuestionWB());
        return ResponseEntity.ok(workbook.getValidations());
    }
}

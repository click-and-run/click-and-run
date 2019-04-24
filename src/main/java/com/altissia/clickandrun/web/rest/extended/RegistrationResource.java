package com.altissia.clickandrun.web.rest.extended;


import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWB;
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
@RequestMapping("/registration")
public class RegistrationResource {

    private final Logger log = LoggerFactory.getLogger(RegistrationResource.class);

    private final WorkbookExtendedService workbookExtendedService;

    public RegistrationResource(WorkbookExtendedService workbookExtendedService) {
        this.workbookExtendedService = workbookExtendedService;
    }

    @Timed
    @PostMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, SheetValidation>> validateFile(@RequestParam(name = "file") MultipartFile file) {
        log.debug("REST request to /registration/validate with {}", file.getOriginalFilename());

        Workbook workbook;

        // todo ExceptionTranslator rather than try catch https://github.com/click-and-run/click-and-run/issues/6
        try {
            workbook = workbookExtendedService.validateWorkbook(file, new RegistrationWB());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().header("click-and-run-error", e.getMessage()).build();
        }

        return ResponseEntity.ok(workbook.getValidations());
    }
}

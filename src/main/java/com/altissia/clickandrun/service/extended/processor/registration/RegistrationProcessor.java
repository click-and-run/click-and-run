package com.altissia.clickandrun.service.extended.processor.registration;

import com.altissia.clickandrun.domain.Learner;
import com.altissia.clickandrun.domain.License;
import com.altissia.clickandrun.domain.enumeration.Language;
import com.altissia.clickandrun.domain.enumeration.Service;
import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrantRow;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWorkbook;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.ServiceRow;
import com.altissia.clickandrun.repository.LearnerRepository;
import com.altissia.clickandrun.repository.LicenseRepository;
import com.altissia.clickandrun.service.extended.processor.Processor;
import com.altissia.clickandrun.service.extended.processor.ProcessorOptions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWorkbook.SHEET_REGISTRANTS;
import static com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWorkbook.SHEET_SERVICES;

@org.springframework.stereotype.Service
public class RegistrationProcessor extends Processor<RegistrationResult, ProcessorOptions> {

    private final LearnerRepository learnerRepository;
    private final LicenseRepository licenseRepository;

    public RegistrationProcessor(LearnerRepository learnerRepository, LicenseRepository licenseRepository) {
        this.learnerRepository = learnerRepository;
        this.licenseRepository = licenseRepository;
    }

    @Override
    public boolean isApplicableTo(Workbook workbook) {
        return workbook instanceof RegistrationWorkbook;
    }

    @Override
    public RegistrationResult process(Workbook workbook, ProcessorOptions options) {
        ImmutableMap<String, Learner> learnersMap = processRegistrantRows(workbook);
        List<License> licenses = processServiceRows(workbook, learnersMap);

        RegistrationResult registrationResult = new RegistrationResult();
        registrationResult.setLearnerCreated(learnersMap.size());
        registrationResult.setLicenseCreated(licenses.size());

        return registrationResult;
    }

    private ImmutableMap<String, Learner> processRegistrantRows(Workbook workbook) {
        List<RegistrantRow> learnerRows = workbook.getSheetRows(SHEET_REGISTRANTS);
        List<Learner> learners = learnerRows.stream()
            .map(registrantRow -> {
                Learner learner = new Learner();
                learner.setFirstName(registrantRow.getFirstName());
                learner.setLastName(registrantRow.getLastName());
                learner.setLogin(registrantRow.getLogin());
                learner.setInterfaceLanguage(Language.valueOf(registrantRow.getInterfaceLanguage()));

                return learner;
            }).collect(Collectors.toList());
        learnerRepository.save(learners);

        return Maps.uniqueIndex(learners, Learner::getLogin);
    }

    private List<License> processServiceRows(Workbook workbook, ImmutableMap<String, Learner> learnersMap) {
        List<ServiceRow> serviceRows = workbook.getSheetRows(SHEET_SERVICES);

        List<License> licenses = serviceRows.stream()
            .map(serviceRow -> {
                License license = new License();
                license.setService(Service.valueOf(serviceRow.getService()));
                license.setStudyLanguage(Language.valueOf(serviceRow.getStudyLanguage()));
                license.setValidSince(Instant.now());
                license.setValidUntil(Instant.now().plus(serviceRow.getDuration(), ChronoUnit.DAYS));
                license.setLearner(learnersMap.get(serviceRow.getLogin()));

                return license;
            })
            .collect(Collectors.toList());

        licenseRepository.save(licenses);
        return licenses;
    }
}

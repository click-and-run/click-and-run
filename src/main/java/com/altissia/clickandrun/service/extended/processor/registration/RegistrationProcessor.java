package com.altissia.clickandrun.service.extended.processor.registration;

import com.altissia.clickandrun.domain.Learner;
import com.altissia.clickandrun.domain.License;
import com.altissia.clickandrun.domain.enumeration.Language;
import com.altissia.clickandrun.domain.enumeration.Service;
import com.altissia.clickandrun.domain.spreadsheet.Workbook;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrantRow;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWB;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.ServiceRow;
import com.altissia.clickandrun.repository.LearnerRepository;
import com.altissia.clickandrun.service.extended.processor.Processor;
import com.altissia.clickandrun.service.extended.processor.ProcessorOptions;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class RegistrationProcessor extends Processor<RegistrationResult, ProcessorOptions> {

    private final LearnerRepository learnerRepository;

    public RegistrationProcessor(LearnerRepository learnerRepository) {
        this.learnerRepository = learnerRepository;
    }

    @Override
    public boolean isApplicableTo(Workbook workbook) {
        return workbook instanceof RegistrationWB;
    }

    @Override
    public RegistrationResult process(Workbook workbook, ProcessorOptions options) {
        List<ServiceRow> serviceRows = (List<ServiceRow>) workbook.getSheetRows(RegistrationWB.SHEET_SERVICES);
        Map<String, List<License>> licenseByLogin = new HashMap<>();

        serviceRows.forEach(serviceRow -> {
            License license = new License();
            license.setService(Service.valueOf(serviceRow.getService()));
            license.setStudyLanguage(Language.valueOf(serviceRow.getStudyLanguage()));
            license.setValidSince(Instant.now());
            license.setValidUntil(Instant.now().plus(serviceRow.getDuration(), ChronoUnit.DAYS));

            if (licenseByLogin.containsKey(serviceRow.getLogin())) {
                licenseByLogin.get(serviceRow.getLogin()).add(license);
            } else {
                licenseByLogin.put(serviceRow.getLogin(), new ArrayList<License>(){{
                    add(license);
                }});
            }
        });

        List<RegistrantRow> learnerRows = (List<RegistrantRow>) workbook.getSheetRows(RegistrationWB.SHEET_REGISTRANTS);
        List<Learner> learners = learnerRows.stream()
            .map(registrantRow -> {
                Learner learner = new Learner();
                learner.setFirstName(registrantRow.getFirstName());
                learner.setLastName(registrantRow.getLastName());
                learner.setLogin(registrantRow.getLogin());
                learner.setInterfaceLanguage(Language.valueOf(registrantRow.getInterfaceLanguage()));
                learner.setLicenses(licenseByLogin.get(learner.getLogin()));
                return learner;
            }).collect(Collectors.toList());


        learnerRepository.save(learners);

        RegistrationResult registrationResult = new RegistrationResult();
        registrationResult.setLearnerCreated(learners.size());
        registrationResult.setLicenseCreated(serviceRows.size());

        return registrationResult;
    }
}

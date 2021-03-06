package com.altissia.clickandrun.service.extended.validator.registration;

import com.altissia.clickandrun.domain.spreadsheet.Row;
import com.altissia.clickandrun.domain.spreadsheet.Sheet;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrantRow;
import com.altissia.clickandrun.domain.spreadsheet.concrete.registration.RegistrationWorkbook;
import com.altissia.clickandrun.domain.spreadsheet.validation.RowValidation;
import com.altissia.clickandrun.repository.LearnerRepository;
import com.altissia.clickandrun.service.extended.validator.SheetValidator;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class LoginUnavailableValidator extends SheetValidator<RegistrantRow> {

    private final LearnerRepository learnerRepository;

    public LoginUnavailableValidator(LearnerRepository learnerRepository) {
        this.learnerRepository = learnerRepository;
    }

    @Override
    public boolean isApplicableTo(Sheet<? extends Row> sheet) {
        return sheet instanceof RegistrationWorkbook.RegistrantsSheet;
    }

    @Override
    public long validate(Sheet<RegistrantRow> sheet) {
        ImmutableListMultimap<String, RegistrantRow> rowsByLogin = Multimaps.index(sheet.getRows(), RegistrantRow::getLogin);

        AtomicLong used = new AtomicLong(0);
        learnerRepository.findAllByLoginIn(rowsByLogin.keySet()).forEach(learner -> rowsByLogin.get(learner.getLogin()).forEach(rowWithUsedLogin -> {
            used.getAndIncrement();
            sheet.addRowError(new RowValidation(rowWithUsedLogin.getRow(), "login", rowWithUsedLogin.getLogin(), "com.altissia.constraints.login.unavailable"));
        }));

        return used.get();
    }

}

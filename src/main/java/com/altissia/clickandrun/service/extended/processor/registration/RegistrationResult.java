package com.altissia.clickandrun.service.extended.processor.registration;

import com.altissia.clickandrun.service.extended.processor.ProcessorResult;

import java.util.Objects;

public class RegistrationResult extends ProcessorResult {

    private int learnerCreated;

    private int licenseCreated;

    public int getLearnerCreated() {
        return learnerCreated;
    }

    public void setLearnerCreated(int learnerCreated) {
        this.learnerCreated = learnerCreated;
    }

    public int getLicenseCreated() {
        return licenseCreated;
    }

    public void setLicenseCreated(int licenseCreated) {
        this.licenseCreated = licenseCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegistrationResult)) return false;
        RegistrationResult that = (RegistrationResult) o;
        return getLearnerCreated() == that.getLearnerCreated() &&
            getLicenseCreated() == that.getLicenseCreated();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLearnerCreated(), getLicenseCreated());
    }

    @Override
    public String toString() {
        return "RegistrationResult{" +
            "learnerCreated=" + learnerCreated +
            ", licenseCreated=" + licenseCreated +
            '}';
    }
}

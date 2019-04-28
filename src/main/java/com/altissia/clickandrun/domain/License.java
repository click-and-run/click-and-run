package com.altissia.clickandrun.domain;

import com.altissia.clickandrun.domain.enumeration.Language;
import com.altissia.clickandrun.domain.enumeration.Service;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A License.
 */
@Entity
@Table(name = "license")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class License implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "service", nullable = false)
    private Service service;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "study_language", nullable = false)
    private Language studyLanguage;

    @NotNull
    @Column(name = "valid_since", nullable = false)
    private Instant validSince;

    @NotNull
    @Column(name = "valid_until", nullable = false)
    private Instant validUntil;

    @Column(name = "consumed")
    private Boolean consumed;

    @ManyToOne
    private Learner learner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public License service(Service service) {
        this.service = service;
        return this;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Language getStudyLanguage() {
        return studyLanguage;
    }

    public License studyLanguage(Language studyLanguage) {
        this.studyLanguage = studyLanguage;
        return this;
    }

    public void setStudyLanguage(Language studyLanguage) {
        this.studyLanguage = studyLanguage;
    }

    public Instant getValidSince() {
        return validSince;
    }

    public License validSince(Instant validSince) {
        this.validSince = validSince;
        return this;
    }

    public void setValidSince(Instant validSince) {
        this.validSince = validSince;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public License validUntil(Instant validUntil) {
        this.validUntil = validUntil;
        return this;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean isConsumed() {
        return consumed;
    }

    public License consumed(Boolean consumed) {
        this.consumed = consumed;
        return this;
    }

    public void setConsumed(Boolean consumed) {
        this.consumed = consumed;
    }

    public Learner getLearner() {
        return learner;
    }

    public License learner(Learner learner) {
        this.learner = learner;
        return this;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        License license = (License) o;
        if (license.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), license.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "License{" +
            "id=" + getId() +
            ", service='" + getService() + "'" +
            ", studyLanguage='" + getStudyLanguage() + "'" +
            ", validSince='" + getValidSince() + "'" +
            ", validUntil='" + getValidUntil() + "'" +
            ", consumed='" + isConsumed() + "'" +
            "}";
    }
}

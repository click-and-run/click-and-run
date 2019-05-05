package com.altissia.clickandrun.domain;

import com.altissia.clickandrun.domain.enumeration.Language;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Learner.
 */
@Entity
@Table(name = "learner")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Learner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Pattern(regexp = "\\p{Lu}\\p{Ll}+")
    @Column(name = "first_name", length = 50)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 100)
    @Pattern(regexp = "\\p{Lu}\\p{Ll}+")
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "interface_language", nullable = false)
    private Language interfaceLanguage;

    @OneToMany(mappedBy = "learner")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<License> licenses = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Learner firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Learner lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public Learner login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Language getInterfaceLanguage() {
        return interfaceLanguage;
    }

    public Learner interfaceLanguage(Language interfaceLanguage) {
        this.interfaceLanguage = interfaceLanguage;
        return this;
    }

    public void setInterfaceLanguage(Language interfaceLanguage) {
        this.interfaceLanguage = interfaceLanguage;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public Learner licenses(List<License> licenses) {
        this.licenses = licenses;
        return this;
    }

    public Learner addLicenses(License license) {
        this.licenses.add(license);
        license.setLearner(this);
        return this;
    }

    public Learner removeLicenses(License license) {
        this.licenses.remove(license);
        license.setLearner(null);
        return this;
    }

    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Learner learner = (Learner) o;
        if (learner.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), learner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Learner{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", login='" + getLogin() + "'" +
            ", interfaceLanguage='" + getInterfaceLanguage() + "'" +
            "}";
    }
}

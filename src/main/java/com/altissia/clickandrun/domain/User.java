package com.altissia.clickandrun.domain;

import com.altissia.clickandrun.domain.enumeration.Language;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A User.
 */
@Entity
@Table(name = "user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User implements Serializable {

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public User firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public User lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public User login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Language getInterfaceLanguage() {
        return interfaceLanguage;
    }

    public User interfaceLanguage(Language interfaceLanguage) {
        this.interfaceLanguage = interfaceLanguage;
        return this;
    }

    public void setInterfaceLanguage(Language interfaceLanguage) {
        this.interfaceLanguage = interfaceLanguage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        if (user.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", login='" + getLogin() + "'" +
            ", interfaceLanguage='" + getInterfaceLanguage() + "'" +
            "}";
    }
}

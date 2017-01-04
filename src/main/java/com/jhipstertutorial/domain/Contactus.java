package com.jhipstertutorial.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Contactus.
 */
@Entity
@Table(name = "contactus")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contactus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 4, max = 50)
    @Column(name = "contactname", length = 50, nullable = false)
    private String contactname;

    @NotNull
    @Size(min = 6, max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "message", length = 256, nullable = false)
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactname() {
        return contactname;
    }

    public Contactus contactname(String contactname) {
        this.contactname = contactname;
        return this;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getEmail() {
        return email;
    }

    public Contactus email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public Contactus message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contactus contactus = (Contactus) o;
        if (contactus.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contactus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contactus{" +
            "id=" + id +
            ", contactname='" + contactname + "'" +
            ", email='" + email + "'" +
            ", message='" + message + "'" +
            '}';
    }
}

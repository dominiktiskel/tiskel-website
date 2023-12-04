package com.tiskel.website.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DemoMeeting.
 */
@Entity
@Table(name = "demo_meeting")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DemoMeeting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created")
    private ZonedDateTime created;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DemoMeeting id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public DemoMeeting created(ZonedDateTime created) {
        this.setCreated(created);
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public DemoMeeting date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getEmail() {
        return this.email;
    }

    public DemoMeeting email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemoMeeting)) {
            return false;
        }
        return getId() != null && getId().equals(((DemoMeeting) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemoMeeting{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", date='" + getDate() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}

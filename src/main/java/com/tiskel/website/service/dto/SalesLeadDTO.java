package com.tiskel.website.service.dto;

import com.tiskel.website.domain.enumeration.SalesLeadStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.tiskel.website.domain.SalesLead} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalesLeadDTO implements Serializable {

    private Long id;

    private ZonedDateTime created;

    private String phoneNumber;

    private String email;

    private String note;

    private SalesLeadStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public SalesLeadStatus getStatus() {
        return status;
    }

    public void setStatus(SalesLeadStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalesLeadDTO)) {
            return false;
        }

        SalesLeadDTO salesLeadDTO = (SalesLeadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, salesLeadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalesLeadDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", note='" + getNote() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}

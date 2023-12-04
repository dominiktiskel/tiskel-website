package com.tiskel.website.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.tiskel.website.domain.DemoMeeting} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DemoMeetingDTO implements Serializable {

    private Long id;

    private ZonedDateTime created;

    @NotNull
    private ZonedDateTime date;

    @NotNull
    private String email;

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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemoMeetingDTO)) {
            return false;
        }

        DemoMeetingDTO demoMeetingDTO = (DemoMeetingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, demoMeetingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemoMeetingDTO{" +
            "id=" + getId() +
            ", created='" + getCreated() + "'" +
            ", date='" + getDate() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}

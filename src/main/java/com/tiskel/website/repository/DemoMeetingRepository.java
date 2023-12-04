package com.tiskel.website.repository;

import com.tiskel.website.domain.DemoMeeting;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DemoMeeting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemoMeetingRepository extends JpaRepository<DemoMeeting, Long> {}

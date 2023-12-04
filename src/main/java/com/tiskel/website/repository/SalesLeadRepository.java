package com.tiskel.website.repository;

import com.tiskel.website.domain.SalesLead;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SalesLead entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesLeadRepository extends JpaRepository<SalesLead, Long> {}

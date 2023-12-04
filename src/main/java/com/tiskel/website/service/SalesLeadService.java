package com.tiskel.website.service;

import com.tiskel.website.service.dto.SalesLeadDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.tiskel.website.domain.SalesLead}.
 */
public interface SalesLeadService {
    /**
     * Save a salesLead.
     *
     * @param salesLeadDTO the entity to save.
     * @return the persisted entity.
     */
    SalesLeadDTO save(SalesLeadDTO salesLeadDTO);

    /**
     * Updates a salesLead.
     *
     * @param salesLeadDTO the entity to update.
     * @return the persisted entity.
     */
    SalesLeadDTO update(SalesLeadDTO salesLeadDTO);

    /**
     * Partially updates a salesLead.
     *
     * @param salesLeadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SalesLeadDTO> partialUpdate(SalesLeadDTO salesLeadDTO);

    /**
     * Get all the salesLeads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SalesLeadDTO> findAll(Pageable pageable);

    /**
     * Get the "id" salesLead.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SalesLeadDTO> findOne(Long id);

    /**
     * Delete the "id" salesLead.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.tiskel.website.service;

import com.tiskel.website.service.dto.DemoMeetingDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.tiskel.website.domain.DemoMeeting}.
 */
public interface DemoMeetingService {
    /**
     * Save a demoMeeting.
     *
     * @param demoMeetingDTO the entity to save.
     * @return the persisted entity.
     */
    DemoMeetingDTO save(DemoMeetingDTO demoMeetingDTO);

    /**
     * Updates a demoMeeting.
     *
     * @param demoMeetingDTO the entity to update.
     * @return the persisted entity.
     */
    DemoMeetingDTO update(DemoMeetingDTO demoMeetingDTO);

    /**
     * Partially updates a demoMeeting.
     *
     * @param demoMeetingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DemoMeetingDTO> partialUpdate(DemoMeetingDTO demoMeetingDTO);

    /**
     * Get all the demoMeetings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DemoMeetingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" demoMeeting.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DemoMeetingDTO> findOne(Long id);

    /**
     * Delete the "id" demoMeeting.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.tiskel.website.web.rest;

import com.tiskel.website.repository.DemoMeetingRepository;
import com.tiskel.website.service.DemoMeetingService;
import com.tiskel.website.service.dto.DemoMeetingDTO;
import com.tiskel.website.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tiskel.website.domain.DemoMeeting}.
 */
@RestController
@RequestMapping("/api/demo-meetings")
public class DemoMeetingResource {

    private final Logger log = LoggerFactory.getLogger(DemoMeetingResource.class);

    private static final String ENTITY_NAME = "demoMeeting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemoMeetingService demoMeetingService;

    private final DemoMeetingRepository demoMeetingRepository;

    public DemoMeetingResource(DemoMeetingService demoMeetingService, DemoMeetingRepository demoMeetingRepository) {
        this.demoMeetingService = demoMeetingService;
        this.demoMeetingRepository = demoMeetingRepository;
    }

    /**
     * {@code POST  /demo-meetings} : Create a new demoMeeting.
     *
     * @param demoMeetingDTO the demoMeetingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demoMeetingDTO, or with status {@code 400 (Bad Request)} if the demoMeeting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DemoMeetingDTO> createDemoMeeting(@Valid @RequestBody DemoMeetingDTO demoMeetingDTO) throws URISyntaxException {
        log.debug("REST request to save DemoMeeting : {}", demoMeetingDTO);
        if (demoMeetingDTO.getId() != null) {
            throw new BadRequestAlertException("A new demoMeeting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemoMeetingDTO result = demoMeetingService.save(demoMeetingDTO);
        return ResponseEntity
            .created(new URI("/api/demo-meetings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demo-meetings/:id} : Updates an existing demoMeeting.
     *
     * @param id the id of the demoMeetingDTO to save.
     * @param demoMeetingDTO the demoMeetingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demoMeetingDTO,
     * or with status {@code 400 (Bad Request)} if the demoMeetingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demoMeetingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DemoMeetingDTO> updateDemoMeeting(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DemoMeetingDTO demoMeetingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DemoMeeting : {}, {}", id, demoMeetingDTO);
        if (demoMeetingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demoMeetingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demoMeetingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemoMeetingDTO result = demoMeetingService.update(demoMeetingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demoMeetingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demo-meetings/:id} : Partial updates given fields of an existing demoMeeting, field will ignore if it is null
     *
     * @param id the id of the demoMeetingDTO to save.
     * @param demoMeetingDTO the demoMeetingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demoMeetingDTO,
     * or with status {@code 400 (Bad Request)} if the demoMeetingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the demoMeetingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the demoMeetingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemoMeetingDTO> partialUpdateDemoMeeting(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DemoMeetingDTO demoMeetingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemoMeeting partially : {}, {}", id, demoMeetingDTO);
        if (demoMeetingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demoMeetingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demoMeetingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemoMeetingDTO> result = demoMeetingService.partialUpdate(demoMeetingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demoMeetingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /demo-meetings} : get all the demoMeetings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demoMeetings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DemoMeetingDTO>> getAllDemoMeetings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DemoMeetings");
        Page<DemoMeetingDTO> page = demoMeetingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /demo-meetings/:id} : get the "id" demoMeeting.
     *
     * @param id the id of the demoMeetingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demoMeetingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DemoMeetingDTO> getDemoMeeting(@PathVariable Long id) {
        log.debug("REST request to get DemoMeeting : {}", id);
        Optional<DemoMeetingDTO> demoMeetingDTO = demoMeetingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demoMeetingDTO);
    }

    /**
     * {@code DELETE  /demo-meetings/:id} : delete the "id" demoMeeting.
     *
     * @param id the id of the demoMeetingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemoMeeting(@PathVariable Long id) {
        log.debug("REST request to delete DemoMeeting : {}", id);
        demoMeetingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

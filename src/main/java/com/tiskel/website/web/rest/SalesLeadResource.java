package com.tiskel.website.web.rest;

import com.tiskel.website.repository.SalesLeadRepository;
import com.tiskel.website.service.SalesLeadService;
import com.tiskel.website.service.dto.SalesLeadDTO;
import com.tiskel.website.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.tiskel.website.domain.SalesLead}.
 */
@RestController
@RequestMapping("/api/sales-leads")
public class SalesLeadResource {

    private final Logger log = LoggerFactory.getLogger(SalesLeadResource.class);

    private static final String ENTITY_NAME = "salesLead";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalesLeadService salesLeadService;

    private final SalesLeadRepository salesLeadRepository;

    public SalesLeadResource(SalesLeadService salesLeadService, SalesLeadRepository salesLeadRepository) {
        this.salesLeadService = salesLeadService;
        this.salesLeadRepository = salesLeadRepository;
    }

    /**
     * {@code POST  /sales-leads} : Create a new salesLead.
     *
     * @param salesLeadDTO the salesLeadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salesLeadDTO, or with status {@code 400 (Bad Request)} if the salesLead has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SalesLeadDTO> createSalesLead(@RequestBody SalesLeadDTO salesLeadDTO) throws URISyntaxException {
        log.debug("REST request to save SalesLead : {}", salesLeadDTO);
        if (salesLeadDTO.getId() != null) {
            throw new BadRequestAlertException("A new salesLead cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalesLeadDTO result = salesLeadService.save(salesLeadDTO);
        return ResponseEntity
            .created(new URI("/api/sales-leads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sales-leads/:id} : Updates an existing salesLead.
     *
     * @param id the id of the salesLeadDTO to save.
     * @param salesLeadDTO the salesLeadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salesLeadDTO,
     * or with status {@code 400 (Bad Request)} if the salesLeadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salesLeadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SalesLeadDTO> updateSalesLead(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SalesLeadDTO salesLeadDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SalesLead : {}, {}", id, salesLeadDTO);
        if (salesLeadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salesLeadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salesLeadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SalesLeadDTO result = salesLeadService.update(salesLeadDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salesLeadDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sales-leads/:id} : Partial updates given fields of an existing salesLead, field will ignore if it is null
     *
     * @param id the id of the salesLeadDTO to save.
     * @param salesLeadDTO the salesLeadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salesLeadDTO,
     * or with status {@code 400 (Bad Request)} if the salesLeadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the salesLeadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the salesLeadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SalesLeadDTO> partialUpdateSalesLead(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SalesLeadDTO salesLeadDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SalesLead partially : {}, {}", id, salesLeadDTO);
        if (salesLeadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, salesLeadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!salesLeadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SalesLeadDTO> result = salesLeadService.partialUpdate(salesLeadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salesLeadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sales-leads} : get all the salesLeads.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salesLeads in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SalesLeadDTO>> getAllSalesLeads(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SalesLeads");
        Page<SalesLeadDTO> page = salesLeadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sales-leads/:id} : get the "id" salesLead.
     *
     * @param id the id of the salesLeadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salesLeadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalesLeadDTO> getSalesLead(@PathVariable Long id) {
        log.debug("REST request to get SalesLead : {}", id);
        Optional<SalesLeadDTO> salesLeadDTO = salesLeadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(salesLeadDTO);
    }

    /**
     * {@code DELETE  /sales-leads/:id} : delete the "id" salesLead.
     *
     * @param id the id of the salesLeadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesLead(@PathVariable Long id) {
        log.debug("REST request to delete SalesLead : {}", id);
        salesLeadService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

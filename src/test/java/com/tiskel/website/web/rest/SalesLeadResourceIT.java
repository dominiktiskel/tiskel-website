package com.tiskel.website.web.rest;

import static com.tiskel.website.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tiskel.website.IntegrationTest;
import com.tiskel.website.domain.SalesLead;
import com.tiskel.website.domain.enumeration.SalesLeadStatus;
import com.tiskel.website.repository.SalesLeadRepository;
import com.tiskel.website.service.dto.SalesLeadDTO;
import com.tiskel.website.service.mapper.SalesLeadMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SalesLeadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalesLeadResourceIT {

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final SalesLeadStatus DEFAULT_STATUS = SalesLeadStatus.NEW;
    private static final SalesLeadStatus UPDATED_STATUS = SalesLeadStatus.NEW;

    private static final String ENTITY_API_URL = "/api/sales-leads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalesLeadRepository salesLeadRepository;

    @Autowired
    private SalesLeadMapper salesLeadMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalesLeadMockMvc;

    private SalesLead salesLead;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesLead createEntity(EntityManager em) {
        SalesLead salesLead = new SalesLead()
            .created(DEFAULT_CREATED)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .note(DEFAULT_NOTE)
            .status(DEFAULT_STATUS);
        return salesLead;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesLead createUpdatedEntity(EntityManager em) {
        SalesLead salesLead = new SalesLead()
            .created(UPDATED_CREATED)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .note(UPDATED_NOTE)
            .status(UPDATED_STATUS);
        return salesLead;
    }

    @BeforeEach
    public void initTest() {
        salesLead = createEntity(em);
    }

    @Test
    @Transactional
    void createSalesLead() throws Exception {
        int databaseSizeBeforeCreate = salesLeadRepository.findAll().size();
        // Create the SalesLead
        SalesLeadDTO salesLeadDTO = salesLeadMapper.toDto(salesLead);
        restSalesLeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salesLeadDTO)))
            .andExpect(status().isCreated());

        // Validate the SalesLead in the database
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeCreate + 1);
        SalesLead testSalesLead = salesLeadList.get(salesLeadList.size() - 1);
        assertThat(testSalesLead.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testSalesLead.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSalesLead.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSalesLead.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSalesLead.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createSalesLeadWithExistingId() throws Exception {
        // Create the SalesLead with an existing ID
        salesLead.setId(1L);
        SalesLeadDTO salesLeadDTO = salesLeadMapper.toDto(salesLead);

        int databaseSizeBeforeCreate = salesLeadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesLeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salesLeadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SalesLead in the database
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSalesLeads() throws Exception {
        // Initialize the database
        salesLeadRepository.saveAndFlush(salesLead);

        // Get all the salesLeadList
        restSalesLeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesLead.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getSalesLead() throws Exception {
        // Initialize the database
        salesLeadRepository.saveAndFlush(salesLead);

        // Get the salesLead
        restSalesLeadMockMvc
            .perform(get(ENTITY_API_URL_ID, salesLead.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salesLead.getId().intValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSalesLead() throws Exception {
        // Get the salesLead
        restSalesLeadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalesLead() throws Exception {
        // Initialize the database
        salesLeadRepository.saveAndFlush(salesLead);

        int databaseSizeBeforeUpdate = salesLeadRepository.findAll().size();

        // Update the salesLead
        SalesLead updatedSalesLead = salesLeadRepository.findById(salesLead.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSalesLead are not directly saved in db
        em.detach(updatedSalesLead);
        updatedSalesLead
            .created(UPDATED_CREATED)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .note(UPDATED_NOTE)
            .status(UPDATED_STATUS);
        SalesLeadDTO salesLeadDTO = salesLeadMapper.toDto(updatedSalesLead);

        restSalesLeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salesLeadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salesLeadDTO))
            )
            .andExpect(status().isOk());

        // Validate the SalesLead in the database
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeUpdate);
        SalesLead testSalesLead = salesLeadList.get(salesLeadList.size() - 1);
        assertThat(testSalesLead.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testSalesLead.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSalesLead.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSalesLead.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSalesLead.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingSalesLead() throws Exception {
        int databaseSizeBeforeUpdate = salesLeadRepository.findAll().size();
        salesLead.setId(longCount.incrementAndGet());

        // Create the SalesLead
        SalesLeadDTO salesLeadDTO = salesLeadMapper.toDto(salesLead);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesLeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salesLeadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salesLeadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesLead in the database
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalesLead() throws Exception {
        int databaseSizeBeforeUpdate = salesLeadRepository.findAll().size();
        salesLead.setId(longCount.incrementAndGet());

        // Create the SalesLead
        SalesLeadDTO salesLeadDTO = salesLeadMapper.toDto(salesLead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesLeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salesLeadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesLead in the database
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalesLead() throws Exception {
        int databaseSizeBeforeUpdate = salesLeadRepository.findAll().size();
        salesLead.setId(longCount.incrementAndGet());

        // Create the SalesLead
        SalesLeadDTO salesLeadDTO = salesLeadMapper.toDto(salesLead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesLeadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salesLeadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalesLead in the database
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalesLeadWithPatch() throws Exception {
        // Initialize the database
        salesLeadRepository.saveAndFlush(salesLead);

        int databaseSizeBeforeUpdate = salesLeadRepository.findAll().size();

        // Update the salesLead using partial update
        SalesLead partialUpdatedSalesLead = new SalesLead();
        partialUpdatedSalesLead.setId(salesLead.getId());

        partialUpdatedSalesLead.created(UPDATED_CREATED).phoneNumber(UPDATED_PHONE_NUMBER);

        restSalesLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalesLead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalesLead))
            )
            .andExpect(status().isOk());

        // Validate the SalesLead in the database
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeUpdate);
        SalesLead testSalesLead = salesLeadList.get(salesLeadList.size() - 1);
        assertThat(testSalesLead.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testSalesLead.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSalesLead.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSalesLead.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSalesLead.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSalesLeadWithPatch() throws Exception {
        // Initialize the database
        salesLeadRepository.saveAndFlush(salesLead);

        int databaseSizeBeforeUpdate = salesLeadRepository.findAll().size();

        // Update the salesLead using partial update
        SalesLead partialUpdatedSalesLead = new SalesLead();
        partialUpdatedSalesLead.setId(salesLead.getId());

        partialUpdatedSalesLead
            .created(UPDATED_CREATED)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .note(UPDATED_NOTE)
            .status(UPDATED_STATUS);

        restSalesLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalesLead.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalesLead))
            )
            .andExpect(status().isOk());

        // Validate the SalesLead in the database
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeUpdate);
        SalesLead testSalesLead = salesLeadList.get(salesLeadList.size() - 1);
        assertThat(testSalesLead.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testSalesLead.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSalesLead.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSalesLead.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSalesLead.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSalesLead() throws Exception {
        int databaseSizeBeforeUpdate = salesLeadRepository.findAll().size();
        salesLead.setId(longCount.incrementAndGet());

        // Create the SalesLead
        SalesLeadDTO salesLeadDTO = salesLeadMapper.toDto(salesLead);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salesLeadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salesLeadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesLead in the database
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalesLead() throws Exception {
        int databaseSizeBeforeUpdate = salesLeadRepository.findAll().size();
        salesLead.setId(longCount.incrementAndGet());

        // Create the SalesLead
        SalesLeadDTO salesLeadDTO = salesLeadMapper.toDto(salesLead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salesLeadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesLead in the database
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalesLead() throws Exception {
        int databaseSizeBeforeUpdate = salesLeadRepository.findAll().size();
        salesLead.setId(longCount.incrementAndGet());

        // Create the SalesLead
        SalesLeadDTO salesLeadDTO = salesLeadMapper.toDto(salesLead);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesLeadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(salesLeadDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalesLead in the database
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalesLead() throws Exception {
        // Initialize the database
        salesLeadRepository.saveAndFlush(salesLead);

        int databaseSizeBeforeDelete = salesLeadRepository.findAll().size();

        // Delete the salesLead
        restSalesLeadMockMvc
            .perform(delete(ENTITY_API_URL_ID, salesLead.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SalesLead> salesLeadList = salesLeadRepository.findAll();
        assertThat(salesLeadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

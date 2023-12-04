package com.tiskel.website.web.rest;

import static com.tiskel.website.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tiskel.website.IntegrationTest;
import com.tiskel.website.domain.DemoMeeting;
import com.tiskel.website.repository.DemoMeetingRepository;
import com.tiskel.website.service.dto.DemoMeetingDTO;
import com.tiskel.website.service.mapper.DemoMeetingMapper;
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
 * Integration tests for the {@link DemoMeetingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemoMeetingResourceIT {

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/demo-meetings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemoMeetingRepository demoMeetingRepository;

    @Autowired
    private DemoMeetingMapper demoMeetingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemoMeetingMockMvc;

    private DemoMeeting demoMeeting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemoMeeting createEntity(EntityManager em) {
        DemoMeeting demoMeeting = new DemoMeeting().created(DEFAULT_CREATED).date(DEFAULT_DATE).email(DEFAULT_EMAIL);
        return demoMeeting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemoMeeting createUpdatedEntity(EntityManager em) {
        DemoMeeting demoMeeting = new DemoMeeting().created(UPDATED_CREATED).date(UPDATED_DATE).email(UPDATED_EMAIL);
        return demoMeeting;
    }

    @BeforeEach
    public void initTest() {
        demoMeeting = createEntity(em);
    }

    @Test
    @Transactional
    void createDemoMeeting() throws Exception {
        int databaseSizeBeforeCreate = demoMeetingRepository.findAll().size();
        // Create the DemoMeeting
        DemoMeetingDTO demoMeetingDTO = demoMeetingMapper.toDto(demoMeeting);
        restDemoMeetingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demoMeetingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DemoMeeting in the database
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeCreate + 1);
        DemoMeeting testDemoMeeting = demoMeetingList.get(demoMeetingList.size() - 1);
        assertThat(testDemoMeeting.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDemoMeeting.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDemoMeeting.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createDemoMeetingWithExistingId() throws Exception {
        // Create the DemoMeeting with an existing ID
        demoMeeting.setId(1L);
        DemoMeetingDTO demoMeetingDTO = demoMeetingMapper.toDto(demoMeeting);

        int databaseSizeBeforeCreate = demoMeetingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemoMeetingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demoMeetingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemoMeeting in the database
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = demoMeetingRepository.findAll().size();
        // set the field null
        demoMeeting.setDate(null);

        // Create the DemoMeeting, which fails.
        DemoMeetingDTO demoMeetingDTO = demoMeetingMapper.toDto(demoMeeting);

        restDemoMeetingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demoMeetingDTO))
            )
            .andExpect(status().isBadRequest());

        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = demoMeetingRepository.findAll().size();
        // set the field null
        demoMeeting.setEmail(null);

        // Create the DemoMeeting, which fails.
        DemoMeetingDTO demoMeetingDTO = demoMeetingMapper.toDto(demoMeeting);

        restDemoMeetingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demoMeetingDTO))
            )
            .andExpect(status().isBadRequest());

        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemoMeetings() throws Exception {
        // Initialize the database
        demoMeetingRepository.saveAndFlush(demoMeeting);

        // Get all the demoMeetingList
        restDemoMeetingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demoMeeting.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getDemoMeeting() throws Exception {
        // Initialize the database
        demoMeetingRepository.saveAndFlush(demoMeeting);

        // Get the demoMeeting
        restDemoMeetingMockMvc
            .perform(get(ENTITY_API_URL_ID, demoMeeting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demoMeeting.getId().intValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingDemoMeeting() throws Exception {
        // Get the demoMeeting
        restDemoMeetingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDemoMeeting() throws Exception {
        // Initialize the database
        demoMeetingRepository.saveAndFlush(demoMeeting);

        int databaseSizeBeforeUpdate = demoMeetingRepository.findAll().size();

        // Update the demoMeeting
        DemoMeeting updatedDemoMeeting = demoMeetingRepository.findById(demoMeeting.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDemoMeeting are not directly saved in db
        em.detach(updatedDemoMeeting);
        updatedDemoMeeting.created(UPDATED_CREATED).date(UPDATED_DATE).email(UPDATED_EMAIL);
        DemoMeetingDTO demoMeetingDTO = demoMeetingMapper.toDto(updatedDemoMeeting);

        restDemoMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demoMeetingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demoMeetingDTO))
            )
            .andExpect(status().isOk());

        // Validate the DemoMeeting in the database
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeUpdate);
        DemoMeeting testDemoMeeting = demoMeetingList.get(demoMeetingList.size() - 1);
        assertThat(testDemoMeeting.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDemoMeeting.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemoMeeting.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingDemoMeeting() throws Exception {
        int databaseSizeBeforeUpdate = demoMeetingRepository.findAll().size();
        demoMeeting.setId(longCount.incrementAndGet());

        // Create the DemoMeeting
        DemoMeetingDTO demoMeetingDTO = demoMeetingMapper.toDto(demoMeeting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemoMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demoMeetingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demoMeetingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemoMeeting in the database
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemoMeeting() throws Exception {
        int databaseSizeBeforeUpdate = demoMeetingRepository.findAll().size();
        demoMeeting.setId(longCount.incrementAndGet());

        // Create the DemoMeeting
        DemoMeetingDTO demoMeetingDTO = demoMeetingMapper.toDto(demoMeeting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemoMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demoMeetingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemoMeeting in the database
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemoMeeting() throws Exception {
        int databaseSizeBeforeUpdate = demoMeetingRepository.findAll().size();
        demoMeeting.setId(longCount.incrementAndGet());

        // Create the DemoMeeting
        DemoMeetingDTO demoMeetingDTO = demoMeetingMapper.toDto(demoMeeting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemoMeetingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demoMeetingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemoMeeting in the database
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemoMeetingWithPatch() throws Exception {
        // Initialize the database
        demoMeetingRepository.saveAndFlush(demoMeeting);

        int databaseSizeBeforeUpdate = demoMeetingRepository.findAll().size();

        // Update the demoMeeting using partial update
        DemoMeeting partialUpdatedDemoMeeting = new DemoMeeting();
        partialUpdatedDemoMeeting.setId(demoMeeting.getId());

        partialUpdatedDemoMeeting.email(UPDATED_EMAIL);

        restDemoMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemoMeeting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemoMeeting))
            )
            .andExpect(status().isOk());

        // Validate the DemoMeeting in the database
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeUpdate);
        DemoMeeting testDemoMeeting = demoMeetingList.get(demoMeetingList.size() - 1);
        assertThat(testDemoMeeting.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDemoMeeting.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDemoMeeting.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateDemoMeetingWithPatch() throws Exception {
        // Initialize the database
        demoMeetingRepository.saveAndFlush(demoMeeting);

        int databaseSizeBeforeUpdate = demoMeetingRepository.findAll().size();

        // Update the demoMeeting using partial update
        DemoMeeting partialUpdatedDemoMeeting = new DemoMeeting();
        partialUpdatedDemoMeeting.setId(demoMeeting.getId());

        partialUpdatedDemoMeeting.created(UPDATED_CREATED).date(UPDATED_DATE).email(UPDATED_EMAIL);

        restDemoMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemoMeeting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemoMeeting))
            )
            .andExpect(status().isOk());

        // Validate the DemoMeeting in the database
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeUpdate);
        DemoMeeting testDemoMeeting = demoMeetingList.get(demoMeetingList.size() - 1);
        assertThat(testDemoMeeting.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDemoMeeting.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDemoMeeting.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingDemoMeeting() throws Exception {
        int databaseSizeBeforeUpdate = demoMeetingRepository.findAll().size();
        demoMeeting.setId(longCount.incrementAndGet());

        // Create the DemoMeeting
        DemoMeetingDTO demoMeetingDTO = demoMeetingMapper.toDto(demoMeeting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemoMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demoMeetingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demoMeetingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemoMeeting in the database
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemoMeeting() throws Exception {
        int databaseSizeBeforeUpdate = demoMeetingRepository.findAll().size();
        demoMeeting.setId(longCount.incrementAndGet());

        // Create the DemoMeeting
        DemoMeetingDTO demoMeetingDTO = demoMeetingMapper.toDto(demoMeeting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemoMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demoMeetingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemoMeeting in the database
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemoMeeting() throws Exception {
        int databaseSizeBeforeUpdate = demoMeetingRepository.findAll().size();
        demoMeeting.setId(longCount.incrementAndGet());

        // Create the DemoMeeting
        DemoMeetingDTO demoMeetingDTO = demoMeetingMapper.toDto(demoMeeting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemoMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(demoMeetingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemoMeeting in the database
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemoMeeting() throws Exception {
        // Initialize the database
        demoMeetingRepository.saveAndFlush(demoMeeting);

        int databaseSizeBeforeDelete = demoMeetingRepository.findAll().size();

        // Delete the demoMeeting
        restDemoMeetingMockMvc
            .perform(delete(ENTITY_API_URL_ID, demoMeeting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemoMeeting> demoMeetingList = demoMeetingRepository.findAll();
        assertThat(demoMeetingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

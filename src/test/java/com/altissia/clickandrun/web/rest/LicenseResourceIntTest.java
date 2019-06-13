package com.altissia.clickandrun.web.rest;

import com.altissia.clickandrun.ClickAndRunApp;
import com.altissia.clickandrun.domain.License;
import com.altissia.clickandrun.domain.enumeration.Language;
import com.altissia.clickandrun.domain.enumeration.Service;
import com.altissia.clickandrun.repository.LicenseRepository;
import com.altissia.clickandrun.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the LicenseResource REST controller.
 *
 * @see LicenseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClickAndRunApp.class)
public class LicenseResourceIntTest {

    private static final Service DEFAULT_SERVICE = Service.COURSE;
    private static final Service UPDATED_SERVICE = Service.ASSESSMENT;

    private static final Language DEFAULT_STUDY_LANGUAGE = Language.FR_FR;
    private static final Language UPDATED_STUDY_LANGUAGE = Language.NL_BE;

    private static final Instant DEFAULT_VALID_SINCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_SINCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VALID_UNTIL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALID_UNTIL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_CONSUMED = false;
    private static final Boolean UPDATED_CONSUMED = true;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLicenseMockMvc;

    private License license;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LicenseResource licenseResource = new LicenseResource(licenseRepository);
        this.restLicenseMockMvc = MockMvcBuilders.standaloneSetup(licenseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static License createEntity(EntityManager em) {
        return new License()
            .service(DEFAULT_SERVICE)
            .studyLanguage(DEFAULT_STUDY_LANGUAGE)
            .validSince(DEFAULT_VALID_SINCE)
            .validUntil(DEFAULT_VALID_UNTIL)
            .consumed(DEFAULT_CONSUMED);
    }

    @Before
    public void initTest() {
        license = createEntity(em);
    }

    @Test
    @Transactional
    public void createLicense() throws Exception {
        int databaseSizeBeforeCreate = licenseRepository.findAll().size();

        // Create the License
        restLicenseMockMvc.perform(post("/api/licenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(license)))
            .andExpect(status().isCreated());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeCreate + 1);
        License testLicense = licenseList.get(licenseList.size() - 1);
        assertThat(testLicense.getService()).isEqualTo(DEFAULT_SERVICE);
        assertThat(testLicense.getStudyLanguage()).isEqualTo(DEFAULT_STUDY_LANGUAGE);
        assertThat(testLicense.getValidSince()).isEqualTo(DEFAULT_VALID_SINCE);
        assertThat(testLicense.getValidUntil()).isEqualTo(DEFAULT_VALID_UNTIL);
        assertThat(testLicense.isConsumed()).isEqualTo(DEFAULT_CONSUMED);
    }

    @Test
    @Transactional
    public void createLicenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = licenseRepository.findAll().size();

        // Create the License with an existing ID
        license.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLicenseMockMvc.perform(post("/api/licenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(license)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkServiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseRepository.findAll().size();
        // set the field null
        license.setService(null);

        // Create the License, which fails.

        restLicenseMockMvc.perform(post("/api/licenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(license)))
            .andExpect(status().isBadRequest());

        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStudyLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseRepository.findAll().size();
        // set the field null
        license.setStudyLanguage(null);

        // Create the License, which fails.

        restLicenseMockMvc.perform(post("/api/licenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(license)))
            .andExpect(status().isBadRequest());

        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidSinceIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseRepository.findAll().size();
        // set the field null
        license.setValidSince(null);

        // Create the License, which fails.

        restLicenseMockMvc.perform(post("/api/licenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(license)))
            .andExpect(status().isBadRequest());

        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidUntilIsRequired() throws Exception {
        int databaseSizeBeforeTest = licenseRepository.findAll().size();
        // set the field null
        license.setValidUntil(null);

        // Create the License, which fails.

        restLicenseMockMvc.perform(post("/api/licenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(license)))
            .andExpect(status().isBadRequest());

        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLicenses() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get all the licenseList
        restLicenseMockMvc.perform(get("/api/licenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(license.getId().intValue())))
            .andExpect(jsonPath("$.[*].service").value(hasItem(DEFAULT_SERVICE.toString())))
            .andExpect(jsonPath("$.[*].studyLanguage").value(hasItem(DEFAULT_STUDY_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].validSince").value(hasItem(DEFAULT_VALID_SINCE.toString())))
            .andExpect(jsonPath("$.[*].validUntil").value(hasItem(DEFAULT_VALID_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].consumed").value(hasItem(DEFAULT_CONSUMED)));
    }

    @Test
    @Transactional
    public void getLicense() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);

        // Get the license
        restLicenseMockMvc.perform(get("/api/licenses/{id}", license.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(license.getId().intValue()))
            .andExpect(jsonPath("$.service").value(DEFAULT_SERVICE.toString()))
            .andExpect(jsonPath("$.studyLanguage").value(DEFAULT_STUDY_LANGUAGE.toString()))
            .andExpect(jsonPath("$.validSince").value(DEFAULT_VALID_SINCE.toString()))
            .andExpect(jsonPath("$.validUntil").value(DEFAULT_VALID_UNTIL.toString()))
            .andExpect(jsonPath("$.consumed").value(DEFAULT_CONSUMED));
    }

    @Test
    @Transactional
    public void getNonExistingLicense() throws Exception {
        // Get the license
        restLicenseMockMvc.perform(get("/api/licenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLicense() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);
        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();

        // Update the license
        License updatedLicense = licenseRepository.findOne(license.getId());
        updatedLicense
            .service(UPDATED_SERVICE)
            .studyLanguage(UPDATED_STUDY_LANGUAGE)
            .validSince(UPDATED_VALID_SINCE)
            .validUntil(UPDATED_VALID_UNTIL)
            .consumed(UPDATED_CONSUMED);

        restLicenseMockMvc.perform(put("/api/licenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLicense)))
            .andExpect(status().isOk());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate);
        License testLicense = licenseList.get(licenseList.size() - 1);
        assertThat(testLicense.getService()).isEqualTo(UPDATED_SERVICE);
        assertThat(testLicense.getStudyLanguage()).isEqualTo(UPDATED_STUDY_LANGUAGE);
        assertThat(testLicense.getValidSince()).isEqualTo(UPDATED_VALID_SINCE);
        assertThat(testLicense.getValidUntil()).isEqualTo(UPDATED_VALID_UNTIL);
        assertThat(testLicense.isConsumed()).isEqualTo(UPDATED_CONSUMED);
    }

    @Test
    @Transactional
    public void updateNonExistingLicense() throws Exception {
        int databaseSizeBeforeUpdate = licenseRepository.findAll().size();

        // Create the License

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLicenseMockMvc.perform(put("/api/licenses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(license)))
            .andExpect(status().isCreated());

        // Validate the License in the database
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLicense() throws Exception {
        // Initialize the database
        licenseRepository.saveAndFlush(license);
        int databaseSizeBeforeDelete = licenseRepository.findAll().size();

        // Get the license
        restLicenseMockMvc.perform(delete("/api/licenses/{id}", license.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<License> licenseList = licenseRepository.findAll();
        assertThat(licenseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(License.class);
        License license1 = new License();
        license1.setId(1L);
        License license2 = new License();
        license2.setId(license1.getId());
        assertThat(license1).isEqualTo(license2);
        license2.setId(2L);
        assertThat(license1).isNotEqualTo(license2);
        license1.setId(null);
        assertThat(license1).isNotEqualTo(license2);
    }
}

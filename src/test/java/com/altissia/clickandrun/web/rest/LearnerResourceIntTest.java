package com.altissia.clickandrun.web.rest;

import com.altissia.clickandrun.ClickAndRunApp;
import com.altissia.clickandrun.domain.Learner;
import com.altissia.clickandrun.domain.enumeration.Language;
import com.altissia.clickandrun.repository.LearnerRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the learnerResource REST controller.
 *
 * @see LearnerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClickAndRunApp.class)
public class LearnerResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "Adrien";
    private static final String UPDATED_FIRST_NAME = "Renaud";

    private static final String DEFAULT_LAST_NAME = "Horgnies";
    private static final String UPDATED_LAST_NAME = "Laloux";

    private static final String DEFAULT_LOGIN = "ahorgnies@altissia.org";
    private static final String UPDATED_LOGIN = "rlaloux@altissia.org";

    private static final Language DEFAULT_INTERFACE_LANGUAGE = Language.FR_FR;
    private static final Language UPDATED_INTERFACE_LANGUAGE = Language.NL_NL;

    @Autowired
    private LearnerRepository learnerRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restlearnerMockMvc;

    private Learner learner;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LearnerResource learnerResource = new LearnerResource(learnerRepository);
        this.restlearnerMockMvc = MockMvcBuilders.standaloneSetup(learnerResource)
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
    public static Learner createEntity(EntityManager em) {
        return new Learner()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .login(DEFAULT_LOGIN)
            .interfaceLanguage(DEFAULT_INTERFACE_LANGUAGE);
    }

    @Before
    public void initTest() {
        learner = createEntity(em);
    }

    @Test
    @Transactional
    public void createlearner() throws Exception {
        int databaseSizeBeforeCreate = learnerRepository.findAll().size();

        // Create the Learner
        restlearnerMockMvc.perform(post("/api/learners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learner)))
            .andExpect(status().isCreated());

        // Validate the Learner in the database
        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeCreate + 1);
        Learner testLearner = learnerList.get(learnerList.size() - 1);
        assertThat(testLearner.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testLearner.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testLearner.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testLearner.getInterfaceLanguage()).isEqualTo(DEFAULT_INTERFACE_LANGUAGE);
    }

    @Test
    @Transactional
    public void createlearnerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = learnerRepository.findAll().size();

        // Create the Learner with an existing ID
        learner.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restlearnerMockMvc.perform(post("/api/learners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learner)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = learnerRepository.findAll().size();
        // set the field null
        learner.setLastName(null);

        // Create the Learner, which fails.

        restlearnerMockMvc.perform(post("/api/learners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learner)))
            .andExpect(status().isBadRequest());

        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = learnerRepository.findAll().size();
        // set the field null
        learner.setLogin(null);

        // Create the Learner, which fails.

        restlearnerMockMvc.perform(post("/api/learners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learner)))
            .andExpect(status().isBadRequest());

        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInterfaceLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = learnerRepository.findAll().size();
        // set the field null
        learner.setInterfaceLanguage(null);

        // Create the Learner, which fails.

        restlearnerMockMvc.perform(post("/api/learners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learner)))
            .andExpect(status().isBadRequest());

        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAlllearners() throws Exception {
        // Initialize the database
        learnerRepository.saveAndFlush(learner);

        // Get all the learnerList
        restlearnerMockMvc.perform(get("/api/learners?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(learner.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].interfaceLanguage").value(hasItem(DEFAULT_INTERFACE_LANGUAGE.toString())));
    }

    @Test
    @Transactional
    public void getlearner() throws Exception {
        // Initialize the database
        learnerRepository.saveAndFlush(learner);

        // Get the learner
        restlearnerMockMvc.perform(get("/api/learners/{id}", learner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(learner.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.interfaceLanguage").value(DEFAULT_INTERFACE_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistinglearner() throws Exception {
        // Get the learner
        restlearnerMockMvc.perform(get("/api/learners/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatelearner() throws Exception {
        // Initialize the database
        learnerRepository.saveAndFlush(learner);
        int databaseSizeBeforeUpdate = learnerRepository.findAll().size();

        // Update the learner
        Learner updatedLearner = learnerRepository.findOne(learner.getId());
        updatedLearner
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .login(UPDATED_LOGIN)
            .interfaceLanguage(UPDATED_INTERFACE_LANGUAGE);

        restlearnerMockMvc.perform(put("/api/learners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLearner)))
            .andExpect(status().isOk());

        // Validate the Learner in the database
        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeUpdate);
        Learner testLearner = learnerList.get(learnerList.size() - 1);
        assertThat(testLearner.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testLearner.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testLearner.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testLearner.getInterfaceLanguage()).isEqualTo(UPDATED_INTERFACE_LANGUAGE);
    }

    @Test
    @Transactional
    public void updateNonExistinglearner() throws Exception {
        int databaseSizeBeforeUpdate = learnerRepository.findAll().size();

        // Create the Learner

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restlearnerMockMvc.perform(put("/api/learners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learner)))
            .andExpect(status().isCreated());

        // Validate the Learner in the database
        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletelearner() throws Exception {
        // Initialize the database
        learnerRepository.saveAndFlush(learner);
        int databaseSizeBeforeDelete = learnerRepository.findAll().size();

        // Get the learner
        restlearnerMockMvc.perform(delete("/api/learners/{id}", learner.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Learner> learnerList = learnerRepository.findAll();
        assertThat(learnerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Learner.class);
        Learner learner1 = new Learner();
        learner1.setId(1L);
        Learner learner2 = new Learner();
        learner2.setId(learner1.getId());
        assertThat(learner1).isEqualTo(learner2);
        learner2.setId(2L);
        assertThat(learner1).isNotEqualTo(learner2);
        learner1.setId(null);
        assertThat(learner1).isNotEqualTo(learner2);
    }
}

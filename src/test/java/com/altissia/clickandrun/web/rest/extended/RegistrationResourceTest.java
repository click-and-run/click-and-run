package com.altissia.clickandrun.web.rest.extended;

import com.altissia.clickandrun.ClickAndRunApp;
import com.altissia.clickandrun.TestFileProvider;
import com.altissia.clickandrun.domain.Learner;
import com.altissia.clickandrun.domain.enumeration.Language;
import com.altissia.clickandrun.service.extended.WorkbookExtendedService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClickAndRunApp.class)
public class RegistrationResourceTest {

    private static final Logger log = LoggerFactory.getLogger(RegistrationResourceTest.class);

    private static final String VALIDATION_ENDPOINT = "/api/registration/validate";

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private WorkbookExtendedService workbookExtendedService;

    @Autowired
    private EntityManager entityManager;

    private MockMvc restMock;

    private TestFileProvider testFileProvider = new TestFileProvider();

    @Before
    public void setup() {
        RegistrationResource registrationResource = new RegistrationResource(workbookExtendedService);

        // reflexion utils
        restMock = MockMvcBuilders.standaloneSetup(registrationResource)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }


    @Test
    public void testValideModelFile() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/registration/model.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.valid").value("true"));
    }

    @Test
    public void testDuplicateLogin() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/registration/duplicate-login.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.valid").value("false"))
            .andExpect(jsonPath("$.validations.registrants.valid").value("false"))
            .andExpect(jsonPath("$.validations.services.valid").value("true"))
            .andExpect(jsonPath("$.validations.registrants.errors").value(hasSize(2)))
            .andExpect(jsonPath("$.validations.registrants.errors.[*].field").value(everyItem(is("login"))))
            .andExpect(jsonPath("$.validations.registrants.errors.[*].violation").value(everyItem(is("com.altissia.constraints.login.duplicate"))))
            .andExpect(jsonPath("$.validations.registrants.errors.[*].value").value(everyItem(is("rlaloux@altissia.org"))));
    }

    @Test
    public void testUnavailableLogin() throws Exception {
        Learner learner = new Learner();
        learner.setLogin("ahorgnies@altissia.org");
        learner.setFirstName("Adrien");
        learner.setLastName("Horgnies");
        learner.setInterfaceLanguage(Language.EN_GB);

        entityManager.persist(learner);
        entityManager.flush();

        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/registration/used-login.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.valid").value("false"))
            .andExpect(jsonPath("$.validations.registrants.valid").value("false"))
            .andExpect(jsonPath("$.validations.services.valid").value("true"))
            .andExpect(jsonPath("$.validations.registrants.errors").value(hasSize(1)))
            .andExpect(jsonPath("$.validations.registrants.errors.[0].field").value(is("login")))
            .andExpect(jsonPath("$.validations.registrants.errors.[0].violation").value(is("com.altissia.constraints.login.unavailable")))
            .andExpect(jsonPath("$.validations.registrants.errors.[0].value").value(is("ahorgnies@altissia.org")));
    }

    @Test
    public void testUnusedRegistrant() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/registration/unused-registrant.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.valid").value("false"))
            .andExpect(jsonPath("$.validations.registrants.valid").value("false"))
            .andExpect(jsonPath("$.validations.services.valid").value("true"))
            .andExpect(jsonPath("$.validations.registrants.errors").value(hasSize(1)))
            .andExpect(jsonPath("$.validations.registrants.errors.[0].field").value(is("login")))
            .andExpect(jsonPath("$.validations.registrants.errors.[0].violation").value(is("com.altissia.constraints.registrant.unused")))
            .andExpect(jsonPath("$.validations.registrants.errors.[0].value").value(is("adrien.pierre.horgnies@gmail.com")));
    }

    @Test
    public void testDuplicateService() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/registration/duplicate-service.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.valid").value("false"))
            .andExpect(jsonPath("$.validations.registrants.valid").value("true"))
            .andExpect(jsonPath("$.validations.services.valid").value("false"))
            .andExpect(jsonPath("$.validations.services.errors").value(hasSize(2)))
            .andExpect(jsonPath("$.validations.services.errors.[*].field").value(everyItem(is("login"))))
            .andExpect(jsonPath("$.validations.services.errors.[*].violation").value(everyItem(is("com.altissia.constraints.service.duplicate"))))
            .andExpect(jsonPath("$.validations.services.errors.[*].value").value(everyItem(is("adrien.pierre.horgnies@gmail.com"))));
    }

    @Test
    public void testUndefinedRegistrant() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/registration/undefined-registrant.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.valid").value("false"))
            .andExpect(jsonPath("$.validations.registrants.valid").value("true"))
            .andExpect(jsonPath("$.validations.services.valid").value("false"))
            .andExpect(jsonPath("$.validations.services.errors").value(hasSize(1)))
            .andExpect(jsonPath("$.validations.services.errors.[0].field").value(is("login")))
            .andExpect(jsonPath("$.validations.services.errors.[0].violation").value(is("com.altissia.constraints.registrant.undefined")))
            .andExpect(jsonPath("$.validations.services.errors.[0].value").value(is("adrien.pierre.horgnies@gmail.com")));
    }
}

package com.altissia.clickandrun.web.rest.extended;

import com.altissia.clickandrun.ClickAndRunApp;
import com.altissia.clickandrun.TestFileProvider;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Set of generic validation test that are present by default
 * in the service but is tested through rest endpoint for ease
 * of result assertion.
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClickAndRunApp.class)
public class ImportGenericValidationTest {

    private static final Logger log = LoggerFactory.getLogger(ImportGenericValidationTest.class);

    private static final String VALIDATION_ENDPOINT = "/api/registration/validate";

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private WorkbookExtendedService workbookExtendedService;

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
    public void testValidateEmptyFile() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/error-empty.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.registrants.headers.[*].violation").value("com.altissia.constraints.sheet.missing"))
            .andExpect(jsonPath("$.registrants.valid").value("false"))
            .andExpect(jsonPath("$.services.headers.[*].violation").value("com.altissia.constraints.sheet.missing"))
            .andExpect(jsonPath("$.services.valid").value("false"));
    }

    /**
     * Poiji skip blank row
     */
    @Test
    public void testValidateEmptyRow() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/error-empty-row.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.registrants.valid").value("true"))
            .andExpect(jsonPath("$.services.valid").value("true"));
    }

    @Test
    public void testValidateStartNotZero() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/error-start-not-zero.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.registrants.headers.[*].violation").value(hasSize(4)))
            .andExpect(jsonPath("$.registrants.headers.[*].violation").value(everyItem(is("com.altissia.constraints.header.missing"))))
            .andExpect(jsonPath("$.registrants.headers.[*].column").value(everyItem(is(-1))))
            .andExpect(jsonPath("$.registrants.valid").value("false"));
    }

    /**
     * Poiji ignore additional row that are not referenced by an header
     */
    @Test
    public void testValidateWiderHeaders() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/valid-wider-than-header.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.registrants.valid").value("true"))
            .andExpect(jsonPath("$.services.valid").value("true"));
    }

    @Test
    public void testValidateHeadersColumnMissing() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/error-column-missing.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.registrants.valid").value("true"))
            .andExpect(jsonPath("$.services.headers.[*].violation").value("com.altissia.constraints.header.missing"))
            .andExpect(jsonPath("$.services.headers.[*].column").value(-1))
            .andExpect(jsonPath("$.services.valid").value("false"));
    }

    @Test
    public void testValidateHeadersBlank() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/error-header-blank.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.registrants.valid").value("true"))
            .andExpect(jsonPath("$.services.headers.[*].violation").value(hasSize(2)))
            .andExpect(jsonPath("$.services.headers.[*].violation").value(containsInAnyOrder("com.altissia.constraints.header.missing", "com.altissia.constraints.header.blank")))
            .andExpect(jsonPath("$.services.headers.[*].column").value(hasItems(-1, 1)))
            .andExpect(jsonPath("$.services.valid").value("false"));
    }

    @Test
    public void testValidateHeadersMissing() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/error-header-missing.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.registrants.valid").value("true"))
            .andExpect(jsonPath("$.services.headers.[*].violation").value("com.altissia.constraints.header.missing"))
            .andExpect(jsonPath("$.services.headers.[*].column").value(-1))
            .andExpect(jsonPath("$.services.valid").value("false"));
    }

    @Test
    public void testValidateHeadersUnrecognized() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/error-unknown-header.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(jsonPath("$.registrants.valid").value("true"))
            .andExpect(jsonPath("$.services.headers.[*].violation").value(hasItem("com.altissia.constraints.header.invalid")))
            .andExpect(jsonPath("$.services.headers.[*].value").value(hasItems("another header", "yet another header")))
            .andExpect(jsonPath("$.services.headers.[*].column").value(hasItems(1, 4)))
            .andExpect(jsonPath("$.services.valid").value("false"));
    }

    @Test
    public void testValidateWrongFormat() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/error-wrong-format")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testValidationOrdering() throws Exception {
        String firstResponse = restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/error-various-content.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String secondResponse = restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/error-various-content.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThat(firstResponse).isEqualTo(secondResponse);
    }

    @Test
    public void testValidatingValid() throws Exception {
        restMock.perform(MockMvcRequestBuilders
            .fileUpload(VALIDATION_ENDPOINT)
            .file(testFileProvider.getXLSX("/import/generic/model.xlsx")))
            .andDo(mvcResult -> log.debug("Response: {}, {}", mvcResult.getResponse().getStatus(), mvcResult.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.registrants.valid").value("true"))
            .andExpect(jsonPath("$.services.valid").value("true"));
    }
}

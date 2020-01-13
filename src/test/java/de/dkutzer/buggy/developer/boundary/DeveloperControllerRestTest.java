package de.dkutzer.buggy.developer.boundary;

import de.dkutzer.buggy.developer.control.DeveloperRepository;
import de.dkutzer.buggy.developer.entity.DeveloperEntity;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("mockmvc")
@ActiveProfiles("test")
class DeveloperControllerRestTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeveloperRepository developerRepository;

    @BeforeEach
    void setUp() {
        developerRepository.deleteAll();
    }


    @Test
    @DisplayName("Test of creating a new developer")
    void createDeveloperTest() throws Exception {
        final String operationName = "developer-create";

        final String developerInDto = "{\"name\":\"Steve Jobs\"}";
        // when - the resource is passed via POST
        mockMvc.perform(RestDocumentationRequestBuilders.post("/developers/")
            .content(developerInDto)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(jsonPath("$.name", Matchers.is("Steve Jobs")));


        Assert.assertEquals(developerRepository.count(),1);
        Assert.assertTrue(developerRepository.findByName("Steve Jobs").isPresent());


    }


    @Test
    @DisplayName("Test of getting an existing developer")
    void getDeveloperTest() throws Exception {
        final String operationName = "developer-get";
        DeveloperEntity bill = new DeveloperEntity();
        bill.setName("bill");
        developerRepository.save(bill);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/developers/{name}","bill")
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.name", Matchers.is("bill")));

    }

    @Test
    @DisplayName("Test of getting all existing developers")
    void getAllDeveloperTest() throws Exception {
        final String operationName = "developer-get-all";
        DeveloperEntity bill = new DeveloperEntity();
        bill.setName("Bill Gates");
        DeveloperEntity steve = new DeveloperEntity();
        steve.setName("Steve Jobs");
        developerRepository.save(bill);
        developerRepository.save(steve);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/developers/")
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.[0].name", Matchers.is("Bill Gates")))
            .andExpect(jsonPath("$.[1].name", Matchers.is("Steve Jobs")));
    }
    @Test
    @DisplayName("Test of deleting existing developers")
    void deleteDeveloperTest() throws Exception {
        final String operationName = "developer-delete";
        DeveloperEntity bill = new DeveloperEntity();
        bill.setName("Bill Gates");
        DeveloperEntity steve = new DeveloperEntity();
        steve.setName("Steve Jobs");
        developerRepository.save(bill);
        developerRepository.save(steve);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/developers/{name}", "Steve Jobs"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assert.assertEquals(developerRepository.count(),1);
        Assert.assertTrue(developerRepository.findByName("Bill Gates").isPresent());
        Assert.assertFalse(developerRepository.findByName("Steve Jobs").isPresent());

    }

}

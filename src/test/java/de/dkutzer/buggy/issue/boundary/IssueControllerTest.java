package de.dkutzer.buggy.issue.boundary;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dkutzer.buggy.ConstrainedFields;
import de.dkutzer.buggy.developer.control.DeveloperRepository;
import de.dkutzer.buggy.developer.entity.DeveloperDto;
import de.dkutzer.buggy.issue.control.IssueRepository;
import de.dkutzer.buggy.issue.entity.IssueDto;
import de.dkutzer.buggy.issue.entity.Status;
import de.dkutzer.buggy.issue.entity.Type;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Tag("restdocs")
@ActiveProfiles("test")
class IssueControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DeveloperRepository developerRepository;
    @Autowired
    private IssueRepository issueRepository;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        issueRepository.deleteAll();
        developerRepository.deleteAll();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(restDocumentation).uris().withScheme("https")
                .withHost("buggy.io").withPort(443).and()
                .operationPreprocessors().withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint()))
            .build();
    }
    private static  final ConstrainedFields CONSTRAINED_FIELDS = new ConstrainedFields(IssueDto.class);

    private static final FieldDescriptor[] issueFieldDescriptors = {
        fieldWithPath("type").description("The type of the Issue. Bug or Story").attributes(key("constraints").value("")),
        fieldWithPath("description").description("The description of the Issue.").attributes(key("constraints").value("")),
        fieldWithPath("points").description("The estimated workload to finish the story.").attributes(key("constraints").value("")),
        fieldWithPath("status").description("The current status.").attributes(key("constraints").value("")),
        fieldWithPath("id").description("The unique id.").attributes(key("constraints").value("")),
        fieldWithPath("createdAt").description("UTC Time of the creation of the issue.").attributes(key("constraints").value("")),
        fieldWithPath("assignee").description("Currently assigned developer").attributes(key("constraints").value("")),
        fieldWithPath("priority").description("Priority of the Bug").attributes(key("constraints").value("")),
        fieldWithPath("title").description("The title of the Issue.").attributes(key("constraints").value(""))
    };

    @Test
    @DisplayName("Test of creating a new issue (Story)")
    void createStoryTest() throws Exception {
        final String operationName = "issue-create-story";

        IssueDto storyDto = new IssueDto();
        storyDto.setType(Type.STORY);
        storyDto.setTitle("Test A");
        storyDto.setDescription("As bla I want to blub with blab");
        storyDto.setStatus(Status.New);
        storyDto.setPoints(5);

        // when - the resource is passed via POST
        mockMvc.perform(RestDocumentationRequestBuilders.post("/issues/")
            .content(objectMapper.writeValueAsString(storyDto))
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(jsonPath("$.type", Matchers.is("STORY")))
            .andExpect(jsonPath("$.id", Matchers.notNullValue()))

            // rest docs
            .andDo(document(operationName,
                responseFields(issueFieldDescriptors),
                requestFields(issueFieldDescriptors)));
    }

    @Test
    @DisplayName("Test of getting an issue (Story)")
    void getStoryTest() throws Exception {
        final String operationName = "issue-get-story";

        IssueDto storyDto = new IssueDto();
        storyDto.setType(Type.STORY);
        storyDto.setTitle("Test A");
        storyDto.setDescription("As bla I want to blub with blab");
        storyDto.setStatus(Status.New);
        storyDto.setPoints(5);

        final MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.post("/issues/")
            .content(objectMapper.writeValueAsString(storyDto))
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        final IssueDto savedIssueDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), IssueDto.class);

        // when - the resource is passed via POST
        mockMvc.perform(RestDocumentationRequestBuilders.get("/issues/{id}",savedIssueDto.getId())
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.type", Matchers.is("STORY")))
            .andExpect(jsonPath("$.id", Matchers.is(savedIssueDto.getId())))
            .andExpect(jsonPath("$.createdAt", Matchers.notNullValue()))

            // rest docs
            .andDo(document(operationName,
                pathParameters(
                    parameterWithName("id").description("the id of the desired issue")),

                responseFields(issueFieldDescriptors)));
    }


}
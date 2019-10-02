package de.dkutzer.buggy.planning.boundary;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dkutzer.buggy.developer.control.DeveloperRepository;
import de.dkutzer.buggy.developer.entity.DeveloperDto;
import de.dkutzer.buggy.issue.control.IssueRepository;
import de.dkutzer.buggy.issue.entity.IssueDto;
import de.dkutzer.buggy.issue.entity.Priority;
import de.dkutzer.buggy.issue.entity.Status;
import de.dkutzer.buggy.issue.entity.Type;
import de.dkutzer.buggy.planning.entity.PlanningDto;
import de.dkutzer.buggy.planning.entity.Week;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
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
class PlanningControllerTest {


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

    private static final FieldDescriptor[] planningFieldDescriptors = {
        fieldWithPath("summary.weeks").description("Number of weeks until all stories are finished"),
        fieldWithPath("summary.issues").description("Number of all issues."),
        fieldWithPath("summary.developers").description("Number of developers"),
        fieldWithPath("summary.issuesPerWeek").description("Avarage value of issues per week"),
        fieldWithPath("weeks[].number").description("Number of a working week."),
        fieldWithPath("weeks[].issueDtos[].id").description("Id of the issue in this week"),
        fieldWithPath("weeks[].issueDtos[].type").description("Type of the issue in this week"),
        fieldWithPath("weeks[].issueDtos[].title").description("Title of the issue in this week"),
        fieldWithPath("weeks[].issueDtos[].description").description("Description of the issue in this week"),
        fieldWithPath("weeks[].issueDtos[].assignee").description("Assigne of the issue in this week"),
        fieldWithPath("weeks[].issueDtos[].points").description("Estimated Points of the issue in this week"),
        fieldWithPath("weeks[].issueDtos[].status").description("Status of the issue in this week"),
        fieldWithPath("weeks[].issueDtos[].priority").description("Priority of the issue in this week"),
        fieldWithPath("weeks[].issueDtos[].createdAt").description("Creation Date of the issue in this week")
    };


    @Test
    @DisplayName("Test of getting a planning")
    void getStoryTest() throws Exception {
        final String operationName = "planning-get";

        //lets create some estimated stories
        givenEstimatedStory("A",5);
        givenEstimatedStory("B",10);
        givenEstimatedStory("C",1);
        givenEstimatedStory("D",5);
        givenEstimatedStory("E",20);
        givenEstimatedStory("F",7);
        //and for the fun an already completed one
        givenCompletedStory("G");
        //a bug cannot be absent
        givenBug();
        //3 devs are fine
        givenDeveloper();
        givenDeveloper();
        givenDeveloper();


        // when
        final MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.get("/planning/")
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            // rest docs
            .andDo(document(operationName,
                responseFields(planningFieldDescriptors)))
            .andReturn();

        final PlanningDto planningDto = objectMapper
            .readValue(mvcResult.getResponse().getContentAsString(), PlanningDto.class);

        //then
        Assert.assertEquals(3,planningDto.getSummary().getDevelopers());
        Assert.assertEquals(2,planningDto.getSummary().getWeeks());
        Assert.assertEquals(6,planningDto.getSummary().getIssues());
        Assert.assertEquals(3.0,planningDto.getSummary().getIssuesPerWeek(),0.01);
        Assert.assertEquals(2,planningDto.getWeeks().size());
        final Week firstWeek = planningDto.getWeeks().get(0);
        Assert.assertEquals(1,(int)firstWeek.getNumber());
        Assert.assertEquals(5,firstWeek.getIssueDtos().size());
        final Week secondWeek = planningDto.getWeeks().get(1);
        Assert.assertEquals(2,(int)secondWeek.getNumber());
        Assert.assertEquals(2,secondWeek.getIssueDtos().size());

    }

    private void givenDeveloper() throws Exception {
        DeveloperDto dto = new DeveloperDto();
        dto.setName(RandomStringUtils.randomAlphanumeric(10));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/developers/")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    private void givenBug() throws Exception {
        IssueDto dto = new IssueDto();
        dto.setType(Type.BUG);
        dto.setTitle(RandomStringUtils.randomAlphabetic(50));
        dto.setDescription(RandomStringUtils.randomAlphabetic(50));
        dto.setStatus(Status.New);
        dto.setPriority(Priority.Major);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/issues/")
            .content(objectMapper.writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    }

    private IssueDto givenEstimatedStory(String title, int points) throws Exception {
        IssueDto storyDto = new IssueDto();
        storyDto.setType(Type.STORY);
        storyDto.setTitle(title);
        storyDto.setDescription(RandomStringUtils.randomAlphabetic(50));
        storyDto.setStatus(Status.Estimated);
        storyDto.setPoints(points);

        final MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.post("/issues/")
            .content(objectMapper.writeValueAsString(storyDto))
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        final IssueDto savedIssueDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), IssueDto.class);

        return savedIssueDto;

    }
    private IssueDto givenCompletedStory(String title) throws Exception {
        IssueDto storyDto = new IssueDto();
        storyDto.setType(Type.STORY);
        storyDto.setTitle(title);
        storyDto.setDescription(RandomStringUtils.randomAlphabetic(50));
        storyDto.setStatus(Status.Completed);
        storyDto.setPoints(RandomUtils.nextInt(1,20));

        final MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.post("/issues/")
            .content(objectMapper.writeValueAsString(storyDto))
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        final IssueDto savedIssueDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), IssueDto.class);

        return savedIssueDto;

    }


}
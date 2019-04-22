package fr.lmarchau.proto.fees.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lmarchau.proto.fees.config.RepositoryTestConfig;
import fr.lmarchau.proto.fees.dto.*;
import fr.lmarchau.proto.fees.service.FeesEngineService;
import fr.lmarchau.proto.fees.service.RuleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQL)
@Import(RepositoryTestConfig.class)
public class ControllerTest {

    @MockBean
    private RuleService ruleService;
    @MockBean
    private FeesEngineService feesEngineService;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void createRuleShouldReturnCreatedRule() throws Exception {
        RuleDto rule = RuleDto.builder().name("test").rule("rule details").build();

        given(ruleService.create(rule)).willReturn(0L);

        mvc.perform(put("/rules").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(rule)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.rule").value("rule details"));

        verify(ruleService).create(rule);
    }

    @Test
    public void computeFeesShouldReturnRelationshipFees() throws Exception {
        BusinessRelationship relationship = BusinessRelationship.builder()
                .client(Member.builder().ip("217.127.206.227").build())
                .freelance(Member.builder().ip("217.127.206.227").build())
                .mission(Mission.builder().length("2months").build())
                .firstMission(LocalDateTime.now().minusYears(2))
                .lastMission(LocalDateTime.now().minusMonths(2))
                .build();

        RelationshipFees relationshipFees = RelationshipFees.builder().fees(10).build();


        given(feesEngineService.compute(relationship)).willReturn(relationshipFees);

        mvc.perform(get("/fees/compute").contentType(APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(relationship)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.fees").value(10))
                .andExpect(jsonPath("$.ruleName").doesNotExist());

        verify(feesEngineService).compute(relationship);
    }

}

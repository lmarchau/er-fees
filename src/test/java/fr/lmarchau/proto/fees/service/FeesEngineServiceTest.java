package fr.lmarchau.proto.fees.service;

import fr.lmarchau.proto.fees.dto.BusinessRelationship;
import fr.lmarchau.proto.fees.dto.Member;
import fr.lmarchau.proto.fees.dto.Mission;
import fr.lmarchau.proto.fees.dto.RelationshipFees;
import fr.lmarchau.proto.fees.repository.IpStackRepository;
import fr.lmarchau.proto.fees.repository.RuleRepository;
import fr.lmarchau.proto.fees.rule.FeesRuleEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FeesEngineServiceTest {

    @Mock
    private RuleRepository ruleRepository;
    @Mock
    private IpStackRepository ipStackRepository;
    @Mock
    private FeesRuleEngine engine;

    @InjectMocks
    private FeesEngineService feesEngineService;

    @Test
    public void computeWithInvalidParametersShouldThrowAnException() {

        assertThatThrownBy(() -> feesEngineService.compute(BusinessRelationship.builder().build())).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void computeWithoutRuleShouldReturnDefaultFee() {
        BusinessRelationship relationship = BusinessRelationship.builder()
                .mission(Mission.builder().length("1month").build())
                .client(Member.builder().ip("1.2.3.4").build())
                .freelance(Member.builder().ip("4.5.6.7").build())
                .build();

        when(ruleRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
        when(engine.apply(relationship, Collections.EMPTY_LIST)).thenReturn(RelationshipFees.builder().fees(10).build());

        RelationshipFees result = feesEngineService.compute(relationship);

        assertThat(result).isNotNull().extracting("fees").containsExactly(10);

        verify(ruleRepository).findAll();
        verify(engine).apply(relationship, Collections.EMPTY_LIST);

    }
    @Test
    public void computeWithoutRuleShouldThrowAnException() {
        BusinessRelationship relationship = BusinessRelationship.builder()
                .mission(Mission.builder().length("1day").build())
                .client(Member.builder().ip("1.2.3.4").build())
                .freelance(Member.builder().ip("4.5.6.7").build())
                .build();

        assertThatThrownBy(() -> feesEngineService.compute(relationship)).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void computeShouldGeoLocClientAndFreelance() {
        BusinessRelationship relationship = BusinessRelationship.builder()
                .mission(Mission.builder().length("1month").build())
                .client(Member.builder().ip("1.2.3.4").build())
                .freelance(Member.builder().ip("4.5.6.7").build())
                .build();

        when(ruleRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
        when(engine.apply(relationship, Collections.EMPTY_LIST)).thenReturn(RelationshipFees.builder().fees(10).build());

        RelationshipFees result = feesEngineService.compute(relationship);

        assertThat(result).isNotNull().extracting("fees").containsExactly(10);

        verify(ipStackRepository, times(2)).findCountryCodeByIp(anyString());
        verify(ruleRepository).findAll();
        verify(engine).apply(relationship, Collections.EMPTY_LIST);

    }

}

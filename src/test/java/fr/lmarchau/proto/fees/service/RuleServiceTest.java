package fr.lmarchau.proto.fees.service;

import fr.lmarchau.proto.fees.dto.RuleDto;
import fr.lmarchau.proto.fees.model.Rule;
import fr.lmarchau.proto.fees.repository.RuleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RuleServiceTest {

    @Mock
    private RuleRepository ruleRepository;

    @InjectMocks
    private RuleService ruleService;

    @Test
    public void createRuleWithoutNameShouldThrowAnException() {
        RuleDto rule = RuleDto.builder().rule("details").build();
        assertThatThrownBy(() -> ruleService.create(rule)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createRuleWithoutDetailShouldThrowAnException() {
        RuleDto rule = RuleDto.builder().name("name").build();
        assertThatThrownBy(() -> ruleService.create(rule)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void createValidRuleShouldSave() {
        RuleDto rule = RuleDto.builder().name("name").rule("details").build();
        when(ruleRepository.save(ArgumentMatchers.any())).thenReturn(Rule.builder().id(0L).build());
        Long result = ruleService.create(rule);
        assertThat(result).isEqualTo(0L);
        verify(ruleRepository).save(any());
    }

}

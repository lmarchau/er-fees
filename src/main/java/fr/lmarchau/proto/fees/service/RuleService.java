package fr.lmarchau.proto.fees.service;

import fr.lmarchau.proto.fees.dto.RuleDto;
import fr.lmarchau.proto.fees.model.Rule;
import fr.lmarchau.proto.fees.repository.RuleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Transactional
public class RuleService {

    private RuleRepository ruleRepository;

    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public Long create(RuleDto createRule) {
        if(Objects.isNull(createRule) || Objects.isNull(createRule.getName()) || Objects.isNull(createRule.getRule())) {
            throw new IllegalArgumentException("Create Rule needs name and rule");
        }
        // FIXME uniq
        Rule rule = Rule.builder().name(createRule.getName()).details(createRule.getRule()).build();
        rule = ruleRepository.save(rule);
        return rule.getId();
    }
}

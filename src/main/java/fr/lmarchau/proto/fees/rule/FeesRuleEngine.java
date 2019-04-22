package fr.lmarchau.proto.fees.rule;

import fr.lmarchau.proto.fees.dto.BusinessRelationship;
import fr.lmarchau.proto.fees.dto.RelationshipFees;
import fr.lmarchau.proto.fees.model.Rule;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;

import java.io.CharArrayReader;
import java.util.List;

@Slf4j
public class FeesRuleEngine {

    private RulesEngine rulesEngine;
    private int defaulFees;

    public FeesRuleEngine(int defaulFees) {
        this.rulesEngine = new DefaultRulesEngine();
        this.defaulFees = defaulFees;
    }

    // FIXME bad performance
    public RelationshipFees apply(BusinessRelationship relationship, List<Rule> rules) {
        log.info("Apply rules {} on {}", rules, relationship);
        RelationshipFees relationshipFees = RelationshipFees.builder().fees(defaulFees).build();
        Rules erRules = new Rules();
        rules.forEach(r -> {
            erRules.register(MVELRuleFactory.createRuleFrom(new CharArrayReader(r.getDetails().toCharArray())));
        });
        Facts facts = new Facts();
        facts.put("relationship", relationship);
        facts.put("result", relationshipFees);
        log.info("Fire Rules ");
        rulesEngine.fire(erRules, facts);
        log.info("Result {}", relationshipFees);
        return relationshipFees;
    }
}

package rule;


import fr.lmarchau.proto.fees.dto.BusinessRelationship;
import fr.lmarchau.proto.fees.dto.Member;
import fr.lmarchau.proto.fees.dto.Mission;
import fr.lmarchau.proto.fees.dto.RelationshipFees;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class SpainAndRepeatTest {

    @Test
    public void fireRuleShouldDoNothing() throws FileNotFoundException {
        BusinessRelationship relationship = BusinessRelationship.builder()
                .freelance(Member.builder().countryCode("FR").build())
                .client(Member.builder().countryCode("FR").build())
                .mission(Mission.builder().length("1months").build())
                .firstMission(LocalDateTime.now().minusMonths(1))
                .build();
        RelationshipFees result = RelationshipFees.builder().fees(10).build();
        Rules rules = new Rules();
        Rule spainAndRepeat = MVELRuleFactory.createRuleFrom(new FileReader("src/main/resources/rules/spain_and_repeat.yml"));
        rules.register(spainAndRepeat);
        RulesEngine rulesEngine = new DefaultRulesEngine();
        Facts facts = new Facts();
        facts.put("relationship", relationship);
        facts.put("result", result);
        rulesEngine.fire(rules, facts);
        assertThat(result.getFees()).isEqualTo(10);
    }

    @Test
    public void fireRuleShouldChangeFeesWithRepeat() throws FileNotFoundException {
        BusinessRelationship relationship = BusinessRelationship.builder()
                .freelance(Member.builder().countryCode("ES").build())
                .client(Member.builder().countryCode("ES").build())
                .mission(Mission.builder().length("1months").months(1).build())
                .firstMission(LocalDateTime.now().minusMonths(4))
                .build();
        RelationshipFees result = RelationshipFees.builder().fees(10).build();
        Rules rules = new Rules();
        Rule spainAndRepeat = MVELRuleFactory.createRuleFrom(new FileReader("src/main/resources/rules/spain_and_repeat.yml"));
        rules.register(spainAndRepeat);
        RulesEngine rulesEngine = new DefaultRulesEngine();
        Facts facts = new Facts();
        facts.put("relationship", relationship);
        facts.put("result", result);
        rulesEngine.fire(rules, facts);
        assertThat(result.getFees()).isEqualTo(8);
        assertThat(result.getRuleName()).isEqualTo("spain and repeat");
    }

    @Test
    public void fireRuleShouldChangeFeesWithDuration() throws FileNotFoundException {
        BusinessRelationship relationship = BusinessRelationship.builder()
                .freelance(Member.builder().countryCode("ES").build())
                .client(Member.builder().countryCode("ES").build())
                .mission(Mission.builder().length("3months").months(3).build())
                .firstMission(LocalDateTime.now().minusMonths(1))
                .build();
        RelationshipFees result = RelationshipFees.builder().fees(10).build();
        Rules rules = new Rules();
        Rule spainAndRepeat = MVELRuleFactory.createRuleFrom(new FileReader("src/main/resources/rules/spain_and_repeat.yml"));
        rules.register(spainAndRepeat);
        RulesEngine rulesEngine = new DefaultRulesEngine();
        Facts facts = new Facts();
        facts.put("relationship", relationship);
        facts.put("result", result);
        rulesEngine.fire(rules, facts);
        assertThat(result.getFees()).isEqualTo(8);
        assertThat(result.getRuleName()).isEqualTo("spain and repeat");
    }

}

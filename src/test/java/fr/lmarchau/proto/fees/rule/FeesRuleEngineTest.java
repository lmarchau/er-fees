package fr.lmarchau.proto.fees.rule;

import fr.lmarchau.proto.fees.dto.BusinessRelationship;
import fr.lmarchau.proto.fees.dto.RelationshipFees;
import fr.lmarchau.proto.fees.model.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class FeesRuleEngineTest {

    private FeesRuleEngine engine = new FeesRuleEngine(10);

    @Test
    public void applyShouldReturnValidResult() throws IOException {
        RelationshipFees result = engine.apply(BusinessRelationship.builder().build(),
                Arrays.asList(Rule.builder().details(new String(Files.readAllBytes(Paths.get("src/test/resources/rules/demo.yml")), "UTF-8")).build()));
        assertThat(result).isNotNull().extracting("fees", "ruleName").containsOnly(2, "demo");
    }

    @Test
    public void applyShouldReturnDefaultResult() throws IOException {
        RelationshipFees result = engine.apply(null,
                Arrays.asList(Rule.builder().details(new String(Files.readAllBytes(Paths.get("src/test/resources/rules/demo.yml")), "UTF-8")).build()));
        assertThat(result).isNotNull().extracting("fees", "ruleName").containsOnly(10, null);
    }
}

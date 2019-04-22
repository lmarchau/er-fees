package fr.lmarchau.proto.fees.config;

import fr.lmarchau.proto.fees.model.Rule;
import fr.lmarchau.proto.fees.repository.RuleRepository;
import fr.lmarchau.proto.fees.rule.FeesRuleEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class AppConfiguration {

    @Autowired
    private RuleRepository ruleRepository;

    @Bean
    public FeesRuleEngine feesRuleEngine(@Value("${app.fees.default}") int defaultFees) {
        return new FeesRuleEngine(defaultFees);
    }

    @PostConstruct
    public void insertRules() throws IOException {
        File ruleFile = ResourceUtils.getFile("classpath:rules/spain_and_repeat.yml");
        Rule rule = Rule.builder().name("spain_and_repeat").details(new String(Files.readAllBytes(ruleFile.toPath()))).build();
        ruleRepository.save(rule);
    }
}

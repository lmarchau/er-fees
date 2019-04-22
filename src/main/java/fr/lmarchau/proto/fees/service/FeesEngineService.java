package fr.lmarchau.proto.fees.service;

import fr.lmarchau.proto.fees.dto.BusinessRelationship;
import fr.lmarchau.proto.fees.dto.RelationshipFees;
import fr.lmarchau.proto.fees.model.Rule;
import fr.lmarchau.proto.fees.repository.IpStackRepository;
import fr.lmarchau.proto.fees.repository.RuleRepository;
import fr.lmarchau.proto.fees.rule.FeesRuleEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@Transactional
@Slf4j
public class FeesEngineService {

    private static final Pattern MONTH_REGEX = Pattern.compile("[0-9]+month[s]?", Pattern.CASE_INSENSITIVE);

    private RuleRepository ruleRepository;
    private IpStackRepository ipStackRepository;
    private FeesRuleEngine engine;

    public FeesEngineService(RuleRepository ruleRepository, IpStackRepository ipStackRepository, FeesRuleEngine engine) {
        this.ruleRepository = ruleRepository;
        this.ipStackRepository = ipStackRepository;
        this.engine = engine;
    }

    public RelationshipFees compute(BusinessRelationship relationship) {
        log.info("Check relationship Parameters {}", relationship);
        if (Objects.isNull(relationship)
                || Objects.isNull(relationship.getClient())
                || Objects.isNull(relationship.getClient().getIp())
                || Objects.isNull(relationship.getFreelance())
                || Objects.isNull(relationship.getFreelance().getIp())
                || Objects.isNull(relationship.getMission())) {
            log.error("Invalid Parameters {}", relationship);
            throw new IllegalArgumentException("Invalid parameters");
        }
        // FIXME permit other unit
        if (MONTH_REGEX.matcher(relationship.getMission().getLength()).matches()) {
            relationship.getMission().setMonths(Integer.valueOf(relationship.getMission().getLength().replaceAll("\\D", "")));
        } else {
            log.error("Invalid mission length {}", relationship.getMission().getLength());
            throw new IllegalArgumentException("Invalid mission length, only month pattern supported");
        }

        log.info("Client GeoLoc");
        relationship.getClient().setCountryCode(ipStackRepository.findCountryCodeByIp(relationship.getClient().getIp()));
        log.info("Freelance GeoLoc");
        relationship.getFreelance().setCountryCode(ipStackRepository.findCountryCodeByIp(relationship.getFreelance().getIp()));
        List<Rule> rules = ruleRepository.findAll();
        return engine.apply(relationship, rules);
    }
}

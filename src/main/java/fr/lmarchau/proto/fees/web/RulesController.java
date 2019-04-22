package fr.lmarchau.proto.fees.web;

import fr.lmarchau.proto.fees.dto.RuleDto;
import fr.lmarchau.proto.fees.service.RuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/rules")
public class RulesController {

    private RuleService ruleService;

    public RulesController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PutMapping
    public ResponseEntity<RuleDto> create(@RequestBody RuleDto rule) {
        ruleService.create(rule);
        return ResponseEntity.created(URI.create("/rules/" + rule.getName())).body(rule);
    }

}

package fr.lmarchau.proto.fees.web;

import fr.lmarchau.proto.fees.dto.BusinessRelationship;
import fr.lmarchau.proto.fees.dto.RelationshipFees;
import fr.lmarchau.proto.fees.service.FeesEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fees")
public class FeesController {

    private FeesEngineService feesEngineService;

    public FeesController(FeesEngineService feesEngineService) {
        this.feesEngineService = feesEngineService;
    }

    @GetMapping("/compute")
    public ResponseEntity<RelationshipFees> compute(@RequestBody BusinessRelationship businessRelationship) {
        return ResponseEntity.ok(feesEngineService.compute(businessRelationship));
    }

}

package fr.lmarchau.proto.fees.repository;

import fr.lmarchau.proto.fees.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {

}

package fr.lmarchau.proto.fees.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessRelationship {

    private Member client;
    private Member freelance;
    private Mission mission;

    private LocalDateTime firstMission;
    private LocalDateTime lastMission;

}

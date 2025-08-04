package com.accepted.matches.model.dto;

import com.accepted.matches.enums.Sport;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MatchDto {
    private Long id;
    private String description;
    private LocalDate matchDate;
    private LocalTime matchTime;
    private String teamA;
    private String teamB;
    private Sport sport;
   /* private List<Long> matchOddsIds;*/
}

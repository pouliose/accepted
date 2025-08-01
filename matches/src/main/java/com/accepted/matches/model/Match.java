package com.accepted.matches.model;

import com.accepted.matches.enums.Sport;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Match {

    private Long id;
    private String description;
    private String matchDate;
    private String matchTime;
    private String teamA;
    private String teamB;
    private Sport sport;
}

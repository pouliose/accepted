package com.accepted.matches.utils;

import com.accepted.matches.enums.Specifier;
import com.accepted.matches.enums.Sport;
import com.accepted.matches.model.dto.MatchDto;
import com.accepted.matches.model.dto.MatchOddsDto;
import com.accepted.matches.model.entities.Match;
import com.accepted.matches.model.entities.MatchOdds;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class CreateTestData {

    public static Match createMatchA() {
        return Match.builder()
                .id(1L)
                .description("OSFP-PAO")
                .matchDate(java.time.LocalDate.of(2025, 10, 1))
                .matchTime(java.time.LocalTime.of(20, 0))
                .teamA("OSFP")
                .teamB("PAO")
                .sport(Sport.FOOTBALL)
                .build();
    }

    public static MatchDto createMatchADto() {
        return MatchDto.builder()
                .id(1L)
                .description("OSFP-PAO")
                .matchDate(java.time.LocalDate.of(2025, 10, 1))
                .matchTime(java.time.LocalTime.of(20, 0))
                .teamA("OSFP")
                .teamB("PAO")
                .sport(Sport.FOOTBALL)
                .build();
    }

    public static MatchOdds createMatchOddsA(Match match) {
        return MatchOdds.builder()
                .id(1L)
                .specifier(Specifier.ONE)
                .odd(new BigDecimal(1.10))
                .match(match)
                .build();
    }

    public static MatchOddsDto createMatchOddsADto(MatchDto match) {
        return MatchOddsDto.builder()
                .id(1L)
                .specifier(Specifier.ONE)
                .odd(new BigDecimal(1.10))
                .match(match)
                .build();
    }
}

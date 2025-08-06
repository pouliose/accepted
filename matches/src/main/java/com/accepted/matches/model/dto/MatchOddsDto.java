package com.accepted.matches.model.dto;

import com.accepted.matches.enums.Specifier;
import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MatchOddsDto {
    private Long id;
    private Specifier specifier;
    private BigDecimal odd;
    private Long match;
}

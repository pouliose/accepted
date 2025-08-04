package com.accepted.matches.model.entities;

import com.accepted.matches.enums.Specifier;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@Entity
@Table(name="matchOdds")
public class MatchOdds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @org.hibernate.validator.constraints.Range(min = 1, message = "ID must be greater than zero.")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Specifier specifier;
    @Column(precision = 5, scale = 3)
    private BigDecimal odd;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "match_id", nullable=false)
    private Match match;
}

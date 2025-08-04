package com.accepted.matches.model.entities;

import com.accepted.matches.enums.Sport;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @org.hibernate.validator.constraints.Range(min = 1, message = "ID must be greater than zero.")
    private Long id;
    private String description;
    private LocalDate matchDate;
    private LocalTime matchTime;
    private String teamA;
    private String teamB;

    @Enumerated(EnumType.STRING)
    private Sport sport;

    @OneToMany(mappedBy = "match") //TODO define cascade strategy
    private Set<MatchOdds> matchOdds;
}

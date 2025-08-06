package com.accepted.matches.repositories;

import com.accepted.matches.model.entities.MatchOdds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchOddsRepository extends JpaRepository<MatchOdds, Long> {
    Page<MatchOdds> findByMatchId(Long matchId, Pageable pageable);
}

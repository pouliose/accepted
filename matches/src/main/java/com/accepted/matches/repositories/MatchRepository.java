package com.accepted.matches.repositories;

import com.accepted.matches.enums.Sport;
import com.accepted.matches.model.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("SELECT m FROM Match m WHERE m.sport = :sport")
    List<Match> findMatchesBySport(Sport sport) ;
}

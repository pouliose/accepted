package com.accepted.matches.services;

import com.accepted.matches.model.entities.Match;
import com.accepted.matches.repositories.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Optional<Match> findById(Long id) {
        return matchRepository.findById(id);
    }
}

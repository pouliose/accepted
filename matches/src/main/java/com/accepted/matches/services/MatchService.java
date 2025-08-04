package com.accepted.matches.services;

import com.accepted.matches.exceptions.MatchNotFoundException;
import com.accepted.matches.model.entities.Match;
import com.accepted.matches.repositories.MatchRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Match findById(Long id) {

        Optional<Match> match = matchRepository.findById(id);
        if(match.isEmpty()) {
            throw new MatchNotFoundException(String.format("Match with %d does not exist.", id));
        }
        return match.get();
    }

    public Page<Match> findAll(Pageable pageable) {
        return matchRepository.findAll(pageable);
    }

    public Match createMatch(Match match) {
        return matchRepository.save(match);
    }

    public Match updateMatch(Long id, Match match) throws BadRequestException {
        if (!matchRepository.existsById(id)) {
            throw new BadRequestException("Match with id " + id + " does not exist.");
        }
        match.setId(id);
        return matchRepository.save(match);
    }

    public void deleteMatch(Long id) throws BadRequestException {
        if (!matchRepository.existsById(id)) {
            throw new BadRequestException("Match with id " + id + " does not exist.");
        }
        matchRepository.deleteById(id);
    }

    public boolean existsMatchById(Long matchId) {
        if (matchId == null) {
            throw new IllegalArgumentException("Match ID cannot be null.");
        }
        return matchRepository.existsById(matchId);
    }
}

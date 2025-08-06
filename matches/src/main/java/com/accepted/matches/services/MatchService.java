package com.accepted.matches.services;

import com.accepted.matches.exceptions.MatchNotFoundException;
import com.accepted.matches.mappers.Mapper;
import com.accepted.matches.model.dto.MatchDto;
import com.accepted.matches.model.entities.Match;
import com.accepted.matches.repositories.MatchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final Mapper<Match, MatchDto> matchMapper;

    public MatchService(MatchRepository matchRepository, Mapper<Match, MatchDto> matchMapper) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
    }

    public MatchDto findById(Long id) {

        Optional<Match> match = matchRepository.findById(id);
        if(match.isEmpty()) {
            throw new MatchNotFoundException(String.format("Match with %d does not exist.", id));
        }
        return matchMapper.mapTo(match.get());
    }

    public Page<MatchDto> findAll(Pageable pageable) {
        Page<Match> matches = matchRepository.findAll(pageable);
        Page<MatchDto> matchResponseDtos = matches.map(matchMapper::mapTo);
        return matchResponseDtos;
    }

    public MatchDto createMatch(MatchDto matchDto) {
        Match match = matchMapper.mapFrom(matchDto);
        Match matchSaved = matchRepository.save(match);
        MatchDto matchDtoSaved = matchMapper.mapTo(matchSaved);

        return matchDtoSaved;
    }

    public MatchDto updateMatch(Long id, MatchDto matchDto) {
        if (!matchRepository.existsById(id)) {
            throw new MatchNotFoundException("Match with id " + id + " does not exist.");
        }
        Match match = matchMapper.mapFrom(matchDto);

        match.setId(id);
        Match matchUpdated = matchRepository.save(match);
        MatchDto matchDtoUpdated = matchMapper.mapTo(matchUpdated);
        return matchDtoUpdated;
    }

    public void deleteMatch(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new MatchNotFoundException("Match with id " + id + " does not exist.");
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

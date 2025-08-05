package com.accepted.matches.services;

import com.accepted.matches.exceptions.MatchNotFoundException;
import com.accepted.matches.exceptions.MatchOddsNotFoundException;
import com.accepted.matches.mappers.Mapper;
import com.accepted.matches.model.dto.MatchOddsDto;
import com.accepted.matches.model.entities.Match;
import com.accepted.matches.model.entities.MatchOdds;
import com.accepted.matches.repositories.MatchOddsRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchOddsService {

    private final MatchOddsRepository matchOddsRepository;
    private final MatchService matchService;
    private final Mapper<MatchOdds, MatchOddsDto> matchOddsMapper;

    public MatchOddsService(MatchOddsRepository matchOddsRepository, MatchService matchService, Mapper<MatchOdds, MatchOddsDto> matchOddsMapper) {
        this.matchOddsRepository = matchOddsRepository;
        this.matchService = matchService;
        this.matchOddsMapper = matchOddsMapper;
    }

    public MatchOddsDto findByMatchOddsId(Long id) {

        Optional<MatchOdds> matchOdds = matchOddsRepository.findById(id);
        if (matchOdds.isEmpty()) {
            throw new MatchOddsNotFoundException(String.format("MatchOdds with %d does not exist.", id));
        }
        MatchOdds matchOddsFound = matchOdds.get();
        return getMatchOddsDto(matchOddsFound);
    }

    public Page<MatchOddsDto> findAll(Pageable pageable) {
        Page<MatchOdds> matchOdds = matchOddsRepository.findAll(pageable);

        Page<MatchOddsDto> matchResponseDtos = matchOdds.map(matchOdd -> getMatchOddsDto(matchOdd));
        return matchResponseDtos;
    }

    public Page<MatchOddsDto> findMatchOddsByMatchId(Long matchId, Pageable pageable) {
        Page<MatchOdds> matchOdds = matchOddsRepository.findByMatchId(matchId, pageable);

        //Page<MatchOddsDto> matchOddsDtos = matchOdds.map(matchOddsMapper::mapTo); TODO
        Page<MatchOddsDto> matchOddsDtos = matchOdds.map(MatchOddsService::getMatchOddsDto);

        return matchOddsDtos;
    }

    private static MatchOddsDto getMatchOddsDto(MatchOdds matchOdd) {
        MatchOddsDto dto = new MatchOddsDto();
        dto.setId(matchOdd.getId());
        dto.setSpecifier(matchOdd.getSpecifier());
        dto.setOdd(matchOdd.getOdd());
        dto.setMatch(matchOdd.getMatch().getId());
        return dto;
    }

    public MatchOddsDto createMatchOdds(Long matchIdParamVar, MatchOddsDto matchOddsRequestDto) throws BadRequestException {
        Long matchId = matchOddsRequestDto.getMatch();

        validateIdParamVarOverBodyField(matchIdParamVar, matchId, "Match");

        Match match = matchService.findById(matchId);
        MatchOdds matchOdds = matchOddsMapper.mapFrom(matchOddsRequestDto);
        matchOdds.setMatch(match);

        MatchOdds matchOddsSaved = matchOddsRepository.save(matchOdds);

        // Prepare the response DTO
        MatchOddsDto matchOddsResponseDto = matchOddsRequestDto;
        matchOddsResponseDto.setId(matchOddsSaved.getId());

        return matchOddsResponseDto;
    }

    private void validateIdParamVarOverBodyField(Long idParamVar, Long idFromBody, String entityName) throws BadRequestException {
        if (idFromBody == null || idFromBody <= 0) {
            throw new BadRequestException(String.format("%s ID in the request body must be provided and greater than zero.", entityName));
        }
        if (idParamVar <= 0) {
            throw new BadRequestException(String.format("%s ID as request parameter must be greater than zero.", entityName));
        }
        if (!idFromBody.equals(idParamVar)) {
            throw new BadRequestException(String.format("%s ID in in the request body defers from the request parameter.", entityName));
        }
    }

    public MatchOddsDto updateMatchOdds(Long id, MatchOddsDto matchOddsDto) throws BadRequestException {
        if (!matchOddsRepository.existsById(id)) {
            throw new MatchOddsNotFoundException(String.format("MatchOdds with id %d does not exist.", id));
        }

        validateIdParamVarOverBodyField(id, matchOddsDto.getId(), "MatchOdds");
        MatchOdds matchOdds = getMatchOdds(matchOddsDto);//matchOddsMapper.mapFrom(matchOddsDto);

        MatchOdds matchOddsUpdated = matchOddsRepository.save(matchOdds);
        //MatchOddsDto matchOddsDtoUpdated = matchOddsMapper.mapTo(matchOddsUpdated);
        MatchOddsDto matchOddsDtoUpdated = getMatchOddsDto(matchOddsUpdated);

        return matchOddsDtoUpdated;
    }

    private MatchOdds getMatchOdds(MatchOddsDto matchOddsDto) {
        MatchOdds matchOdds = new MatchOdds();
        matchOdds.setId(matchOddsDto.getId());
        matchOdds.setSpecifier(matchOddsDto.getSpecifier());
        matchOdds.setOdd(matchOddsDto.getOdd());

        Match match = matchService.findById(matchOddsDto.getMatch());
        matchOdds.setMatch(match);

        return matchOdds;
    }

    public void deleteMatchOdds(Long id) {
        if (!matchOddsRepository.existsById(id)) {
            throw new MatchNotFoundException("MatchOdds with id " + id + " does not exist.");
        }
        matchOddsRepository.deleteById(id);
    }
}

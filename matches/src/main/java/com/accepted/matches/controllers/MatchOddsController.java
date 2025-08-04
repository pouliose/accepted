package com.accepted.matches.controllers;

import com.accepted.matches.mappers.Mapper;
import com.accepted.matches.model.dto.MatchOddsDto;
import com.accepted.matches.model.entities.MatchOdds;
import com.accepted.matches.services.MatchOddsService;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1")
public class MatchOddsController {

    private final MatchOddsService matchOddsService;

    private final Mapper<MatchOdds, MatchOddsDto> matchOddsMapper;

    @GetMapping(path = "/matchesodds/{id}")
    public ResponseEntity<MatchOddsDto> getMatchOddsById(@PathVariable("id") Long id) {
        MatchOddsDto matchOddsDto = matchOddsService.findByMatchOddsId(id);

        return new ResponseEntity<>(matchOddsDto, HttpStatus.OK);
    }

    @GetMapping(path = "/matchesodds")
    public ResponseEntity<Page<MatchOddsDto>> getAllMatchesOdds(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        Page<MatchOddsDto> matchOddsDto = matchOddsService.findAll(pageable);


        // Page<MatchOddsDto> matchResponseDtos = matches.map(matchOddsMapper::mapTo); TODO Converter org.modelmapper failed to convert Match to Long.
        return new ResponseEntity<>(matchOddsDto, HttpStatus.OK);
    }

    @GetMapping(path = "/matches/{matchId}/odds")
    public ResponseEntity<Page<MatchOddsDto>> getMatchOddsByMatchId(
            @PathVariable("matchId") Long matchId,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<MatchOddsDto> matchOddsDtos = matchOddsService.findMatchOddsByMatchId(matchId, pageable);
        return new ResponseEntity<>(matchOddsDtos, HttpStatus.OK);
    }

    @PostMapping(path = "/matches/{matchId}/odds")
    public ResponseEntity<MatchOddsDto> createMatch(@PathVariable Long matchId, @RequestBody MatchOddsDto matchOddsDto) throws BadRequestException {
        MatchOddsDto matchOddsDtoSaved = matchOddsService.createMatchOdds(matchId, matchOddsDto);
        return new ResponseEntity<>(matchOddsDtoSaved, HttpStatus.CREATED);
    }

    @PutMapping(path = "/matchesodds/{id}")
    public ResponseEntity<MatchOddsDto> updateMatchOdds(
            @PathVariable("id") Long id,
            @RequestBody MatchOddsDto matchOddsDto) throws BadRequestException {

        MatchOddsDto matchOddsDtoUpdated = matchOddsService.updateMatchOdds(id, matchOddsDto);
        return new ResponseEntity<>(matchOddsDtoUpdated, HttpStatus.OK);
    }

    @DeleteMapping(path = "/matchesodds/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable("id") Long id) {
        try {
            matchOddsService.deleteMatch(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}

package com.accepted.matches.controllers;

import com.accepted.matches.mappers.Mapper;
import com.accepted.matches.model.dto.MatchDto;
import com.accepted.matches.model.entities.Match;
import com.accepted.matches.services.MatchService;
import lombok.AllArgsConstructor;
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
public class MatchController {

    private final MatchService matchService;

    private final Mapper<Match, MatchDto> matchMapper;

    @GetMapping(path = "/matches/{id}")
    public ResponseEntity<MatchDto> getMatchById(@PathVariable("id") Long id) {
        MatchDto matchResult = matchService.findById(id);

        return new ResponseEntity<>(matchResult, HttpStatus.OK);
    }

    @GetMapping(path = "/matches")
    public ResponseEntity<Page<MatchDto>> getAllMatches(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        Page<MatchDto> matches = matchService.findAll(pageable);

        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @PostMapping(path = "/matches")
    public ResponseEntity<MatchDto> createMatch(@RequestBody MatchDto matchRequestDto) {
        MatchDto matchSaved = matchService.createMatch(matchRequestDto);
        return new ResponseEntity<>(matchSaved, HttpStatus.CREATED);
    }

    @PutMapping(path = "/matches/{id}")
    public ResponseEntity<MatchDto> updateMatch(
            @PathVariable("id") Long id,
            @RequestBody MatchDto matchDto) {
        MatchDto matchDtoUpdated = matchService.updateMatch(id, matchDto);
        return new ResponseEntity<>(matchDtoUpdated, HttpStatus.OK);
    }

    @DeleteMapping(path = "/matches/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable("id") Long id) {
        matchService.deleteMatch(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

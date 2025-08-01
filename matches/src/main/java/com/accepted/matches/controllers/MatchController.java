package com.accepted.matches.controllers;

import com.accepted.matches.mappers.Mapper;
import com.accepted.matches.model.dto.MatchDto;
import com.accepted.matches.model.entities.Match;
import com.accepted.matches.services.MatchService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/matches")
public class MatchController {

    private final MatchService matchService;

    private final Mapper<Match, MatchDto> matchMapper;
    //private final ModelMapper modelMapper;


    @GetMapping(path = "/{id}")
    public ResponseEntity<MatchDto> getMatchById(@PathVariable("id") Long id) {
        Optional<Match> matchResult = matchService.findById(id);

        return matchResult.map(match -> {
                    MatchDto matchDto = matchMapper.mapTo(match);
                    return new ResponseEntity<>(matchDto, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

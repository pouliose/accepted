package com.accepted.matches.mappers.impl;

import com.accepted.matches.mappers.Mapper;
import com.accepted.matches.model.dto.MatchDto;
import com.accepted.matches.model.entities.Match;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

@Component
public class MatchMapperImpl implements Mapper<Match, MatchDto> {
    private ModelMapper modelMapper;

    public MatchMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public MatchDto mapTo(Match match) {
        return modelMapper.map(match, MatchDto.class);
    }

    @Override
    public Match mapFrom(MatchDto matchDto) {
        return modelMapper.map(matchDto, Match.class);
    }
}

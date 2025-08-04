package com.accepted.matches.mappers.impl;

import com.accepted.matches.mappers.Mapper;
import com.accepted.matches.model.dto.MatchOddsDto;
import com.accepted.matches.model.entities.MatchOdds;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MatchOddsMapperImpl implements Mapper<MatchOdds, MatchOddsDto> {
    private ModelMapper modelMapper;

    public MatchOddsMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public MatchOddsDto mapTo(MatchOdds match) {
        return modelMapper.map(match, MatchOddsDto.class);
    }

    @Override
    public MatchOdds mapFrom(MatchOddsDto matchDto) {
        return modelMapper.map(matchDto, MatchOdds.class);
    }
}

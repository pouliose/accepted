package com.accepted.matches.services;

import com.accepted.matches.enums.Specifier;
import com.accepted.matches.exceptions.MatchOddsNotFoundException;
import com.accepted.matches.mappers.Mapper;
import com.accepted.matches.model.dto.MatchDto;
import com.accepted.matches.model.dto.MatchOddsDto;
import com.accepted.matches.model.entities.Match;
import com.accepted.matches.model.entities.MatchOdds;
import com.accepted.matches.repositories.MatchOddsRepository;
import com.accepted.matches.repositories.MatchRepository;
import com.accepted.matches.utils.CreateTestData;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static com.accepted.matches.utils.CreateTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(MatchOddsService.class)
public class MatchOddsServiceTests {
    @MockitoBean
    private final MatchOddsRepository matchOddsRepository;
    @MockitoBean
    private final MatchRepository matchRepository;
    @MockitoBean
    private final MatchService matchService;
    private final MatchOddsService matchOddsService;

    @MockitoBean
    private final Mapper<MatchOdds, MatchOddsDto> matchOddsMapper;
    @MockitoBean
    private final Mapper<Match, MatchDto> matchMapper;

    @Autowired
    public MatchOddsServiceTests(MatchOddsRepository matchOddsRepository, MatchRepository matchRepository, MatchService matchService, MatchOddsService matchOddsService, Mapper<MatchOdds, MatchOddsDto> matchOddsMapper, Mapper<Match, MatchDto> matchMapper) {
        this.matchOddsRepository = matchOddsRepository;
        this.matchRepository = matchRepository;
        this.matchService = matchService;
        this.matchOddsService = matchOddsService;
        this.matchOddsMapper = matchOddsMapper;
        this.matchMapper = matchMapper;
    }

    @Test
    public void testFindMatchOddsByIdReturnsMatchOddsWhenExists() {
        Match matchA = CreateTestData.createMatchA();
        MatchOdds matchOddsA = CreateTestData.createMatchOddsA(matchA);
        MatchDto matchADto = createMatchADto();

        MatchOddsDto matchOddsADto = createMatchOddsADto(matchADto);

        Mockito.when(matchOddsRepository.findById(1L)).thenReturn(Optional.of(matchOddsA));

        MatchOddsDto result = matchOddsService.findByMatchOddsId(1L);

        assertNotNull(result);
        assertEquals(matchOddsADto, result);
        Mockito.verify(matchOddsRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void testFindMatchOddsByIdThrowsExceptionWhenMatchOddsIdDoesNotExists() {
        Mockito.when(matchOddsRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(MatchOddsNotFoundException.class, () -> matchOddsService.findByMatchOddsId(1L));
        Mockito.verify(matchOddsRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void testFindAllMatchOddsReturnsPageableResults() {
        Match matchA = CreateTestData.createMatchA();
        MatchOdds matchOddsA = CreateTestData.createMatchOddsA(matchA);
        MatchDto matchADto = createMatchADto();

        MatchOddsDto matchOddsADto = createMatchOddsADto(matchADto);

        Pageable pageable = PageRequest.of(0, 10);

        List<MatchOdds> matches = List.of(matchOddsA);
        Page<MatchOdds> matchOddsPage = new org.springframework.data.domain.PageImpl<>(matches);

        Mockito.when(matchOddsRepository.findAll(pageable)).thenReturn(matchOddsPage);

        Page<MatchOddsDto> result = matchOddsService.findAll(pageable);

        assertEquals(1, result.getSize());
        assertEquals(matchOddsADto, result.stream().findFirst().get());
        Mockito.verify(matchOddsRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void testFindMatchOddsByMatchIdReturnsPageableResults() {
        Match matchA = CreateTestData.createMatchA();
        MatchDto matchADto = createMatchADto();
        MatchOdds matchOddsA = CreateTestData.createMatchOddsA(matchA);
        MatchOdds matchOddsB = CreateTestData.createMatchOddsB(matchA);

        MatchOddsDto matchOddsADto = createMatchOddsADto(matchADto);
        MatchOddsDto matchOddsBDto = createMatchOddsBDto(matchADto);

        List<MatchOddsDto> matchOddsDtoList = List.of(matchOddsADto, matchOddsBDto);


        Pageable pageable = PageRequest.of(0, 10);

        List<MatchOdds> matchOddsList = List.of(matchOddsA, matchOddsB);
        Page<MatchOdds> matchOddsPage = new org.springframework.data.domain.PageImpl<>(matchOddsList);

        Mockito.when(matchOddsRepository.findByMatchId(matchA.getId(), pageable)).thenReturn(matchOddsPage);

        Page<MatchOddsDto> result = matchOddsService.findMatchOddsByMatchId(matchA.getId(), pageable);

        assertEquals(2, result.getSize());
        assertIterableEquals(matchOddsDtoList, result.getContent());
        Mockito.verify(matchOddsRepository, Mockito.times(1)).findByMatchId(matchA.getId(), pageable);
    }

    @Test
    public void testCreateMatchOddsSavesAndReturnsMatchOdds() throws BadRequestException {
        Match matchA = CreateTestData.createMatchA();
        MatchOdds matchOddsA = CreateTestData.createMatchOddsA(matchA);
        MatchDto matchADto = createMatchADto();
        MatchOddsDto matchOddsADto = createMatchOddsADto(matchADto);

        Mockito.when(matchOddsRepository.save(matchOddsA)).thenReturn(matchOddsA);
        Mockito.when(matchService.existsMatchById(matchA.getId())).thenReturn(true);

        Mockito.when(matchOddsMapper.mapFrom(matchOddsADto)).thenReturn(matchOddsA);
        MatchOddsDto matchOddsDtoSaved = matchOddsService.createMatchOdds(matchOddsA.getMatch().getId(), matchOddsADto);

        assertNotNull(matchOddsDtoSaved);
        assertEquals(matchOddsADto, matchOddsDtoSaved);
        Mockito.verify(matchOddsRepository, Mockito.times(1)).save(matchOddsA);
    }

    @Test
    public void testUpdateMatchOddsUpdatesAndReturnsUpdatedMatchOdds() throws BadRequestException {
        Match matchA = CreateTestData.createMatchA();
        MatchDto matchADto = createMatchADto();
        MatchOdds matchOddsA = CreateTestData.createMatchOddsA(matchA);

        MatchOdds matchOddsAChanged = matchOddsA;
        matchOddsAChanged.setOdd(matchOddsA.getOdd().add(new java.math.BigDecimal("7.00")));
        matchOddsAChanged.setSpecifier(Specifier.TWO);

        MatchOddsDto matchOddsADto = createMatchOddsADto(matchADto);
        MatchOddsDto matchOddsAChangedDto = matchOddsADto;
        matchOddsAChangedDto.setOdd(matchOddsADto.getOdd().add(new java.math.BigDecimal("7.00")));
        matchOddsAChangedDto.setSpecifier(Specifier.TWO);

        Mockito.when(matchOddsMapper.mapFrom(matchOddsAChangedDto)).thenReturn(matchOddsAChanged);
        Mockito.when(matchOddsRepository.existsById(matchOddsA.getId())).thenReturn(true);
        Mockito.when(matchService.findById(matchOddsA.getMatch().getId())).thenReturn(matchADto);
        Mockito.when(matchOddsRepository.save(Mockito.any(MatchOdds.class))).thenReturn(matchOddsAChanged);
        Mockito.when(matchRepository.findById(matchOddsA.getMatch().getId())).thenReturn(Optional.of(matchA));

        MatchOddsDto matchOddsDtoUpdated = matchOddsService.updateMatchOdds(matchOddsA.getId(), matchOddsAChangedDto);

        assertNotNull(matchOddsDtoUpdated);
        assertEquals(matchOddsAChangedDto, matchOddsDtoUpdated);
        Mockito.verify(matchOddsRepository, Mockito.times(1)).existsById(matchOddsA.getId());
        Mockito.verify(matchOddsRepository, Mockito.times(1)).save(matchOddsAChanged);
    }

    @Test
    public void testUpdateMatchOddsThrowsMatchOddsNotFoundExceptionWhenMatchOddsDoesNotExist() {
        MatchDto matchADto = createMatchADto();
        MatchOddsDto matchOddsADto = createMatchOddsADto(matchADto);

        Mockito.when(matchOddsRepository.existsById(matchOddsADto.getId())).thenReturn(false);

        assertThrows(MatchOddsNotFoundException.class, () -> matchOddsService.updateMatchOdds(matchOddsADto.getId(), matchOddsADto));

        Mockito.verify(matchOddsRepository, Mockito.times(1)).existsById(matchOddsADto.getId());
    }

    @Test
    public void testDeleteMatchOddsDeletesMatchOddsWhenExists() {
        long matchId = 1L;
        Mockito.when(matchOddsRepository.existsById(matchId)).thenReturn(true);

        matchOddsService.deleteMatchOdds(matchId);

        Mockito.verify(matchOddsRepository, Mockito.times(1)).existsById(matchId);
        Mockito.verify(matchOddsRepository, Mockito.times(1)).deleteById(matchId);
    }

    @Test
    public void testDeleteMatchThrowsMatchOddsNotFoundExceptionWhenMatchOddsDoesNotExist() {
        long matchOddId = 1L;
        Mockito.when(matchOddsRepository.existsById(matchOddId)).thenReturn(false);

        assertThrows(MatchOddsNotFoundException.class, () -> matchOddsService.deleteMatchOdds(matchOddId));

        Mockito.verify(matchOddsRepository, Mockito.times(1)).existsById(matchOddId);
    }
}

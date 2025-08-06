package com.accepted.matches.services;

import com.accepted.matches.exceptions.MatchNotFoundException;
import com.accepted.matches.mappers.Mapper;
import com.accepted.matches.model.dto.MatchDto;
import com.accepted.matches.model.entities.Match;
import com.accepted.matches.repositories.MatchRepository;
import com.accepted.matches.utils.CreateTestData;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(MatchService.class)
public class MatchServiceTests {
    @MockitoBean
    private final MatchRepository matchRepository;

    private final MatchService matchService;
    @MockitoBean
    private final Mapper<Match, MatchDto> matchMapper;

    @Autowired
    public MatchServiceTests(MatchRepository matchRepository, MatchService matchService, Mapper<Match, MatchDto> matchMapper) {
        this.matchRepository = matchRepository;
        this.matchService = matchService;
        this.matchMapper = matchMapper;
    }

    @Test
    public void testFindMatchByIdReturnsMatchWhenExists() {
        Match matchA = CreateTestData.createMatchA();
        MatchDto matchADto = CreateTestData.createMatchADto();

        Mockito.when(matchRepository.findById(1L)).thenReturn(Optional.of(matchA));
        Mockito.when(matchMapper.mapTo(matchA)).thenReturn(matchADto);

        MatchDto result = matchService.findById(1L);

        assertNotNull(result);
        assertEquals(matchADto, result);
        Mockito.verify(matchRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void testFindMatchByIdThrowsExceptionWhenMatchIdDoesNotExists() {
        Mockito.when(matchRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(MatchNotFoundException.class, () -> matchService.findById(1L));
        Mockito.verify(matchRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void testFindAllMatchesReturnsPageableResults() {
        Match matchA = CreateTestData.createMatchA();
        MatchDto matchADto = CreateTestData.createMatchADto();

        Pageable pageable = PageRequest.of(0, 10);

        List<Match> matches = List.of(matchA);
        List<MatchDto> matchesDto = List.of(matchADto);
        Page<Match> matchPage = new org.springframework.data.domain.PageImpl<>(matches);


        Mockito.when(matchRepository.findAll(pageable)).thenReturn(matchPage);
        Mockito.when(matchMapper.mapTo(matchA)).thenReturn(matchADto);

        Page<MatchDto> result = matchService.findAll(pageable);

        assertEquals(1, result.getSize());
        System.out.println("Expected: " + matchADto);
        System.out.println("Actual value: " + result.stream().findFirst().get().getDescription() + " " + result.stream().findFirst().get().getId() + " " + result.stream().findFirst().get().getMatchDate() + " " + result.stream().findFirst().get().getMatchTime() + " " + result.stream().findFirst().get().getSport());
        assertTrue(matchADto.equals(result.stream().findFirst().get()));
        Mockito.verify(matchRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void testCreateMatchSavesAndReturnsMatch() {
        Match matchA = CreateTestData.createMatchA();
        MatchDto matchADto = CreateTestData.createMatchADto();

        Mockito.when(matchRepository.save(matchA)).thenReturn(matchA);
        Mockito.when(matchMapper.mapFrom(matchADto)).thenReturn(matchA);
        Mockito.when(matchMapper.mapTo(matchA)).thenReturn(matchADto);

        MatchDto matchDtoSaved = matchService.createMatch(matchADto);
        assertNotNull(matchDtoSaved);
        assertEquals(matchADto, matchDtoSaved);
        Mockito.verify(matchRepository, Mockito.times(1)).save(matchA);
    }

    @Test
    public void testUpdateMatchUpdatesAndReturnsMatch() {
        Match matchA = CreateTestData.createMatchA();
        MatchDto matchADto = CreateTestData.createMatchADto();

        Mockito.when(matchRepository.existsById(1L)).thenReturn(true);
        Mockito.when(matchRepository.save(matchA)).thenReturn(matchA);
        Mockito.when(matchMapper.mapFrom(matchADto)).thenReturn(matchA);
        Mockito.when(matchMapper.mapTo(matchA)).thenReturn(matchADto);

        MatchDto matchDtoUpdated = matchService.updateMatch(1L, matchADto);

        assertNotNull(matchDtoUpdated);
        assertEquals(matchADto, matchDtoUpdated);
        Mockito.verify(matchRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(matchRepository, Mockito.times(1)).save(matchA);
    }

    @Test
    public void testUpdateMatchThrowsMatchNotFoundExceptionWhenMatchDoesNotExist() {
        Match matchA = CreateTestData.createMatchA();
        MatchDto matchADto = CreateTestData.createMatchADto();

        Mockito.when(matchRepository.existsById(matchA.getId())).thenReturn(false);

        assertThrows(MatchNotFoundException.class, () -> matchService.updateMatch(matchA.getId(), matchADto));

        Mockito.verify(matchRepository, Mockito.times(1)).existsById(matchA.getId());
    }

    @Test
    public void testDeleteMatchDeletesMatchWhenExists() {
        Mockito.when(matchRepository.existsById(1L)).thenReturn(true);

        matchService.deleteMatch(1L);

        Mockito.verify(matchRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(matchRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteMatchThrowsMatchNotFoundExceptionWhenMatchDoesNotExist() {
        long matchId = 1L;
        Mockito.when(matchRepository.existsById(matchId)).thenReturn(false);

        assertThrows(MatchNotFoundException.class, () -> matchService.deleteMatch(matchId));

        Mockito.verify(matchRepository, Mockito.times(1)).existsById(matchId);
    }
}

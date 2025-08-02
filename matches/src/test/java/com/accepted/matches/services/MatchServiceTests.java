package com.accepted.matches.services;

import com.accepted.matches.exceptions.MatchNotFoundException;
import com.accepted.matches.model.entities.Match;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(MatchService.class)
public class MatchServiceTests {
    @MockitoBean
    private final MatchRepository matchRepository;

    private final MatchService matchService;

    @Autowired
    public MatchServiceTests(MatchRepository matchRepository, MatchService matchService) {
        this.matchRepository = matchRepository;
        this.matchService = matchService;
    }

    @Test
    public void testFindMatchByIdReturnsMatchWhenExists() {
        Match matchA = CreateTestData.createMatchA();

        Mockito.when(matchRepository.findById(1L)).thenReturn(Optional.of(matchA));

        Match result = matchService.findById(1L);

        assertNotNull(result);
        assertEquals(matchA, result);
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

        Pageable pageable = PageRequest.of(0, 10);

        List<Match> matches = List.of(matchA);
        Page<Match> matchPage = new org.springframework.data.domain.PageImpl<>(matches);

        Mockito.when(matchRepository.findAll(pageable)).thenReturn(matchPage);

        Page<Match> result = matchService.findAll(pageable);

        assertEquals(1, result.getSize());
        assertEquals(matchA, result.stream().findFirst().get());
        Mockito.verify(matchRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void testCreateMatchSavesAndReturnsMatch() {
        Match matchA = CreateTestData.createMatchA();

        Mockito.when(matchRepository.save(matchA)).thenReturn(matchA);

        Match matchSaved = matchService.createMatch(matchA);

        assertNotNull(matchSaved);
        assertEquals(matchA, matchSaved);
        Mockito.verify(matchRepository, Mockito.times(1)).save(matchA);
    }

    @Test
    public void testUpdateMatchUpdatesAndReturnsMatch() throws BadRequestException {
        Match matchA = CreateTestData.createMatchA();

        Mockito.when(matchRepository.existsById(1L)).thenReturn(true);
        Mockito.when(matchRepository.save(matchA)).thenReturn(matchA);

        Match matchUpdated = matchService.updateMatch(1L, matchA);

        assertNotNull(matchUpdated);
        assertEquals(matchA, matchUpdated);
        Mockito.verify(matchRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(matchRepository, Mockito.times(1)).save(matchA);
    }

    @Test
    public void testUpdateMatchThrowsExceptionWhenNotExists() {
        Match matchA = CreateTestData.createMatchA();

        Mockito.when(matchRepository.existsById(1L)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> matchService.updateMatch(1L, matchA));

        Mockito.verify(matchRepository, Mockito.times(1)).existsById(1L);
    }

    @Test
    public void testDeleteMatchDeletesMatchWhenExists() throws BadRequestException {
        Mockito.when(matchRepository.existsById(1L)).thenReturn(true);

        matchService.deleteMatch(1L);

        Mockito.verify(matchRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(matchRepository, Mockito.times(1)).deleteById(1L);
    }
}

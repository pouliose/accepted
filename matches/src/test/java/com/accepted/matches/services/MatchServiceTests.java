package com.accepted.matches.services;

import com.accepted.matches.model.entities.Match;
import com.accepted.matches.repositories.MatchRepository;
import com.accepted.matches.utils.CreateTestData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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

        Optional<Match> result = matchService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(matchA, result.get());
        Mockito.verify(matchRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void testFindMatchByIdReturnsEmptyWhenNotExists() {
        Mockito.when(matchRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Match> result = matchService.findById(1L);

        assertFalse(result.isPresent());
        Mockito.verify(matchRepository, Mockito.times(1)).findById(1L);
    }
}

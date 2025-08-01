package com.accepted.matches.controllers;

import com.accepted.matches.mappers.Mapper;
import com.accepted.matches.model.dto.MatchDto;
import com.accepted.matches.model.entities.Match;
import com.accepted.matches.services.MatchService;
import com.accepted.matches.utils.CreateTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(MatchController.class)
public class MatchControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatchService matchService;

    @MockitoBean
    private Mapper<Match, MatchDto> matchMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private final String urlMatches = "/api/v1/matches/";

    @Test
    public void testGetMatchByIdReturnsHttpStatus200WhenMatchExists() throws Exception {

        Match matchA = CreateTestData.createMatchA();
        MatchDto matchADto = CreateTestData.createMatchADto();

        Mockito.when(matchService.findById(1L)).thenReturn(Optional.of(matchA));
        Mockito.when(matchMapper.mapTo(matchA)).thenReturn(matchADto);

        mockMvc.perform(MockMvcRequestBuilders.get(urlMatches + "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(matchADto)));
    }

    @Test
    public void testGetMatchByIdReturnsHttpStatusNotFound() throws Exception {
        Mockito.when(matchService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get(urlMatches + "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}

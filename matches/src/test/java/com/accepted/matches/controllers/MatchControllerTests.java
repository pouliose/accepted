package com.accepted.matches.controllers;

import com.accepted.matches.exceptions.MatchNotFoundException;
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

import java.util.ArrayList;
import java.util.List;
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

    private final String urlMatches = "/api/v1/matches";

    @Test
    public void testGetMatchByIdReturnsHttpStatus200WhenMatchExists() throws Exception {

        Match matchA = CreateTestData.createMatchA();
        MatchDto matchADto = CreateTestData.createMatchADto();

        Mockito.when(matchService.findById(1L)).thenReturn(matchA);
        Mockito.when(matchMapper.mapTo(matchA)).thenReturn(matchADto);

        mockMvc.perform(MockMvcRequestBuilders.get(urlMatches + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(matchADto)));
    }

    @Test
    public void testGetMatchByIdReturnsHttpStatusNotFound() throws Exception {
        Mockito.when(matchService.findById(anyLong())).thenThrow( new MatchNotFoundException(String.format("Match with %d does not exist.", 1)));

        mockMvc.perform(MockMvcRequestBuilders.get(urlMatches + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetAllMatchesReturnsHttpStatus200() throws Exception {

        Mockito.when(matchService.findAll(Mockito.any())).thenReturn(
                new org.springframework.data.domain.PageImpl<>(List.of()));

        mockMvc.perform(MockMvcRequestBuilders.get(urlMatches)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAllMatchesReturnsResultsWhenExist() throws Exception {
        Match matchA = CreateTestData.createMatchA();
        MatchDto matchADto = CreateTestData.createMatchADto();

        List<Match> matches = new ArrayList<>();
        matches.add(matchA);

        Mockito.when(matchService.findAll(Mockito.any())).thenReturn(
                new org.springframework.data.domain.PageImpl<>(matches));

        Mockito.when(matchMapper.mapTo(matchA)).thenReturn(matchADto);

        mockMvc.perform(MockMvcRequestBuilders.get(urlMatches)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].id").value(matchADto.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].description").value(matchADto.getDescription())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].matchDate").value(matchADto.getMatchDate().toString())
                );
    }

    @Test
    public void testCreateMatchReturnsCreatedMatch() throws Exception {
        MatchDto matchADto = CreateTestData.createMatchADto();
        Match matchA = CreateTestData.createMatchA();

        Mockito.when(matchService.createMatch(matchA)).thenReturn(matchA);

        /*Mockito.when(matchMapper.mapFrom(matchADto)).thenReturn(matchA);
        Mockito.when(matchMapper.mapTo(matchA)).thenReturn(matchADto);*/

        String requestAsJsonString = objectMapper.writeValueAsString(matchADto);
        mockMvc.perform(MockMvcRequestBuilders.post(urlMatches)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJsonString))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").value(matchADto.getId()))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(matchADto)));
    }

    @Test
    public void testUpdateMatchReturnsUpdatedMatch() throws Exception {
        MatchDto matchADto = CreateTestData.createMatchADto();
        Match matchA = CreateTestData.createMatchA();

        Mockito.when(matchService.updateMatch(1L, matchA)).thenReturn(matchA);
        Mockito.when(matchMapper.mapFrom(matchADto)).thenReturn(matchA);
        Mockito.when(matchMapper.mapTo(matchA)).thenReturn(matchADto);

        String requestAsJsonString = objectMapper.writeValueAsString(matchADto);
        mockMvc.perform(MockMvcRequestBuilders.put(urlMatches + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJsonString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(matchADto)));
    }

    @Test
    public void testDeleteMatchReturnsNoContent() throws Exception {
        Mockito.doNothing().when(matchService).deleteMatch(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete(urlMatches + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}

package com.accepted.matches.controllers;

import com.accepted.matches.exceptions.MatchOddsNotFoundException;
import com.accepted.matches.model.dto.MatchDto;
import com.accepted.matches.model.dto.MatchOddsDto;
import com.accepted.matches.services.MatchOddsService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest(MatchOddsController.class)
public class MatchOddsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatchOddsService matchOddsService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String urlMatches = "/api/v1/matches";
    private final String urlMatchesOdds = "/api/v1/matchesodds";

    @Test
    public void testGetMatchOddsByIdReturnsHttpStatus200WhenMatchOddsExists() throws Exception {
        MatchDto matchADto = CreateTestData.createMatchADto();
        MatchOddsDto matchOddsADto = CreateTestData.createMatchOddsADto(matchADto);

        Mockito.when(matchOddsService.findByMatchOddsId(matchOddsADto.getId())).thenReturn(matchOddsADto);

        mockMvc.perform(MockMvcRequestBuilders.get(urlMatchesOdds + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(matchOddsADto)));
    }

    @Test
    public void testGetMatchOddsByIdReturnsHttpStatusNotFoundWhenMatchOddsDoesNotExist() throws Exception {
        Mockito.when(matchOddsService.findByMatchOddsId(anyLong())).thenThrow(new MatchOddsNotFoundException(String.format("MatchOdds with %d does not exist.", 1)));

        mockMvc.perform(MockMvcRequestBuilders.get(urlMatchesOdds + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetAllMatchOddsReturnsHttpStatus200() throws Exception {

        Mockito.when(matchOddsService.findAll(Mockito.any())).thenReturn(
                new org.springframework.data.domain.PageImpl<>(List.of()));

        mockMvc.perform(MockMvcRequestBuilders.get(urlMatchesOdds)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAllMatchOddsReturnsResultsWhenExist() throws Exception {
        MatchDto matchADto = CreateTestData.createMatchADto();
        MatchOddsDto matchOddsADto = CreateTestData.createMatchOddsADto(matchADto);
        MatchOddsDto matchOddsBDto = CreateTestData.createMatchOddsBDto(matchADto);

        List<MatchOddsDto> matchOddsDtoList = List.of(matchOddsADto, matchOddsBDto);

        Mockito.when(matchOddsService.findAll(Mockito.any())).thenReturn(
                new org.springframework.data.domain.PageImpl<>(matchOddsDtoList));

        mockMvc.perform(MockMvcRequestBuilders.get(urlMatchesOdds)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].id").value(matchOddsADto.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].specifier").value(matchOddsADto.getSpecifier().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].odd").value(matchOddsADto.getOdd().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[0].match").value(matchOddsADto.getMatch().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[1].id").value(matchOddsBDto.getId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[1].specifier").value(matchOddsBDto.getSpecifier().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[1].odd").value(matchOddsBDto.getOdd().toString())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.content[1].match").value(matchOddsBDto.getMatch().toString())
                );
    }

    @Test
    public void testGetMatchOddsByMatchIdReturnsHttpStatus200WhenMatchExists() throws Exception {
        MatchDto matchADto = CreateTestData.createMatchADto();
        MatchOddsDto matchOddsADto = CreateTestData.createMatchOddsADto(matchADto);
        List<MatchOddsDto> matchOddsDtoList = List.of(matchOddsADto);
        Mockito.when(matchOddsService.findMatchOddsByMatchId(Mockito.eq(matchADto.getId()), Mockito.any()))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(matchOddsDtoList));

        mockMvc.perform(MockMvcRequestBuilders.get(urlMatches + "/1/odds")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(matchOddsADto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].specifier").value(matchOddsADto.getSpecifier().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].odd").value(matchOddsADto.getOdd()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].match").value(matchOddsADto.getMatch()));
    }

    @Test
    public void testCreateMatchOddsReturnsCreatedMatchOdds() throws Exception {
        MatchDto matchADto = CreateTestData.createMatchADto();
        MatchOddsDto matchOddsADto = CreateTestData.createMatchOddsADto(matchADto);

        Mockito.when(matchOddsService.createMatchOdds(matchOddsADto.getId(), matchOddsADto))
                .thenReturn(matchOddsADto);

        String requestAsJsonString = objectMapper.writeValueAsString(matchOddsADto);
        mockMvc.perform(MockMvcRequestBuilders.post(urlMatches + "/1/" + "odds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJsonString))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(matchOddsADto)));
    }

    @Test
    public void testUpdateMatchOddsReturnsUpdatedMatchOdds() throws Exception {
        MatchDto matchADto = CreateTestData.createMatchADto();
        MatchOddsDto matchOddsADtoToBeUpdated = CreateTestData.createMatchOddsADto(matchADto);

        Mockito.when(matchOddsService.updateMatchOdds(matchOddsADtoToBeUpdated.getId(), matchOddsADtoToBeUpdated)).thenReturn(matchOddsADtoToBeUpdated);

        String requestAsJsonString = objectMapper.writeValueAsString(matchOddsADtoToBeUpdated);
        mockMvc.perform(MockMvcRequestBuilders.put(urlMatchesOdds + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJsonString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(matchOddsADtoToBeUpdated)));
    }

    @Test
    public void testUpdateMatchOddsReturnsMatchOddsNotFoundExceptionWhenMatchOddsDoesNotExist() throws Exception {
        MatchDto matchADto = CreateTestData.createMatchADto();
        MatchOddsDto matchOddsADtoToBeUpdated = CreateTestData.createMatchOddsADto(matchADto);

        Mockito.when(matchOddsService.updateMatchOdds(matchOddsADtoToBeUpdated.getId(), matchOddsADtoToBeUpdated))
                .thenThrow(new MatchOddsNotFoundException(String.format("MatchOdds with %d does not exist.", matchOddsADtoToBeUpdated.getId())));

        String requestAsJsonString = objectMapper.writeValueAsString(matchOddsADtoToBeUpdated);
        mockMvc.perform(MockMvcRequestBuilders.put(urlMatchesOdds + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJsonString))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteMatchOddsReturnsNoContentWhenMatchOddsExists() throws Exception {
        long matchOddsId = 1L;
        Mockito.doNothing().when(matchOddsService).deleteMatchOdds(matchOddsId);

        mockMvc.perform(MockMvcRequestBuilders.delete(urlMatchesOdds + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(matchOddsService, Mockito.times(1)).deleteMatchOdds(matchOddsId);
    }

    @Test
    public void testDeleteMatchOddsReturnsBadRequestWhenMatchOddsDoesNotExist() throws Exception {
        long matchOddsId = 1L;
        Mockito.doThrow(new MatchOddsNotFoundException(String.format("MatchOdds with %d does not exist.", matchOddsId)))
                .when(matchOddsService).deleteMatchOdds(matchOddsId);

        mockMvc.perform(MockMvcRequestBuilders.delete(urlMatchesOdds + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}

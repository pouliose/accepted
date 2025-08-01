package com.accepted.matches.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/matches")
public class MatchController {
    @GetMapping
    public String getMatches() {
        return "List of matches";
    }
}

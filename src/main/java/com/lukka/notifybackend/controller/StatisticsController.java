package com.lukka.notifybackend.controller;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @GetMapping("/")
    public String login() throws IOException {
        return "test";
    }

}

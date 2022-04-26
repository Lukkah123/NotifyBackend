package com.lukka.notifybackend.controller;

import com.lukka.notifybackend.exception.EmptyRepositoryException;
import com.lukka.notifybackend.model.Statistics;
import com.lukka.notifybackend.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("")
    public Statistics getStatistics() {
        try {
            return statisticsService.updateVisitorCount();
        } catch (EmptyRepositoryException e) {
            return new Statistics();
        }
    }

}

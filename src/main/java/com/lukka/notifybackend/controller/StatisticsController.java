package com.lukka.notifybackend.controller;

import com.lukka.notifybackend.exception.EmptyRepositoryException;
import com.lukka.notifybackend.model.User;
import com.lukka.notifybackend.service.NoteService;
import com.lukka.notifybackend.service.StatisticsService;
import com.lukka.notifybackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final UserService userService;
    private final NoteService noteService;

    public StatisticsController(StatisticsService statisticsService, UserService userService, NoteService noteService) {
        this.statisticsService = statisticsService;
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping("")
    public HashMap<String, Object> getStatistics() {
        HashMap<String, Object> map = new HashMap<>();
        try {
            List<User> users = userService.getAllUsers();
            map.put("totalUsers", users.size());
            map.put("pageVisits", statisticsService.updateVisitorCount());
            map.put("totalNotes", noteService.getAllNotes().size());
            return map;
        } catch (EmptyRepositoryException e) {
            return new HashMap<>();
        }
    }

}

package com.lukka.notifybackend.service;

import com.lukka.notifybackend.exception.EmptyRepositoryException;
import com.lukka.notifybackend.model.Statistics;
import com.lukka.notifybackend.repo.StatisticsRepo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StatisticsService {

    private final StatisticsRepo statisticsRepo;

    public StatisticsService(StatisticsRepo statisticsRepo) {
        this.statisticsRepo = statisticsRepo;
    }

    public List<Statistics> getStatistics() {
        List<Statistics> users = statisticsRepo.findAll();
        if (!users.isEmpty())
            return users;
        else
            throw new EmptyRepositoryException("GET", "User");
    }

    @Transactional
    public Statistics updateVisitorCount() {
        checkIfNoStatistics();
        Statistics statistics = statisticsRepo.getById(0);
        int visitorCount = statistics.getVisitorCount() + 1;
        statistics.setVisitorCount(visitorCount);
        return statistics;
    }

    @Transactional
    public void checkIfNoStatistics() {
        if (statisticsRepo.findAll().isEmpty()) {
            Statistics statistics = new Statistics();
            statistics.setVisitorCount(0);
            statisticsRepo.save(statistics);
        }
    }
}

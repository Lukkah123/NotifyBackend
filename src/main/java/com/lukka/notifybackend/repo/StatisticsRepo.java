package com.lukka.notifybackend.repo;

import com.lukka.notifybackend.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepo extends JpaRepository<Statistics, Integer> {
}

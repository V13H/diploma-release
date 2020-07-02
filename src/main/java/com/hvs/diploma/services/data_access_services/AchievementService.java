package com.hvs.diploma.services.data_access_services;

import com.hvs.diploma.dao.main.AchievementRepository;
import com.hvs.diploma.entities.Achievement;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;

    public AchievementService(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    List<Achievement> findAll() {
        return achievementRepository.findAll();
    }

    public long countAll() {
        return achievementRepository.count();
    }

    Achievement findByTitle(String title) {
        return achievementRepository.findAchievementByTitle(title);
    }

    Set<Achievement> findAllByTitleIsNot(String title) {
        return achievementRepository.findAchievementsByTitleIsNot(title);
    }

    void save(Achievement achievement) {
        achievementRepository.save(achievement);
    }
}

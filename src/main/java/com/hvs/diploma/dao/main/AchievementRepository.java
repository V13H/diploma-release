package com.hvs.diploma.dao.main;

import com.hvs.diploma.entities.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Achievement findAchievementByTitle(String title);

    Set<Achievement> findAchievementsByTitleIsNot(String title);
}

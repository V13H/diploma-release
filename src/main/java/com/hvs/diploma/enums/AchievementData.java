package com.hvs.diploma.enums;

import com.hvs.diploma.entities.Achievement;

//Main purpose of this class is to simplify Achievements initialization
public enum AchievementData {
    START("3..2..1..lift off!",
            "You have successfully added your first task",
            "/img/rocket.jpg"),
    ACCURACY("Accuracy",
            "You have completed 2 tasks and don`t have any expired tasks.Awesome!",
            "/img/hitting-target.jpg"),
    DISCIPLINE("Discipline",
            "You have completed 3 tasks and reached a high success rate",
            "/img/discipline.jpg"),
    COMEBACK("Comeback",
            "Great!You have successfully restarted and completed a failed task.",
            "/img/get-up.jpg"),
    FIRST_WIN("First win",
            "Congratulations!You have completed your first task",
            "/img/win-cup.jpg"),
    STEP_BY_STEP("Step by step",
            "You have completed 4 tasks",
            "/img/progress.jpg"),
    AIMING_HIGH("Aiming high",
            "You have added 2 high priority tasks",
            "/img/aim.jpg"),
    MARATHON("Marathon",
            "You have completed 3 tasks per day",
            "/img/runner-line.jpg"),
    VETERAN("Veteran",
            "You have completed 6 tasks",
            "/img/veteran.jpg"),
    SPARTAN("Spartan", "You have completed 7 tasks and don`t have any expired tasks.",
            "/img/spartan.jpg"),
    GURU("Guru", "You have unlocked all achievements and completed 10 tasks",
            "/img/guru.jpg");

    private String title;
    private String description;
    private String pictureUrl;

    AchievementData(String title, String description, String pictureUrl) {
        this.title = title;
        this.description = description;
        this.pictureUrl = pictureUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public Achievement getAchievement() {
        return new Achievement(this.title, this.description, this.pictureUrl);
    }
}

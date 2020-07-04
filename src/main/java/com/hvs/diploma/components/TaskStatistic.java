package com.hvs.diploma.components;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@ToString
public class TaskStatistic {
    private long notActiveTasksCount;
    private long activeTasksCount;
    private long doneTasksCount;
    private long expiredTasksCount;
    private long notificationsCount;
    private long restartedAndDoneTasksCount;
    private long highPriorityTasksCount;
    private long mediumPriorityTasksCount;
    private long lowPriorityTasksCount;
    private int achievementsUnlocked;
    private double successRate;
    private String progressBarColorToTemplate;


    public TaskStatistic() {
    }

    public void calculateSuccessRate() {
        if (notActiveTasksCount > 0) {
            double succRate = (double) doneTasksCount / notActiveTasksCount * 100;
            BigDecimal res = new BigDecimal(succRate).setScale(2, RoundingMode.HALF_DOWN);
            successRate = res.doubleValue();
            setProgressBarColor(successRate);
        }
    }

    private void setProgressBarColor(Double successRate) {
        if (successRate >= 50.0 && successRate <= 70.0) {
            progressBarColorToTemplate = "yellow";
        } else if (successRate >= 70.0) {
            progressBarColorToTemplate = "green";
        } else {
            progressBarColorToTemplate = "red";
        }
    }

    public void setRestartedAndDoneTasksCount(long count) {
        restartedAndDoneTasksCount = count;
    }


}

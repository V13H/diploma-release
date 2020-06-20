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
    private long totalTasksCount;
    private long activeTasksCount;
    private long doneTasksCount;
    private long expiredTasksCount;
    private long notificationsCount;
    private double successRate;
    private String progressBarColorToTemplate;

    public TaskStatistic() {

    }

    public BigDecimal getSuccessRate() {
        double succRate = (double) doneTasksCount / totalTasksCount * 100;
        BigDecimal res = new BigDecimal(succRate).setScale(2, RoundingMode.HALF_DOWN);
        successRate = res.doubleValue();
        setProgressBarColor(successRate);
        return res;
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

}

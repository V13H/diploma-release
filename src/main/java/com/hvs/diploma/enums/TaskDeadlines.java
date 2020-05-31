package com.hvs.diploma.enums;

import com.hvs.diploma.util.DateHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public enum TaskDeadlines {
    TODAY(DateHelper.today()),
    TOMORROW(DateHelper.tomorrow()),
    THIS_WEEK(DateHelper.lastDayOfCurrentWeek());
    private Timestamp value;

    TaskDeadlines(Timestamp value) {
        this.value = value;
    }

    public Timestamp getValue() {
        return value;
    }

    public static List<Timestamp> getValues(List<TaskDeadlines> taskDeadlines) {
        List<Timestamp> timestamps = new ArrayList<>();
        for (TaskDeadlines taskDeadline : taskDeadlines) {
            timestamps.add(taskDeadline.getValue());
        }
        return timestamps;
    }
}

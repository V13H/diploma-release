package com.hvs.diploma.enums;

import com.hvs.diploma.util.DateTimeHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public enum TaskDeadlines {
    TODAY(DateTimeHelper.today()),
    TOMORROW(DateTimeHelper.tomorrow()),
    THIS_WEEK(DateTimeHelper.lastDayOfCurrentWeek());
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

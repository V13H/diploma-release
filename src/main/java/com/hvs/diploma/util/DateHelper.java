package com.hvs.diploma.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

public class DateHelper {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private DateHelper() {
    }

    public static Timestamp today() {
        LocalDate localDate = LocalDate.now();
        return Timestamp.valueOf(localDate.atStartOfDay());
    }

    public static Timestamp tomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay());
    }

    public static Timestamp lastDayOfCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 8);
        DayOfWeek lastDay = DayOfWeek.SUNDAY;
        LocalDate now = LocalDate.now();
        int daysToAdd = lastDay.getValue() - now.getDayOfWeek().getValue();
        int lastDayOfCurrentWeekValue = now.getDayOfMonth() + daysToAdd;
        return Timestamp.valueOf(LocalDate.of(now.getYear(), now.getMonth(), lastDayOfCurrentWeekValue).atStartOfDay());
    }

    public static Timestamp maxDate() {
        return Timestamp.valueOf(LocalDate.MAX.atStartOfDay());
    }
}

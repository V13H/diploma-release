package com.hvs.diploma.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class DateHelper {

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
        DayOfWeek lastDay = DayOfWeek.SUNDAY;
        LocalDate now = LocalDate.now();
        int daysToAdd = lastDay.getValue() - now.getDayOfWeek().getValue();
        int lastDayOfCurrentWeekValue = now.getDayOfMonth() + daysToAdd;
        return Timestamp.valueOf(LocalDate.of(now.getYear(), now.getMonth(), lastDayOfCurrentWeekValue).atStartOfDay());
    }


    public static Timestamp maxDate() {
        return Timestamp.valueOf(LocalDate.MAX.atStartOfDay());
    }

    public static Timestamp firstDayOfCurrentWeek() {
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        DayOfWeek firstDay = DayOfWeek.MONDAY;
        return Timestamp.valueOf(now.minusDays(dayOfWeek.getValue() - firstDay.getValue()).atStartOfDay());
    }

    public static SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
    }

    public static SimpleDateFormat sqlFormat() {
        return new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
    }

}

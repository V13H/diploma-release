package com.hvs.diploma.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Locale;

//Helper class for getting frequently used dates
public class DateTimeHelper {
    private static final String TIME_PATTERN = "^[0-9]{2}[:][0-9]{2}";
    private static final String DATE_PATTERN = "^[0-9]{2}[.][0-9]{2}[.][0-9]{4}";

    private DateTimeHelper() {
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
        LocalDate lastDayOfCurrentWeek = now.plusDays(daysToAdd);
        return Timestamp.valueOf(lastDayOfCurrentWeek.atStartOfDay());
    }

    public static Timestamp firstDayOfCurrentWeek() {
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        DayOfWeek firstDay = DayOfWeek.MONDAY;
        int daysToSubtract = dayOfWeek.getValue() - firstDay.getValue();
        LocalDate firstDayOfCurrentWeek = now.minusDays(daysToSubtract);
        return Timestamp.valueOf(firstDayOfCurrentWeek.atStartOfDay());
    }

    public static SimpleDateFormat simpleDateFormat() {
        String datePattern = "dd.MM.yyyy";
        return new SimpleDateFormat(datePattern, Locale.ENGLISH);
    }

    public static boolean hasValidFormat(String value, String pattern) {
        return value.matches(pattern);
    }

    public static LocalTime parseTime(String time) throws IllegalArgumentException, ParseException {
        if (hasValidFormat(time, TIME_PATTERN)) {
            String[] split = time.split(":");
            int hours = Integer.parseInt(split[0]);
            int minutes = Integer.parseInt(split[1]);
            if (hasValidValue(hours, 23) && hasValidValue(minutes, 59)) {
                return LocalTime.of(hours, minutes);
            } else {
                throw new IllegalArgumentException("Invalid time values.");
            }
        } else {
            throw new ParseException("Invalid time format", 1);
        }
    }

    public static Timestamp buildTimestampInstance(String date, String time) throws ParseException {
        LocalDate localDate = parseDate(date);
        LocalTime localTime = parseTime(time);
        LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);
        return Timestamp.valueOf(dateTime);
    }

    public static LocalDate parseDate(String date) throws ParseException, IllegalArgumentException {
        if (hasValidFormat(date, DATE_PATTERN)) {
            String[] split = date.split("\\.");
            int day = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            int year = Integer.parseInt(split[2]);
            if (hasValidValues(day, month, year)) {
                return LocalDate.of(year, month, day);
            } else {
                throw new IllegalArgumentException("Invalid date values");
            }
        } else {
            throw new ParseException("Invalid date format", 1);
        }
    }

    private static boolean hasValidValues(int day, int month, int year) {
        if (hasValidValue(year, LocalDate.now().getYear(), 2100) &&
                hasValidValue(month, 1, 12)) {
            Month month1 = Month.of(month);
            boolean isLeap = Year.isLeap(year);
            int length = month1.length(isLeap);
            return day <= length && day > 0;
        } else {
            return false;
        }
    }

    private static boolean hasValidValue(int arg, int max) {
        return arg <= max && arg >= 0;
    }

    private static boolean hasValidValue(int arg, int min, int max) {
        return arg <= max && arg >= min;
    }

}

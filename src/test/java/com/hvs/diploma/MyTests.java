package com.hvs.diploma;

import com.hvs.diploma.components.TaskStatistic;
import com.hvs.diploma.services.validation_services.task_dto_validators.TimeValidator;
import com.hvs.diploma.util.DateTimeHelper;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MyTests {
    private TimeValidator timeValidator = new TimeValidator();
    private static final String TIME_PATTERN = "^[0-9]{2}[:][0-9]{2}";
    private static final String DATE_PATTERN = "^[0-9]{2}[.][0-9]{2}[.][0-9]{4}";

    @Test
    void timeParserTest() {
        String time = "23:30";
        LocalDate now = LocalDate.now();
        String[] split = time.split(":");
        int hours = Integer.parseInt(split[0]);
        int minutes = Integer.parseInt(split[1]);
        LocalDateTime localDateTime = now.atTime(hours, minutes);
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        System.out.println(localDateTime);
    }

    @Test
    void dateParser() {
        String[] validDates = new String[]{"30.06.2020", "29.02.2020", "03.01.2020"};
        String[] invalidDates = new String[]{"qqwr1214", "32.02.2020", "01.02.2232", "29.02.2019"};
        for (String validDate : validDates) {
            try {
                LocalDate localDate = DateTimeHelper.parseDate(validDate);
                System.out.println(localDate.toString());
            } catch (ParseException e) {
                System.out.println(validDate + " : " + e.getMessage());
            }
        }
        for (String invalidDate : invalidDates) {
            try {
                LocalDate localDate = DateTimeHelper.parseDate(invalidDate);
                System.out.println(localDate.toString());
            } catch (Exception e) {
                System.out.println(invalidDate + " : " + e.getMessage());
            }
        }
    }

    @Test
    void validPattern() {
        String[] times = new String[]{"12:20", "32:20", "00:00"};
        String[] invalidTimes = new String[]{"11-=23", "qwer", "12223", "23x112d"};
        for (String time : times) {
            assertTrue(DateTimeHelper.hasValidFormat(time, TIME_PATTERN), time + " is valid");
        }
        for (String invalidTime : invalidTimes) {
            assertFalse(DateTimeHelper.hasValidFormat(invalidTime, TIME_PATTERN), invalidTime + " is invalid");
        }
    }

    @Test
    void randomNum() {
        for (int i = 0; i < 9; i++) {
            Double random = Math.random() * 10000;
            System.out.println(i + 1 + ": " + random.intValue());
        }
    }

    @Test
    void succRateTest() {
        TaskStatistic statistic = new TaskStatistic();
        statistic.setNotActiveTasksCount(10);
        statistic.setDoneTasksCount(6);
        System.out.println(statistic.toString());
        statistic.setNotActiveTasksCount(100);
        statistic.setDoneTasksCount(33);
        System.out.println(statistic.toString());
    }
}
